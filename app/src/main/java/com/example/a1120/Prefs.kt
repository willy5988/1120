package com.example.a1120

import android.content.Context
import androidx.core.content.edit

object Prefs {
    fun rememberCity(context: Context, city: String, cityFileName: String) {
        val prefs = context.getSharedPreferences("cityList", Context.MODE_PRIVATE)
        prefs.edit {
            putString(city, cityFileName)
        }
    }

    fun takeCity(context: Context, city: String): String {
        val prefs = context.getSharedPreferences("cityList", Context.MODE_PRIVATE)
        if (prefs.contains(city)) {
            return prefs.getString(city, null).toString()
        }
        return ""
    }
}