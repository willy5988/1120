package com.example.a1120

data class City(
    val type:String?,
    val name:String,
    val nameEn:String,
    val fileName:String,
)

data class WeatherData(
    val currentWeather: CurrentWeather,
    val hourlyForecast:List<Hour>,
    val tenDayForecast:List<Day>,
    val aqi:Int,
)

data class CurrentWeather(
    val city:String,
    val latitude:String,
    val longitude:String,
)

data class Hour(
    val time:String,
    val weather:String,
    val temperature:String,
)

data class Day(
    val date:String,
    val weatherCondition:String,
    val highTemperature:String,
    val lowTemperature:String,
)