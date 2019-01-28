package org.timreynolds.imagesearch.photodetail

import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import org.timreynolds.imagesearch.data.db.Gallery

class PhotoDetailPresenter : PhotoDetailContract.Presenter {

    private val photoDetailView: PhotoDetailContract.View

    private var flickrPhoto: Gallery? = null

    private val disposable = CompositeDisposable()

    constructor(flickrPhoto: Gallery, photoDetailView: PhotoDetailContract.View) {
        this.flickrPhoto = flickrPhoto
        this.photoDetailView = photoDetailView
        this.photoDetailView.setPresenter(this)
    }

    override fun subscribe() {
        showPhoto()
    }

    override fun unsubscribe() {
        Log.i("gallery-presenter", " ** unsubscribe hit - disposable.clear **")
        disposable.clear()
    }

    override fun showPhoto() {
        // show metadata
        this.flickrPhoto?.let { photoDetailView.showMetadata(it) }
        // Only set if non null with let
        // http://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
        var staticUrl: String?
        // Create static url  and center crop larger image
        staticUrl = "https://farm" + flickrPhoto?.farm + ".staticflickr.com/"+
                flickrPhoto?.server + "/" + flickrPhoto?.flickrId + "_" + flickrPhoto?.secret + ".jpg"

        staticUrl.let { photoDetailView.showPhoto(it) }
    }

}