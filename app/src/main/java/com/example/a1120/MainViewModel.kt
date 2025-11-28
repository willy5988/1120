package com.example.a1120

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val screens = mutableStateListOf<@Composable () -> Unit>({ HomePageScreen(this, null) })
    val screen get() = screens.lastOrNull()

    fun push(screen: @Composable () -> Unit) {
        screens += screen
    }

    fun pop() {
        screens.removeLastOrNull()
    }
}