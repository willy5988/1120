package com.example.a1120

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp

@Composable
fun SettingScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    var chinese by remember {
        mutableStateOf(
            if (Prefs.takeLanguage(context)) {
                "中文"
            } else {
                "chinese"
            }
        )
    }
    var english by remember {
        mutableStateOf(
            if (Prefs.takeLanguage(context)) {
                "英文"
            } else {
                "english"
            }
        )
    }

    var setting by remember {
        mutableStateOf(
            if (Prefs.takeLanguage(context)) {
                "設定"
            } else {
                "setting"
            }
        )
    }




    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            Text(setting, fontSize = 40.sp)
            Spacer(Modifier.weight(.5f))
            Button(
                modifier = Modifier.fillMaxWidth(.7f),
                onClick = {
                    Prefs.rememberLanguage(context, true)
                    chinese = "中文"
                    english = "英文"
                    setting = "中文"
                }
            ) {
                Text(chinese)
            }
            Button(
                modifier = Modifier.fillMaxWidth(.7f),
                onClick = {
                    Prefs.rememberLanguage(context, false)
                    chinese = "chinese"
                    english = "english"
                    setting = "setting"
                }
            ) {

                Text(english)

            }
            Spacer(Modifier.weight(1f))
        }
    }
}