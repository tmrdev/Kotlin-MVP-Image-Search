package org.timreynolds.imagesearch.util.schedulers

import io.reactivex.Scheduler

/**
 * BaseSchedulerProvider
 */
interface BaseSchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler

}