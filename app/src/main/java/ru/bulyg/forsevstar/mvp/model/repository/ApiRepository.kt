package ru.bulyg.forsevstar.mvp.model.repository

import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.schedulers.Schedulers
import ru.bulyg.forsevstar.mvp.model.datasource.ApiService
import ru.bulyg.forsevstar.mvp.model.response.ResponseWeather
import ru.bulyg.forsevstar.status.NetworkStatus
import ru.bulyg.forsevstar.utils.exception.NoInternetException
import java.util.concurrent.TimeUnit

class ApiRepository(val api: ApiService, val networkStatus: NetworkStatus) {
    fun loadWeather(
        lat: String,
        lon: String,
        metric: String,
        apiKey: String
    ): Observable<ResponseWeather> =
        networkStatus.isOnline().flatMap { isOnline ->
            if (isOnline) {
                Observable
                    .interval(0, 5000, TimeUnit.MILLISECONDS)
                    .switchMap {
                        api.loadWeather(lat, lon, metric, apiKey).switchMap {
                            just(it)
                        }
                    }
            } else Observable.error(NoInternetException())
        }.subscribeOn(Schedulers.io())
}