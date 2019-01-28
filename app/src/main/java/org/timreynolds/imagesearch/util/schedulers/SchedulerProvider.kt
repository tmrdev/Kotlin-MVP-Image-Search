package org.timreynolds.imagesearch.util.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * SchedulerProvider
 */
class SchedulerProvider private constructor(): BaseSchedulerProvider {

    companion object {
        private var INSTANCE: SchedulerProvider? = null

        fun getInstance(): SchedulerProvider {
            if (INSTANCE == null) {
                INSTANCE = SchedulerProvider()
            }
            return INSTANCE as SchedulerProvider
        }
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}