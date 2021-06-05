package ru.bulyg.forsevstar.mvp.model.datasource

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.bulyg.forsevstar.mvp.model.response.ResponseWeather

interface ApiService {
    @GET("data/2.5/weather?")
    fun loadWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") metric: String,
        @Query("appid") apiKey: String
    ): Observable<ResponseWeather>
}