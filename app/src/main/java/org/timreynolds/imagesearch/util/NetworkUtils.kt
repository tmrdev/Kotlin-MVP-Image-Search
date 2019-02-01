package org.timreynolds.imagesearch.util

import android.content.Context

/**
 * NetworkUtils
 */
class NetworkUtils {

    companion object {

        /**
         * Extension method to get connectivityManager for Context.
         */

        fun isNetworkConnected(con : Context): Boolean {
            return con.connectivityManager?.activeNetworkInfo != null
        }
    }

}