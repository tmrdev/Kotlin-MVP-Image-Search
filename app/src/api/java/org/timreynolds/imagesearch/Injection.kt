package org.timreynolds.imagesearch

import org.timreynolds.imagesearch.data.source.PhotosRepository
import org.timreynolds.imagesearch.data.source.remote.PhotosRemoteDataSource
import org.timreynolds.imagesearch.util.schedulers.BaseSchedulerProvider
import org.timreynolds.imagesearch.util.schedulers.SchedulerProvider

object Injection {

    fun provideMessagesRepository(): PhotosRepository {
        // Get data from remote API / JSON
        return PhotosRepository.getInstance(PhotosRemoteDataSource.getInstance())
    }

    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider.getInstance()
    }
}