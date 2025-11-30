package com.example.a1120

import android.content.Context
import androidx.core.content.edit

object Prefs {
    fun rememberCity(context: Context, city: String) {
        val prefs = context.getSharedPreferences("cityList", Context.MODE_PRIVATE)
        prefs.edit {
            putString(city, "")
        }
    }

    fun takeCity(context: Context, city: String): Boolean {
        val prefs = context.getSharedPreferences("cityList", Context.MODE_PRIVATE)
        if (prefs.contains(city)) {
            return true
        }
        return false
    }

    fun rememberTemp(context: Context, nowTemp: Boolean) {
        val prefs = context.getSharedPreferences("temp", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("temp", nowTemp).apply()
    }

    fun takeTemp(context: Context): Boolean {
        val prefs = context.getSharedPreferences("temp", Context.MODE_PRIVATE)
        return prefs.getBoolean("temp", true) // 預設攝氏
    }

    fun rememberLanguage(context: Context, nowLanguage: Boolean) {
        val prefs = context.getSharedPreferences("language", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("language", nowLanguage).apply()
    }

    fun takeLanguage(context: Context): Boolean {
        val prefs = context.getSharedPreferences("language", Context.MODE_PRIVATE)
        return prefs.getBoolean("language", true)//Chinese  false = En
    }


}