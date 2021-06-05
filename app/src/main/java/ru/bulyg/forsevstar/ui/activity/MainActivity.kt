package ru.bulyg.forsevstar.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_weather.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.bulyg.forsevstar.App
import ru.bulyg.forsevstar.BuildConfig
import ru.bulyg.forsevstar.R
import ru.bulyg.forsevstar.mvp.presenter.MainPresenter
import ru.bulyg.forsevstar.mvp.view.MainView
import ru.bulyg.forsevstar.utils.glide.loadImage

class MainActivity : MvpAppCompatActivity(), MainView {
    companion object {
        const val PERMISSION_ID = 9999
        //для запуска запихать api_key в файл local.properties
        private const val API_KEY = BuildConfig.API_KEY
    }

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = MainPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    lateinit var locationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private var lat = 0.0
    private var lon = 0.0
    private var metric = "metric"
    private var isClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.instance.appComponent.inject(this)
    }

    override fun init() {
        initLocation()
        initListener()
        initLineChart()
    }

    private fun initLocation() {
        getCoordinates()
        Toast.makeText(this, "Updating coordinates", Toast.LENGTH_LONG).show()
        btn_activity_main_start.isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({
            if (lat != 0.0) {
                btn_activity_main_start.isEnabled = true
                Toast.makeText(this, "Your coordinates have been updated", Toast.LENGTH_SHORT)
                    .show()
            } else init()
        }, 5000)
    }

    private fun initListener() {
        btn_activity_main_start.setOnClickListener {
            if (isClicked) {
                isClicked = false
                presenter.clear()
                btn_activity_main_start.text = getString(R.string.start)
            } else {
                isClicked = true
                btn_activity_main_start.text = getString(R.string.stop)
                presenter.loadData(
                    String.format("%.2f", lat),
                    String.format("%.2f", lon),
                    metric,
                    API_KEY
                )
            }
        }
    }

    private fun initLineChart() {
        with(line_chart_activity_main) {
            data = presenter.setupLineChart()
            data.setDrawValues(false)
            animateY(0)
            setDescription("")
        }
    }

    override fun setTemp(temp: String) {
        tv_item_weather_temp.text = temp
    }

    override fun setIcon(url: String) {
        loadImage(url, iv_item_weather)
    }

    override fun setDescription(desc: String) {
        tv_item_weather_desc.text = desc
    }

    override fun showAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.error)
            .setMessage(R.string.msg)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
            }.show()
    }

    override fun update() {
        initLineChart()
    }

    override fun setBtnTitle() {
        btn_activity_main_start.text = getString(R.string.start)
        isClicked = false
    }

    private fun getCoordinates() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation: Location = locationResult.lastLocation
                lat = lastLocation.latitude
                lon = lastLocation.longitude
            }
        }
        setRequest()
        checkPermission()
        locationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private fun setRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 10
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
            return
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }
}