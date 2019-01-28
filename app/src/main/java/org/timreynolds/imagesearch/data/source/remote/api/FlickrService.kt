package org.timreynolds.imagesearch.data.source.remote.api

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_FLICKR_URL = "https://api.flickr.com/services/rest/"
const val FLICKR_API_KEY = "add-flickr-api-key-here";

class FlickrService private constructor() {

    init {

    }

    companion object {

        private lateinit var retrofit: Retrofit

        val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            // this.level = HttpLoggingInterceptor.Level.BODY
            this.level = HttpLoggingInterceptor.Level.BASIC
        }

        val client : OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        fun getClient(): FlickrAPI {
            val moshi = Moshi.Builder().build()
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_FLICKR_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()

            return retrofit.create(FlickrAPI::class.java)
        }
    }
}