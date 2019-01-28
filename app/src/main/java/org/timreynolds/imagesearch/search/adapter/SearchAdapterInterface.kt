package org.timreynolds.imagesearch.search.adapter

import org.timreynolds.imagesearch.data.models.SearchResults

/**
 *
 */
interface SearchAdapterInterface {

    fun searchAction(size: Int, actionType: String, selectedImagesList: ArrayList<SearchResults.FlickrPhoto>?)

}