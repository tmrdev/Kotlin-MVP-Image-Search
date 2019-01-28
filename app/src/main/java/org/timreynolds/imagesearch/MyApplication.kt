package org.timreynolds.imagesearch

import android.app.Application
import android.arch.persistence.room.Room
import org.timreynolds.imagesearch.data.db.GalleryDatabase

/**
 * MyApplication
 */
class MyApplication : Application() {

    companion object {
        lateinit var db: GalleryDatabase
        fun getDatabase(): GalleryDatabase? {
            return db
        }
    }

    override fun onCreate() {
        super.onCreate()
        db = GalleryDatabase.getInstance(this)!!
    }

}