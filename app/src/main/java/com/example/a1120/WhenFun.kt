package com.example.a1120

object WhenFun {
    fun weekEnToCh(week: String) = when (week) {
        "SATURDAY" -> "星期六"
        "SUNDAY" -> "星期日"
        "FRIDAY" -> "星期五"
        "THURSDAY" -> "星其四"
        "WEDNESDAY" -> "星期三"
        "TUESDAY" -> "星期二"
        "MONDAY" -> "星期一"
        else -> "星期一"

    }

    fun weatherToImage(a: String) = when (a) {
        "cloudy" -> R.drawable.cloudy
        "sunny" -> R.drawable.sunny
        "rain" -> R.drawable.rain
        "thunder" -> R.drawable.thunder
        "overcast" -> R.drawable.overcast
        else -> R.drawable.sunny
    }
}

fun String.weatherToImage() = when (this) {
    "cloudy" -> R.drawable.cloudy
    "sunny" -> R.drawable.sunny
    "rain" -> R.drawable.rain
    "thunder" -> R.drawable.thunder
    "overcast" -> R.drawable.overcast
    else -> R.drawable.sunny
}