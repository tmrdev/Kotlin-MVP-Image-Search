package org.timreynolds.imagesearch




object Injection {

    fun provideMessagesRepository(): PhotosRepository {
        return PhotosRepository.getInstance(FakePhotosRemoteDataSource.getInstance())
    }

    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider.getInstance()
    }
}