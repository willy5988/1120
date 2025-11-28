package com.example.a1120

fun String.weekEnToCh() = when (this) {
    "SATURDAY" -> "星期六"
    "SUNDAY" -> "星期日"
    "FRIDAY" -> "星期五"
    "THURSDAY" -> "星其四"
    "WEDNESDAY" -> "星期三"
    "TUESDAY" -> "星期二"
    "MONDAY" -> "星期一"
    else -> "星期一"

}

fun String.weatherToImage() = when (this) {
    "cloudy" -> R.drawable.cloudy
    "sunny" -> R.drawable.sunny
    "rain" -> R.drawable.rain
    "thunder" -> R.drawable.thunder
    "overcast" -> R.drawable.overcast
    else -> R.drawable.sunny
}