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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun SetScreen(viewModel: MainViewModel) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            Text("設定", fontSize = 40.sp)
            Spacer(Modifier.weight(.5f))
            Button(
                modifier = Modifier.fillMaxWidth(.7f),
                onClick = {}
            ) {
                Text("中文")
            }
            Button(
                modifier = Modifier.fillMaxWidth(.7f),
                onClick = {}
            ) {
                Text("英文")
            }
            Spacer(Modifier.weight(1f))
        }
    }
}