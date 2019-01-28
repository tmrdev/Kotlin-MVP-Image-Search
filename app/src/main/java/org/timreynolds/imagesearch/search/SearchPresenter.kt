package org.timreynolds.imagesearch.gallery

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.timreynolds.imagesearch.MyApplication
import org.timreynolds.imagesearch.data.db.Gallery
import org.timreynolds.imagesearch.data.db.GalleryDatabase

import org.timreynolds.imagesearch.data.models.SearchResults
import org.timreynolds.imagesearch.data.source.PhotosRepository
import org.timreynolds.imagesearch.data.source.remote.api.FLICKR_API_KEY
import org.timreynolds.imagesearch.util.TAG
import org.timreynolds.imagesearch.util.logdebug
import org.timreynolds.imagesearch.util.schedulers.BaseSchedulerProvider

class SearchPresenter : SearchContract.Presenter {

    private val photosRepository: PhotosRepository
    private val photosView: SearchContract.View
    private val scheduler: BaseSchedulerProvider
    private var tagValue: String

    constructor(tagValue: String, photosRepository: PhotosRepository, photosView: SearchContract.View, scheduler: BaseSchedulerProvider) {
        this.tagValue = tagValue
        this.photosRepository = photosRepository
        this.photosView = photosView
        this.scheduler = scheduler
        photosView.setPresenter(this)
    }

    private val disposable = CompositeDisposable()

    override fun subscribe() {
        // subscribe to Flickr image search and load
        loadFlickrPhotos(this.tagValue, FLICKR_API_KEY )
    }

    override fun unsubscribe() {
        Log.i("gallery-presenter", " ** unsubscribe hit - disposable.clear **")
        disposable.clear()
    }

    /**
     * loadFlickrPhotos - retrieves image search results based on user search
     */
    override fun loadFlickrPhotos(tagValue: String, apiKey: String) {
        this.tagValue = tagValue
        Log.i("gallery-presenter", " ** load Flickr Photos top hit **")
        if(this.tagValue.length > 0) {
            disposable.clear()
            val subscription = photosRepository
                    .getFlickrPhotos(tagValue, apiKey)
                    .subscribeOn(scheduler.computation())
                    .observeOn(scheduler.ui())
                    .cache()
                    .doOnError {
                        Log.i(TAG, " error: loadFlickrPhotos -> " + it.message + " : " +  it.localizedMessage + " : " + it.cause)
                    }
                    .subscribe(
                            this::processFlickrPhotos
                    )
                    {
                        Log.i(TAG, "rxjava process error -> " + it.localizedMessage + " : " + it.message)
                        this.processError()

                    }
            disposable.add(subscription)
        } else {
            Log.i(TAG, "** error: tagValue is NULL")
        }
    }

    private fun processFlickrPhotos(results: SearchResults.FlickrPhotos) {
        Log.i(TAG, "** processFlickrPhotos hit : show flickr results")
        photosView.showFlickrPhotos(results)
    }

    private fun processError() {
        Log.i(TAG, "** processError hit ")
        photosView.showLoadingPhotosError()
    }

    /**
     * saveSelectedImagesToDB - process all images selected by user and create an insertAll save to DB
     */
    override fun saveSelectedImagesToDB(photos: List<SearchResults.FlickrPhoto>) {
        val myInserts: MutableList<Gallery> = ArrayList()
        for(item in (photos as List<SearchResults.FlickrPhoto>?)!!) {

            val insertPhotoData = Gallery(0, item.id, item.farm, item.server, item.secret, item.owner, item.title)
            myInserts.add(insertPhotoData)

        }

        val insertStatus = Observable.fromCallable { MyApplication.getDatabase()?.daoGallery()?.insertAllPhotos(myInserts) }
                .subscribeOn(Schedulers.io())
                .doOnError {
                    Log.i(TAG, " error: saveSelectedImagesToDB -> " + it.message + " : " +  it.localizedMessage + " : " + it.cause)
                }
                .subscribe {
                    Log.i(TAG, "Inserted ${photos.size} photos from API into DB...")
                    photosView.gotoGalleryActivity()
                }
        Log.i(TAG,"insert status = > " + insertStatus)
        disposable.add(insertStatus)
    }
}