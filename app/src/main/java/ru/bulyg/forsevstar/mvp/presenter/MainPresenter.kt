package ru.bulyg.forsevstar.mvp.presenter

import android.annotation.SuppressLint
import android.graphics.Color
import com.github.mikephil.charting.data.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.bulyg.forsevstar.mvp.model.repository.ApiRepository
import ru.bulyg.forsevstar.mvp.model.response.ResponseWeather
import ru.bulyg.forsevstar.mvp.view.MainView
import ru.bulyg.forsevstar.utils.exception.NoInternetException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    @Inject
    lateinit var repository: ApiRepository

    var compositeDisposable = CompositeDisposable()

    var entries = mutableListOf<Entry>()
    var labels = mutableListOf<String>()


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun loadData(
        lat: String,
        lon: String,
        metric: String,
        apiKey: String
    ) {
        compositeDisposable.add(
            repository.loadWeather(lat, lon, metric, apiKey)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setViewState(it)
                }, {
                    if (it is NoInternetException) {
                        viewState.showAlertDialog()
                        viewState.setBtnTitle()
                        clear()
                    }
                })
        )
    }

    fun setViewState(response: ResponseWeather) {
        entries.add(Entry(response.main.temp.toFloat(), entries.size))
        labels.add(getCurrentDate())
        viewState.setTemp("${(response.main.temp).toInt()}°C")
        viewState.setIcon("http://openweathermap.org/img/wn/${response.weather[0].icon}@2x.png")
        viewState.setDescription(response.weather[0].description)
        viewState.setName(response.name)
        viewState.update()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("hh:mm:ss")
        return dateFormat.format(Date())
    }

    fun setupLineChart(): LineData {
        val lineDataSet = LineDataSet(entries, "temperature")
        lineDataSet.color = Color.GREEN
        return LineData(labels, lineDataSet)
    }

    override fun detachView(view: MainView?) {
        super.detachView(view)
        compositeDisposable.clear()
        viewState.setBtnTitle()
    }

    fun clear() {
        compositeDisposable.clear()
    }
}