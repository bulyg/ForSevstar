package ru.bulyg.forsevstar

import android.app.Application
import ru.bulyg.forsevstar.di.AppComponent
import ru.bulyg.forsevstar.di.DaggerAppComponent
import ru.bulyg.forsevstar.di.module.AppModule

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}