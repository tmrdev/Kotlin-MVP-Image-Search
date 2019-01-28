package org.timreynolds.imagesearch.gallery

import org.timreynolds.imagesearch.BasePresenter
import org.timreynolds.imagesearch.BaseView
import org.timreynolds.imagesearch.data.models.SearchResults


interface SearchContract {

    interface Presenter : BasePresenter {

        fun loadFlickrPhotos(tagValue: String, apiKey: String)

        fun saveSelectedImagesToDB(photos: List<SearchResults.FlickrPhoto>)

    }

    interface View : BaseView<Presenter> {

        fun showFlickrPhotos(results: SearchResults.FlickrPhotos)

        fun showLoadingPhotosError()

        fun gotoGalleryActivity()

    }
}