package org.timreynolds.imagesearch.gallery.adapter

import android.content.ContentValues
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import org.timreynolds.imagesearch.R
import org.timreynolds.imagesearch.data.models.SearchResults
import org.timreynolds.imagesearch.search.SearchActivity
import org.timreynolds.imagesearch.search.adapter.SearchAdapterInterface
import org.timreynolds.imagesearch.search.adapter.SearchPhotoViewHolder
import org.timreynolds.imagesearch.search.adapter.SearchViewHolderClickListener
import org.timreynolds.imagesearch.util.TAG
import org.timreynolds.imagesearch.util.loadImage


class SearchAdapter(val context: Context, val searchAdapterInterface: SearchAdapterInterface) : RecyclerView.Adapter<SearchPhotoViewHolder>(),
        SearchViewHolderClickListener {


    var searchList: MutableList<SearchResults.FlickrPhoto> = java.util.ArrayList<SearchResults.FlickrPhoto>()

    val selectedIds: MutableList<Long> = java.util.ArrayList<Long>()

    var selectedImages: ArrayList<SearchResults.FlickrPhoto> = ArrayList()

    /**
     * onLongTap - intiates multi-selection
     */
    override fun onLongTap(index: Int) {
        // enables multi-select
        if (!SearchActivity.isMultiSelectOn) {
            SearchActivity.isMultiSelectOn = true
        }
        addToSelectedIds(index)
    }

    override fun onTap(index: Int) {
        // First intial long press will activate additional selections with onTap
        if (SearchActivity.isMultiSelectOn) {
            addToSelectedIds(index)
        } else {
            Toast.makeText(context, "Clicked Item @ Position ${index + 1}", Toast.LENGTH_SHORT).show()
        }
    }
    /*
     * addToSelectedIds - stores all images user has selected in list
     */
    fun addToSelectedIds(index: Int) {
        // NOTE: should be comparing Long values only
        val id = searchList[index].id
        if (selectedIds.contains(id))
            selectedIds.remove(id)
        else
            selectedIds.add(id)

        notifyItemChanged(index)
        if (selectedIds.size < 1) {
            SearchActivity.isMultiSelectOn = false
        }
        searchAdapterInterface.searchAction(selectedIds.size, "increment", null)
        Log.i(TAG, "** bottom addToSelectedIds **")
    }

    override fun getItemCount() = searchList.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchPhotoViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val itemView = inflater.inflate(R.layout.activity_search_view_holder, parent, false)
        return SearchPhotoViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: SearchPhotoViewHolder?, position: Int) {
        var myTitle: String?
        if(searchList[position].title.length < 1) {
            myTitle = "N/A"
        } else {
            myTitle = searchList[position].title
        }
        holder?.textView?.setText(myTitle)

        val id = searchList[position].id

        // NOTE: Using static url which is larger then thumbnail but will be scaled down with Picasso
        var staticUrl: String?
        // Create static url  and center crop larger image
        staticUrl = "https://farm" + searchList[position].farm + ".staticflickr.com/"+
                searchList[position].server + "/" + searchList[position].id + "_" + searchList[position].secret + ".jpg"
        holder?.imageView?.loadImage(staticUrl)

        if (selectedIds.contains(id)) {
            //if item is selected then,set foreground color of FrameLayout.
            holder?.linearLayout?.background = ColorDrawable(ContextCompat.getColor(context, R.color.colorControlActivated))
        } else {
            //else remove selected item color.
            holder?.linearLayout?.background = ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent))
        }
    }

    /*
     * deleteSelectedIds - will remove selected ids from list after user selects them and send user to Gallery view
     */
    fun deleteSelectedIds() {
        if (selectedIds.size < 1) return
        val selectedIdIteration = selectedIds.listIterator();

        while (selectedIdIteration.hasNext()) {

            val selectedItemID = selectedIdIteration.next()
            Log.i(TAG, "The ID is $selectedItemID")
            var indexOfModelList = 0
            val modelListIteration: MutableListIterator<SearchResults.FlickrPhoto> = searchList.listIterator();
            while (modelListIteration.hasNext()) {
                val model = modelListIteration.next()
                if (selectedItemID.equals(model.id)) {
                    modelListIteration.remove()
                    selectedIdIteration.remove()
                    notifyItemRemoved(indexOfModelList)
                    // add Parcelable data for image selected to user gallery
                    selectedImages.add(model)
                }
                indexOfModelList++
            }
            // resets multi select mode
            SearchActivity.isMultiSelectOn = false
        }

        notifyDataSetChanged()
        // send user to gallery
        searchAdapterInterface.searchAction(0, "gallery", selectedImages)
        selectedImages = ArrayList()
    }

    /*
     * Add Search Images to list, but first compare with saved image ids in user gallery and remove any photos already saved
     */
    fun addFlickrSearchPhotos(photos: ArrayList<SearchResults.FlickrPhoto>, savedIds: MutableSet<String>) {

        Log.i(ContentValues.TAG, "** photos --> " + photos.size)
        if (savedIds != null && savedIds.size > 0) {
            for (savedId in savedIds) {
                var indexOfPhotoList = 0
                val photoListIteration: MutableListIterator<SearchResults.FlickrPhoto> = photos.listIterator();
                while (photoListIteration.hasNext()) {
                    val photoModel = photoListIteration.next()
                    // NOTE: Set id to String when comparing to saved id
                    if (savedId == photoModel.id.toString()) {
                        Log.i(TAG, "** REMOVE SAVED IMAGE saved id -> " + savedId + " : model id -> " + photoModel.id.toString())
                        photoListIteration.remove()
                        //notifyItemRemoved(indexOfPhotoList)
                    }
                    indexOfPhotoList++
                }
            }
        }
        searchList.clear()
        searchList.addAll(photos)
        notifyDataSetChanged()
    }

    fun clear() {
        searchList.clear();
        notifyDataSetChanged();
    }

}