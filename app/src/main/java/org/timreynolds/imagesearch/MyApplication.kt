package org.timreynolds.imagesearch

import android.app.Application
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
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

        // Picasso Network Offline Priority
        var okHttpClient = OkHttpClient.Builder()
                .build()
        Picasso.Builder(this)
                .downloader(OkHttp3Downloader(okHttpClient))
                .build()
    }

}