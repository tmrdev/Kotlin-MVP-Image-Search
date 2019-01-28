package org.timreynolds.imagesearch.photodetail

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_photo_detail.*
import org.timreynolds.imagesearch.R
import org.timreynolds.imagesearch.data.db.Gallery
import org.timreynolds.imagesearch.search.SearchActivity

const val INTENT_PHOTO = "intent_photo"

class PhotoDetailActivity : AppCompatActivity() {

    private var photoDetailPresenter: PhotoDetailPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        enableHomeAsUp { onBackPressed() }
        setSupportActionBar(toolbar)

        Log.i("photodetail", "onCreate hit");

        if (savedInstanceState == null) {
            val photo = intent.getParcelableExtra(INTENT_PHOTO) as Gallery

            var photoDetailFragment = PhotoDetailFragment.newInstance()
            changeFragment(photoDetailFragment, true)
            photoDetailPresenter = PhotoDetailPresenter(photo, photoDetailFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        toolbar.inflateMenu(R.menu.menu_photo_detail)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_search -> {
            val intent = Intent(this, SearchActivity::class.java)
            ContextCompat.startActivity(this, intent, null)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun enableHomeAsUp(up: () -> Unit) {
        toolbar.navigationIcon = createUpDrawable()
        toolbar.setNavigationOnClickListener { up() }
    }

    private fun createUpDrawable() = DrawerArrowDrawable(toolbar.context).apply { progress = 1f }

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

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp();
        onBackPressed()
        return true
    }
}
