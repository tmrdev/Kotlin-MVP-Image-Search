package org.timreynolds.imagesearch.data.source.remote

import android.util.Log
import io.reactivex.Flowable
import org.timreynolds.imagesearch.data.models.SearchResults
import org.timreynolds.imagesearch.data.source.PhotosDataSource
import org.timreynolds.imagesearch.data.source.remote.api.FlickrService
import org.timreynolds.imagesearch.util.TAG

class PhotosRemoteDataSource : PhotosDataSource {

    companion object {
        private var INSTANCE: PhotosRemoteDataSource? = null

        fun getInstance(): PhotosRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = PhotosRemoteDataSource()
            }
            return INSTANCE as PhotosRemoteDataSource
        }
    }
    // Should not need to return list here if main photos JSON data class is being returned
    // photo List is part of photos JSON in Flickr API
    override fun getFlickrPhotos(tagValue: String, apiKey: String): Flowable<SearchResults.FlickrPhotos> {
        Log.i(TAG, "** photos remote data source api key -> " + apiKey)
        return FlickrService.getClient().searchPhotos(tagValue, apiKey)
    }
}