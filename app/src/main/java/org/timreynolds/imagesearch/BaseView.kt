package org.timreynolds.imagesearch

import android.support.annotation.StringRes

interface BaseView<T> {

    fun setPresenter(presenter: T)

    fun isNetworkConnected() : Boolean

    fun onError(@StringRes resId : Int)

}