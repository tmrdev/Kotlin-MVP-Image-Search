package org.timreynolds.imagesearch.gallery

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_photo_detail.*
import org.timreynolds.imagesearch.Injection
import org.timreynolds.imagesearch.R
import org.timreynolds.imagesearch.data.models.SearchResults
import org.timreynolds.imagesearch.util.logdebug


class GalleryActivity : AppCompatActivity() {

    private var galleryPresenter: GalleryPresenter? = null
    private val BACK_STACK_ROOT_TAG = "root_fragment"
    private var galleryFragment: GalleryFragment? = null
    var selectedPhotos : ArrayList<SearchResults.FlickrPhoto>? = null
    var shared: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        getSupportActionBar()?.setTitle("My Gallery");

        if (savedInstanceState == null) {
            /*
             * Photos will be added to a personal gallery from searched Flickr images
             * So adapter will need to be reloaded with the relevant data selected by user from search results
             */

            // var galleryFragment = SearchFragment.newInstance()
            galleryFragment = GalleryFragment.newInstance()
            changeFragment(galleryFragment!!, true)
            galleryPresenter = GalleryPresenter(galleryFragment!!, Injection.provideSchedulerProvider())
        } else {
            logdebug("****** savedInstance IS NOT NULL ***")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //return super.onOptionsItemSelected(item)
        onBackPressed();
        return true;
    }

    fun changeFragment(f: Fragment, cleanStack: Boolean = false) {

        val ft = supportFragmentManager.beginTransaction()

        if (cleanStack) {
            clearBackStack()
        }
        ft.replace(R.id.contentFrame, f)
        ft.commit()
    }

    fun clearBackStack() {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}
