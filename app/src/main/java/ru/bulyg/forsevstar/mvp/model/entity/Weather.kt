package ru.bulyg.forsevstar.mvp.model.entity

import com.google.gson.annotations.Expose

data class Weather(
    @Expose
    val description: String,
    @Expose
    val icon: String
)