package org.timreynolds.imagesearch.data.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

object SearchResults {
        data class FlickrPhotos(
                @Json(name = "photos")
                var photos: Photos,

                @Json(name = "total")
                var total: Long,

                @Json(name = "pages")
                var pages: Long
        ) : Serializable

        data class Photos(
                @Json(name = "total")
                var total: Long,

                @Json(name= "photo")
                var photo: List<FlickrPhoto>
        ) : Serializable

        @Parcelize
        data class FlickrPhoto(
                @Json(name = "id")
                var id: Long,

                @Json(name = "farm")
                var farm: Int,

                @Json(name = "server")
                var server: String,

                @Json(name = "secret")
                var secret: String,

                @Json(name= "owner")
                var owner: String,

                @Json(name = "title")
                var title: String

//                @Json(name = "url_t")
//                var url_t: String,

                // url_l sometimes does not appear in json
                // creates crash when passing as Pacelable
//                @Json(name = "url_l")
//                var url_l: String,

        ) : Parcelable
}



