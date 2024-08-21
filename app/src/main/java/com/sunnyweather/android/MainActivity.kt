package com.sunnyweather.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.liveData
import com.sunnyweather.android.R
//import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
