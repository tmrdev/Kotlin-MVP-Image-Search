package org.timreynolds.imagesearch.data

import io.reactivex.Flowable


class FakePhotosRemoteDataSource private constructor() : PhotosDataSource {


    companion object {

        private var INSTANCE: FakePhotosRemoteDataSource? = null

        fun getInstance(): FakePhotosRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = FakePhotosRemoteDataSource()
            }
            return INSTANCE as FakePhotosRemoteDataSource
        }
    }

    override fun getFlickrPhotos(tagValue: String, apiKey: String): Flowable<SearchResults.FlickrPhotos> {
        return Flowable.just(getMockPhotos())
    }


    private fun getMockPhotos(): SearchResults.FlickrPhoto {

        val flickrPhotos: SearchResults.FlickrPhotos
        val flickrPhoto = SearchResults.FlickrPhoto(
                id = 1,
                farm = 5,
                server = "4908",
                secret = "4eebe306bc",
                owner = "22607249@N07",
                title = "test")


        return flickrPhoto
    }
}