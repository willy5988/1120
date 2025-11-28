package com.example.a1120

import android.content.Context
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

object Parse {
    fun cityList(context: Context, textName: String) = buildList {
        val document = context.assets.open(textName).use {
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
        }

        val elements = document.getElementsByTagName("city")
        for (i in 0 until elements.length) {
            val element = elements.item(i) as Element
            val type = element.getAttribute("type")
            val name = element.getElementsByTagName("name").item(0).textContent
            val nameEn = element.getElementsByTagName("name_en").item(0).textContent
            val fileName = element.getElementsByTagName("file_name").item(0).textContent
            add(City(type, name, nameEn, fileName))
        }
    }

    fun weatherData(context: Context, textName: String): WeatherData {
        val document = context.assets.open(textName).use {
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
        }

        // currentWeather
        val currentElements = document.getElementsByTagName("current_weather").item(0) as Element
        val city = currentElements.getElementsByTagName("city").item(0).textContent
        val latitude = currentElements.getElementsByTagName("latitude").item(0).textContent
        val longitude = currentElements.getElementsByTagName("longitude").item(0).textContent
        val currentWeather = CurrentWeather(city, latitude, longitude)

        //hourlyForecast
        val hourlyElements = document.getElementsByTagName("hourly_forecast")
        val hourlyForecast = buildList<Hour> {
            val hourlyElement = hourlyElements.item(0) as Element
            val hourElements = hourlyElement.getElementsByTagName("hour")
            for (i in 0 until hourElements.length) {
                val hourElement = hourElements.item(i) as Element
                val time = hourElement.getElementsByTagName("time").item(0).textContent
                val weatherCondition =
                    hourElement.getElementsByTagName("weather_condition").item(0).textContent
                val temperature =
                    hourElement.getElementsByTagName("temperature").item(0).textContent
                add(Hour(time, weatherCondition, temperature))
            }

        }

        //tenDayForecast
        val tenDayElements = document.getElementsByTagName("ten_day_forecast")
        val tenDayForecast = buildList {
            val tenDayElement = tenDayElements.item(0) as Element
            val dayElements = tenDayElement.getElementsByTagName("day")
            for (i in 0 until dayElements.length) {
                val dayElement = dayElements.item(i) as Element
                val date = dayElement.getElementsByTagName("date").item(0).textContent
                val weatherCondition =
                    dayElement.getElementsByTagName("weather_condition").item(0).textContent
                val highTemperature =
                    dayElement.getElementsByTagName("high_temperature").item(0).textContent
                val lowTemperature =
                    dayElement.getElementsByTagName("low_temperature").item(0).textContent

                add(Day(date, weatherCondition, highTemperature, lowTemperature))
            }
        }

        //aqi
        val aqiElement = document.getElementsByTagName("air_quality_index").item(0) as Element
        val aqi = aqiElement.getElementsByTagName("current_aqi").item(0).textContent.toInt()


        return WeatherData(currentWeather, hourlyForecast, tenDayForecast, aqi)


    }


}