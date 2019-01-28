package org.timreynolds.imagesearch.data.source

import io.reactivex.Flowable
import org.timreynolds.imagesearch.data.models.SearchResults

class PhotosRepository private constructor(private val remoteDataSource: PhotosDataSource) : PhotosDataSource {


    companion object {
        private var instance: PhotosRepository? = null

        fun getInstance(remoteDataSource: PhotosDataSource): PhotosRepository {
            if (instance == null) {
                instance = PhotosRepository(remoteDataSource)
            }
            return instance as PhotosRepository
        }

        fun destroyInstance() = {
            instance = null
        }
    }

    override fun getFlickrPhotos(tagValue: String, apiKey: String): Flowable<SearchResults.FlickrPhotos> {
        return remoteDataSource.getFlickrPhotos(tagValue, apiKey)
    }

}