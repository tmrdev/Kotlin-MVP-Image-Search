package org.timreynolds.imagesearch.search


import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SearchView
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_search.*
import org.timreynolds.imagesearch.Injection
import org.timreynolds.imagesearch.R
import org.timreynolds.imagesearch.data.models.SearchResults
import org.timreynolds.imagesearch.data.source.remote.api.FLICKR_API_KEY
import org.timreynolds.imagesearch.gallery.GalleryActivity
import org.timreynolds.imagesearch.gallery.SearchContract
import org.timreynolds.imagesearch.gallery.SearchPresenter
import org.timreynolds.imagesearch.gallery.adapter.SearchAdapter
import org.timreynolds.imagesearch.search.adapter.SearchAdapterInterface
import org.timreynolds.imagesearch.util.TAG
import org.timreynolds.imagesearch.util.logdebug
import org.timreynolds.imagesearch.util.toast


class SearchActivity : AppCompatActivity(), SearchAdapterInterface, SearchContract.View {

    var actionMode: ActionMode? = null
    var myAdapter: SearchAdapter? = null

    private var searchPresenter: SearchContract.Presenter? = null

    private val BACK_STACK_ROOT_TAG = "root_fragment"

    val LIST_STATE_KEY = "search_recycler_list_state"
    var listState: Parcelable? = null
    val SEARCH_TAG_KEY = "search_tag_state"
    var searchTagState: String? = null

    companion object {
        var isMultiSelectOn = false
        // val TAG = "SearchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbarSearch)

        // Stetho for testing sql db
        Stetho.initializeWithDefaults(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        var tagString: String?
        logdebug("** onCreate hit **")
        if(savedInstanceState != null) {
            tagString = savedInstanceState.getString(SEARCH_TAG_KEY)
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY)
        } else{
            // load cars as default
            // tagString = "cars"
            tagString = ""
        }
        searchPresenter = SearchPresenter(tagString!!, Injection.provideMessagesRepository(), this, Injection.provideSchedulerProvider())
        searchViewSetup(searchView)
        isMultiSelectOn = false

        //val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(SimpleDividerItemDecoration(applicationContext!!));
            // TODO: Can easily switch between Grid and Layout with declarations below
            // layoutManager = GridLayoutManager(context, 2)

            // Swithcing to LinearLayout for multi-select first
            layoutManager = LinearLayoutManager(context)
            clearOnScrollListeners()
        }

        myAdapter = SearchAdapter(this, this)
        searchRecyclerView.adapter = myAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        toolbarSearch.inflateMenu(R.menu.menu_main)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_gallery -> {
            val intent = Intent(this, GalleryActivity::class.java)
            ContextCompat.startActivity(this, intent, null)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        logdebug("** onBackPressed top hit **")
        super.onBackPressed()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("onRestoreInstanceState", "** restore instance **")
        // Retrieve list state and list/item positions
        if(savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY)
            searchTagState = savedInstanceState.getString(SEARCH_TAG_KEY)
        }
    }

    override fun onSaveInstanceState(state: Bundle?) {
        Log.i("onsaveinstance", "** on save instance **")
        // Save list state
        listState = searchRecyclerView.layoutManager.onSaveInstanceState();
        state?.putParcelable(LIST_STATE_KEY, listState);
        searchTagState = searchView.query.toString()
        state?.putString(SEARCH_TAG_KEY, searchTagState);
        super.onSaveInstanceState(state)
    }

    override fun onResume() {
        super.onResume()
        searchPresenter?.subscribe()
        if(searchTagState != null)
            logdebug("** onResume ** tag value -> " + searchTagState)

        this.searchTagState?.let { searchPresenter?.loadFlickrPhotos(it, FLICKR_API_KEY) }
        if (listState != null) {
            logdebug("** onResume ** listState is not null **")
            searchRecyclerView.layoutManager.onRestoreInstanceState(listState)
        }
    }

    override fun onPause() {
        super.onPause()
        searchPresenter?.unsubscribe()
    }

    /*
     * searchAction - interface between Search Adapter and Search Activity for incrementing selected images (display) and
     * passing selected images to GalleryActivity
     */
    override fun searchAction(size: Int, actionType: String, selectedImagesList: ArrayList<SearchResults.FlickrPhoto>?) {
        if(actionType == "increment") {
            var imageTitle: String = "Image"
            if (actionMode == null) actionMode = startActionMode(ActionModeCallback())
            if (size > 1) imageTitle = "Images"
            if (size > 0) actionMode?.setTitle("Add $size $imageTitle To Gallery")

            else actionMode?.finish()
            logdebug("searchAdapterInterface bottom hit **")
        } else if(actionType == "gallery") {
            Log.i(TAG, "passSelectedValueToGallery hit ** selectedImagesList total -> " + selectedImagesList?.size)
            //addPhotosToDB(selectedImagesList!!.toList())
            searchPresenter?.saveSelectedImagesToDB(selectedImagesList!!.toList())
        }
    }

    override fun gotoGalleryActivity() {
        val intent = Intent(this, GalleryActivity::class.java)
        ContextCompat.startActivity(this, intent, null)
    }

    override fun showFlickrPhotos(results: SearchResults.FlickrPhotos) {
        if (results.photos.photo.size > 0) {
            // Load saved image ids from user gallery and do not display them in search results
            val sharedPreference =  getSharedPreferences("PREFS", Context.MODE_PRIVATE)
            // NOTE: Must set HashSet() instead of null for savedIds
            val savedIds = sharedPreference.getStringSet("savedIds", HashSet())
            (searchRecyclerView.adapter as SearchAdapter).addFlickrSearchPhotos(results.photos.photo
                    as ArrayList<SearchResults.FlickrPhoto>, savedIds)
        } else {
            logdebug("showFlickrPhotos ** loading error **")
            showLoadingPhotosError()
        }
    }

    override fun showLoadingPhotosError() {
        toast(getString(R.string.loading_photos_error))
    }

    override fun setPresenter(presenter: SearchContract.Presenter) {
        searchPresenter = presenter
    }

    private fun searchViewSetup(search: SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                logdebug("** on query text submit ** -> " + query)
                (searchRecyclerView.adapter as SearchAdapter).clear()
                searchPresenter?.loadFlickrPhotos(query!!, FLICKR_API_KEY)
                // set clearFocus to keep keyboard hidden when coming back to activity
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                logdebug("** on query text change ** --> " + newText)
                return true
            }
        })
    }

    /*
     * For RecylerView Multi Selection
     */
    inner class ActionModeCallback : ActionMode.Callback {
        var shouldResetRecyclerView = true
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                R.id.action_add -> {
                    shouldResetRecyclerView = false
                    myAdapter?.deleteSelectedIds()
                    actionMode?.setTitle("") //remove item count from action mode.
                    actionMode?.finish()
                    return true
                }
            }
            return false
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val inflater = mode?.getMenuInflater()
            inflater?.inflate(R.menu.action_mode_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menu?.findItem(R.id.action_add)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            if (shouldResetRecyclerView) {
                myAdapter?.selectedIds?.clear()
                myAdapter?.notifyDataSetChanged()
            }
            isMultiSelectOn = false
            actionMode = null
            shouldResetRecyclerView = true
        }
    }

    /**
     * SimpleDividerItemDecoration - add divider line to recylerview list
     */
    inner class SimpleDividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
        private val mDivider: Drawable

        init {
            // mDivider = context.getResources().getDrawable(R.drawable.line_divider)
            mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
        }

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)

                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = child.bottom + params.bottomMargin
                val bottom = top + mDivider.intrinsicHeight

                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
        }
    }
}
