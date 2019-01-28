package org.timreynolds.imagesearch.photodetail

import org.timreynolds.imagesearch.BasePresenter
import org.timreynolds.imagesearch.BaseView
import org.timreynolds.imagesearch.data.db.Gallery

interface PhotoDetailContract {

    interface View : BaseView<Presenter> {

        fun showPhoto(url: String)

        fun showMetadata(photoData: Gallery)

        fun isActive(): Boolean

    }

    interface Presenter : BasePresenter {

        fun showPhoto()

    }
}