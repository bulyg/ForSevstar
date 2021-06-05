package ru.bulyg.forsevstar.di.module

import dagger.Module
import dagger.Provides
import ru.bulyg.forsevstar.mvp.model.datasource.ApiService
import ru.bulyg.forsevstar.mvp.model.repository.ApiRepository
import ru.bulyg.forsevstar.status.NetworkStatus
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideApiRepository(api: ApiService, networkStatus: NetworkStatus): ApiRepository {
        return ApiRepository(api, networkStatus)
    }
}