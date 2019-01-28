package org.timreynolds.imagesearch.data.source

import io.reactivex.Flowable
import org.timreynolds.imagesearch.data.models.SearchResults

interface PhotosDataSource {

    fun getFlickrPhotos(tagValue: String, apiKey: String): Flowable<SearchResults.FlickrPhotos>

}