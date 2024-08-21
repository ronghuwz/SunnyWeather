package com.sunnyweather.android.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(private val skyconList: List<DailyResponse.Skycon>, private val temperatureList: List<DailyResponse.Temperature>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateInfo: TextView = view.findViewById(R.id.dateInfo)
        val skyIcon: ImageView = view.findViewById(R.id.skyIcon)
        val skyInfo: TextView = view.findViewById(R.id.skyInfo)
        val temperatureInfo: TextView = view.findViewById(R.id.temperatureInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skycon = skyconList[position]
        val temperature = temperatureList[position]
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        holder.dateInfo.text = simpleDateFormat.format(skycon.date)
        val sky = getSky(skycon.value)
        holder.skyIcon.setImageResource(sky.icon)
        holder.skyInfo.text = sky.info
        val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
        holder.temperatureInfo.text = tempText
    }

    override fun getItemCount() = skyconList.size
}
