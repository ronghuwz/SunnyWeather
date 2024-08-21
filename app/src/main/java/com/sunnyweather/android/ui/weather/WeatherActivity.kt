package com.sunnyweather.android.ui.weather

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ActivityWeatherBinding
import com.sunnyweather.android.databinding.ForecastBinding
import com.sunnyweather.android.databinding.LifeIndexBinding
import com.sunnyweather.android.databinding.NowBinding
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    lateinit var binding: ActivityWeatherBinding
    private lateinit var nowBinding: NowBinding
    private lateinit var forecastBinding: ForecastBinding
    private lateinit var lifeIndexBinding: LifeIndexBinding

    private lateinit var forecastAdapter: ForecastAdapter

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind the included layouts
        nowBinding = NowBinding.bind(binding.root.findViewById(R.id.nowLayout))
        forecastBinding = ForecastBinding.bind(binding.root.findViewById(R.id.forecastLayout))
        lifeIndexBinding = LifeIndexBinding.bind(binding.root.findViewById(R.id.lifeIndexLayout))

        // Setup forecast RecyclerView
        forecastBinding.forecastRecyclerView.layoutManager = LinearLayoutManager(this)

        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefresh.isRefreshing = false
        })

        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        nowBinding.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        nowBinding.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        // Fill in the now.xml layout data
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        nowBinding.currentTemp.text = currentTempText
        nowBinding.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        nowBinding.currentAQI.text = currentPM25Text
        nowBinding.root.setBackgroundResource(getSky(realtime.skycon).bg)

        // Fill in the forecast.xml layout data
        forecastAdapter = ForecastAdapter(daily.skycon, daily.temperature)
        forecastBinding.forecastRecyclerView.adapter = forecastAdapter

        // Fill in the life_index.xml layout data
        val lifeIndex = daily.lifeIndex
        lifeIndexBinding.coldRiskText.text = lifeIndex.coldRisk[0].desc
        lifeIndexBinding.dressingText.text = lifeIndex.dressing[0].desc
        lifeIndexBinding.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        lifeIndexBinding.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility = View.VISIBLE
    }
}




//package com.sunnyweather.android.ui.weather
//
//import android.content.Context
//import android.graphics.Color
//import android.os.Build
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.inputmethod.InputMethodManager
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.GravityCompat
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import com.sunnyweather.android.R
//import com.sunnyweather.android.databinding.ActivityWeatherBinding
//import com.sunnyweather.android.databinding.ForecastBinding
//import com.sunnyweather.android.databinding.LifeIndexBinding
//import com.sunnyweather.android.databinding.NowBinding
//import com.sunnyweather.android.logic.model.Weather
//import com.sunnyweather.android.logic.model.getSky
//import java.text.SimpleDateFormat
//import java.util.*
//
//class WeatherActivity : AppCompatActivity() {
//
//    lateinit var binding: ActivityWeatherBinding
//    private lateinit var nowBinding: NowBinding
//    private lateinit var forecastBinding: ForecastBinding
//    private lateinit var lifeIndexBinding: LifeIndexBinding
//
//    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        if (Build.VERSION.SDK_INT >= 21) {
//            val decorView = window.decorView
//            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            window.statusBarColor = Color.TRANSPARENT
//        }
//        binding = ActivityWeatherBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Bind the included layouts
//        nowBinding = NowBinding.bind(binding.root.findViewById(R.id.nowLayout))
//        forecastBinding = ForecastBinding.bind(binding.root.findViewById(R.id.forecastLayout))
//        lifeIndexBinding = LifeIndexBinding.bind(binding.root.findViewById(R.id.lifeIndexLayout))
//
//        if (viewModel.locationLng.isEmpty()) {
//            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
//        }
//        if (viewModel.locationLat.isEmpty()) {
//            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
//        }
//        if (viewModel.placeName.isEmpty()) {
//            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
//        }
//
//        viewModel.weatherLiveData.observe(this, Observer { result ->
//            val weather = result.getOrNull()
//            if (weather != null) {
//                showWeatherInfo(weather)
//            } else {
//                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
//                result.exceptionOrNull()?.printStackTrace()
//            }
//            binding.swipeRefresh.isRefreshing = false
//        })
//
//        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
//        refreshWeather()
//        binding.swipeRefresh.setOnRefreshListener {
//            refreshWeather()
//        }
//        nowBinding.navBtn.setOnClickListener {
//            binding.drawerLayout.openDrawer(GravityCompat.START)
//        }
//        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
//            override fun onDrawerStateChanged(newState: Int) {}
//
//            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
//
//            override fun onDrawerOpened(drawerView: View) {}
//
//            override fun onDrawerClosed(drawerView: View) {
//                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
//            }
//        })
//    }
//
//    fun refreshWeather() {
//        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
//        binding.swipeRefresh.isRefreshing = true
//    }
//
//    private fun showWeatherInfo(weather: Weather) {
//        nowBinding.placeName.text = viewModel.placeName
//        val realtime = weather.realtime
//        val daily = weather.daily
//
//        // Fill in the now.xml layout data
//        val currentTempText = "${realtime.temperature.toInt()} ℃"
//        nowBinding.currentTemp.text = currentTempText
//        nowBinding.currentSky.text = getSky(realtime.skycon).info
//        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
//        nowBinding.currentAQI.text = currentPM25Text
//        nowBinding.root.setBackgroundResource(getSky(realtime.skycon).bg)
//
//        // Fill in the forecast.xml layout data
//        forecastBinding.forecastLayout.removeAllViews()
//        val days = daily.skycon.size
//        for (i in 0 until days) {
//            val skycon = daily.skycon[i]
//            val temperature = daily.temperature[i]
//            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastBinding.forecastLayout, false)
//            val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
//            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
//            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
//            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
//            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            dateInfo.text = simpleDateFormat.format(skycon.date)
//            val sky = getSky(skycon.value)
//            skyIcon.setImageResource(sky.icon)
//            skyInfo.text = sky.info
//            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
//            temperatureInfo.text = tempText
//            forecastBinding.forecastLayout.addView(view)
//        }
//
//        // Fill in the life_index.xml layout data
//        val lifeIndex = daily.lifeIndex
//        lifeIndexBinding.coldRiskText.text = lifeIndex.coldRisk[0].desc
//        lifeIndexBinding.dressingText.text = lifeIndex.dressing[0].desc
//        lifeIndexBinding.ultravioletText.text = lifeIndex.ultraviolet[0].desc
//        lifeIndexBinding.carWashingText.text = lifeIndex.carWashing[0].desc
//        binding.weatherLayout.visibility = View.VISIBLE
//    }
//}
