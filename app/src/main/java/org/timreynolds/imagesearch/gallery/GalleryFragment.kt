package org.timreynolds.imagesearch.gallery


import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.timreynolds.imagesearch.R
import org.timreynolds.imagesearch.data.db.Gallery
import org.timreynolds.imagesearch.gallery.adapter.GalleryAdapter
import org.timreynolds.imagesearch.gallery.adapter.PhotoDelegateAdapter
import org.timreynolds.imagesearch.photodetail.INTENT_PHOTO
import org.timreynolds.imagesearch.photodetail.PhotoDetailActivity
import org.timreynolds.imagesearch.util.*

/**
 * GalleryFragment
 */
class GalleryFragment : Fragment(), GalleryContract.View, PhotoDelegateAdapter.onViewSelectedListener {

    private var galleryPresenter: GalleryContract.Presenter? = null

    val LIST_STATE_KEY = "recycler_list_state"
    var listState: Parcelable? = null

    class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceHeight
                }
                left =  spaceHeight
                right = spaceHeight
                bottom = spaceHeight
            }
        }
    }

    companion object {
        fun newInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.i(TAG, "** onViewStateRestored ** hit")
        // Retrieve list state and list/item positions
        if(savedInstanceState != null) {
            Log.i(TAG, "** onViewStateRestored hit ** saved instance not null")
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onError(resId: Int) {
        val message = getString(resId)
        context.toast(message)
    }

    override fun onSaveInstanceState(state: Bundle?) {
        Log.i(TAG, "** onSaveInstanceState hit **")
        // Save list state
        listState = photos_list.layoutManager.onSaveInstanceState()
        state?.putParcelable(LIST_STATE_KEY, listState)
        super.onSaveInstanceState(state)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_gallery)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // configure RecyclerView
        photos_list.apply {
            setHasFixedSize(true)
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.default_padding).toInt()))
            layoutManager = GridLayoutManager(context, 2)
            clearOnScrollListeners()
        }

        if(savedInstanceState != null) {

        }
        initAdapter()
    }

    private fun initAdapter() {
        if (photos_list.adapter == null) {
            photos_list.adapter = GalleryAdapter(this)
        }
    }

    override fun showSelectedPhotos(selectedPhotos: List<Gallery>) {
        if (photos_list!!.adapter != null) {
            Log.i(TAG, "** showSelectedPhotos photos_list IS NOT NULL **")
            (photos_list.adapter as GalleryAdapter).addSelectedPhotos(selectedPhotos)
            saveSelectedIdsToPrefs(selectedPhotos)

        } else {
            Log.i(TAG, "** showSelectedPhotos adapter is NULL **")
        }
    }

    private fun saveSelectedIdsToPrefs(results: List<Gallery>) {
        val sharedPreference =  context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val set = HashSet<String>()
        for(item in results) {
            set.add(item.flickrId.toString())
        }
        editor.putStringSet("savedIds", set)
        // NOTE: using apply instead of commit, as apply performs task in background
        editor.apply()

    }

    override fun onResume() {
        super.onResume()
        galleryPresenter?.subscribe()

        if (listState != null) {
            Log.i(TAG, "** onResume hit ** listState ? -> " + listState.toString())
            photos_list.layoutManager.onRestoreInstanceState(listState)
        }
    }

    override fun isNetworkConnected(): Boolean {
        return NetworkUtils.isNetworkConnected(context)
    }

    override fun onPause() {
        super.onPause()
        galleryPresenter?.unsubscribe()
    }

    override fun setPresenter(presenter: GalleryContract.Presenter) {
        galleryPresenter = presenter
    }

    override fun showLoadingPhotosError() {
        context.toast(getString(R.string.loading_photos_error))
    }

    override fun isActive(): Boolean = isAdded

    override fun onItemSelected(photo: Gallery) {
        context.launchActivity<PhotoDetailActivity> {
            putExtra(INTENT_PHOTO, photo)
        }
    }

}
