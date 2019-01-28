package org.timreynolds.imagesearch.data.source.remote.api

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query
import org.timreynolds.imagesearch.data.models.SearchResults

interface FlickrAPI {
    // NOTE: extras bring in url_t (thumbnail) and url_l (large photo), using just for testing purposes
    @GET("?method=flickr.photos.search&nojsoncallback=1&per_page=50&format=json&extras=url_t%2Curl_l")
    fun searchPhotos(@Query("tags") tags: String,
                     @Query("api_key") api_key: String): Flowable<SearchResults.FlickrPhotos>

}