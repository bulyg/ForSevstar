package ru.bulyg.forsevstar.di

import dagger.Component
import ru.bulyg.forsevstar.App
import ru.bulyg.forsevstar.di.module.ApiModule
import ru.bulyg.forsevstar.di.module.AppModule
import ru.bulyg.forsevstar.di.module.RepositoryModule
import ru.bulyg.forsevstar.mvp.presenter.MainPresenter
import ru.bulyg.forsevstar.ui.activity.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        AppModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(app: App)
    fun inject(activity: MainActivity)
    fun inject(presenter: MainPresenter)
}