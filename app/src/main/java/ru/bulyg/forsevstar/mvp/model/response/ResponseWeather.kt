package ru.bulyg.forsevstar.mvp.model.response

import com.google.gson.annotations.Expose
import ru.bulyg.forsevstar.mvp.model.entity.Main
import ru.bulyg.forsevstar.mvp.model.entity.Weather

data class ResponseWeather(
    @Expose
    val weather: List<Weather>,
    @Expose
    val main: Main,
    @Expose
    val name: String
)