package org.timreynolds.imagesearch.gallery

import android.content.Context
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.timreynolds.imagesearch.MyApplication

import org.timreynolds.imagesearch.data.db.Gallery
import org.timreynolds.imagesearch.data.db.GalleryDatabase
import org.timreynolds.imagesearch.util.TAG
import org.timreynolds.imagesearch.util.schedulers.BaseSchedulerProvider
import kotlin.coroutines.CoroutineContext

class GalleryPresenter : GalleryContract.Presenter {

    private val photosView: GalleryContract.View
    private val scheduler: BaseSchedulerProvider
    private var set: MutableSet<String>? = null

    constructor(photosView: GalleryContract.View, scheduler: BaseSchedulerProvider) {
        this.photosView = photosView
        this.scheduler = scheduler
        photosView.setPresenter(this)
    }

    private val disposable = CompositeDisposable()

    override fun subscribe() {
        loadSelectedImagesFromDB()
    }

//    override fun subscribe(context: Context) {
//        loadSelectedImagesFromDB(context)
//    }

    override fun unsubscribe() {
        Log.i("gallery-presenter", " ** unsubscribe hit - disposable.clear **")
        disposable.clear()
    }

    private fun processSelectedResults(results: List<Gallery>) {
        for(dbItem in results) {
            // Tested and works (JSON Returned Properly for id and owner)
            Log.i(TAG, "*** loaded from db flickr photo id -> " + dbItem.id)
            Log.i(TAG, "*** loaded from db photo owner -> " + dbItem.owner)
        }
        Log.i(TAG, "** load from db hit : show saved db results total??? -> " + results.size)
        photosView.showSelectedPhotos(results)
    }

    override fun loadSelectedImagesFromDB() {
        Log.i(TAG, "** loadSelectedImagesFromDB top hit **")
        // TODO: Setup as subscribe and unsubscribe
        // For now letting Application class handle Room Context instance
        var getDBAllSubscription = MyApplication.getDatabase()!!.daoGallery()
                .getAllPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    Log.i(TAG, " it -> " + it.message + " : " + it.localizedMessage + " : " + it.cause
                    + " : " + it.printStackTrace())
        }.subscribe (
                this::processSelectedResults
        )
        { this.processError() }
        disposable.add(getDBAllSubscription)
    }

    private fun processError() {
        photosView.showLoadingPhotosError()
    }
}