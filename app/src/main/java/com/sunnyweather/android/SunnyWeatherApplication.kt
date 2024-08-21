package com.sunnyweather.android

import android.annotation.SuppressLint
import android.content.Context
import android.app.Application


class SunnyWeatherApplication : Application() {

    companion object {

        const val TOKEN = "yqKLOhaTaukyA482" // 填入你申请到的令牌值

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}