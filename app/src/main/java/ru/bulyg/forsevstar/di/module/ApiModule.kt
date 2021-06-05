package ru.bulyg.forsevstar.di.module

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import ru.bulyg.forsevstar.App
import ru.bulyg.forsevstar.mvp.model.datasource.ApiService
import ru.bulyg.forsevstar.status.AndroidNetworkStatus
import ru.bulyg.forsevstar.status.NetworkStatus
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {
    @Named("baseUrl")
    @Provides
    fun baseUrl(): String {
        return "https://api.openweathermap.org/"
    }

    @Singleton
    @Provides
    fun api(@Named("baseUrl") baseUrl: String, gson: Gson): ApiService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun gson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Provides
    fun networkStatus(app: App): NetworkStatus {
        return AndroidNetworkStatus(app)
    }
}