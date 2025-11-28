package com.example.a1120

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import com.example.a1120.ui.theme._1120Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<MainViewModel>()
        setContent {
            _1120Theme {
                Crossfade(
                    viewModel.screen, label = ""
                ) {
                    it?.invoke() ?: finish()
                }
                BackHandler {
                    viewModel.pop()
                }
            }
        }
    }
}
