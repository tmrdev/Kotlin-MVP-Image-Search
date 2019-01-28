package org.timreynolds.imagesearch.gallery

import android.content.Context

import org.timreynolds.imagesearch.BasePresenter
import org.timreynolds.imagesearch.BaseView
import org.timreynolds.imagesearch.data.db.Gallery

interface GalleryContract {

    interface Presenter : BasePresenter {

        // fun loadSelectedImagesFromDB(context: Context)
        fun loadSelectedImagesFromDB()

        // fun subscribe(context: Context)
    }

    interface View : BaseView<Presenter> {

        fun showSelectedPhotos(selectedPhotos: List<Gallery>)

        fun showLoadingPhotosError()

        fun isActive(): Boolean

    }
}