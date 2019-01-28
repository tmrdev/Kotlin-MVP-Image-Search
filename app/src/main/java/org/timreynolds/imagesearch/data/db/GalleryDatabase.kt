package org.timreynolds.imagesearch.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import okhttp3.internal.Internal.instance

/**
 * Database - note that context is needed GalleryDatabase
 */
@Database(entities = [(Gallery::class)],version = 1,exportSchema = false)
abstract class GalleryDatabase : RoomDatabase() {

    abstract fun daoGallery(): DaoGallery

    companion object {
        private var INSTANCE: GalleryDatabase? = null

        fun getInstance(context: Context): GalleryDatabase? {
            if (INSTANCE == null) {
                synchronized(GalleryDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, GalleryDatabase::class.java, "gallery.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

//    companion object {
//        private val DB_NAME = "gallery.db"
//        private lateinit var instance: GalleryDatabase
//
//        fun getInstance(context:Context):GalleryDatabase {
//            return instance
//        }
//
//        fun setInstance(context:Context):GalleryDatabase {
//            if (instance == null)
//            {
//                synchronized (GalleryDatabase::class.java) {
//                    if (instance == null)
//                    {
//                        instance = Room.databaseBuilder(context.getApplicationContext(),
//                                GalleryDatabase::class.java, DB_NAME).build()
//                    }
//                }
//            }
//            return instance
//        }
//    }

//    companion object {
//        private var INSTANCE: GalleryDatabase? = null
//        fun getDataBase(context: Context): GalleryDatabase {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(context, GalleryDatabase::class.java, "gallery-db").build()
//            }
//            return INSTANCE as GalleryDatabase
//        }
//    }


}