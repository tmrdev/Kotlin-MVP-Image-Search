package org.timreynolds.imagesearch.data.db

import android.arch.persistence.room.*
import io.reactivex.Flowable

/**
 * DaoGallery
 */
@Dao
interface DaoGallery {

    @Query("select * from photos")
    fun getAllPhotos():Flowable<List<Gallery>>

    @Query("select * from photos where photoId in (:id)")
    fun getPhotoById(id: Int):Gallery

    @Query("delete from photos")
    fun deleteAllPhotos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(gallery: Gallery)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPhotos(gallery: List<Gallery>)

    @Update
    fun updateGallery(gallery: Gallery)

    @Delete
    fun deleteGallery(gallery: Gallery)

}