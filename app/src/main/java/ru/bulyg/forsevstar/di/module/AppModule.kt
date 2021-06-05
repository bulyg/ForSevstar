package ru.bulyg.forsevstar.di.module

import dagger.Module
import dagger.Provides
import ru.bulyg.forsevstar.App

@Module
class AppModule(val app: App) {
    @Provides
    fun app(): App {
        return app
    }
}