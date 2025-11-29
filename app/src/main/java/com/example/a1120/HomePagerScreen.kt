package com.example.a1120

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePageScreen(viewModel: MainViewModel, takeCityList: List<City>?) {

    val context = LocalContext.current
    val cityListXml = remember { Parse.cityList(context, "city_list.xml") }
    val nowCity by remember { mutableStateOf(cityListXml.first { it.type == "current" }) }
    val cityList = remember { takeCityList?.toMutableStateList() ?: mutableStateListOf(nowCity) }
    val pagerState = rememberPagerState(pageCount = { cityList.size })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // 1. 使用 BottomAppBar，並將背景設為白色
            BottomAppBar(
                modifier = Modifier.height(80.dp),
                containerColor = Color.White,
                contentColor = Color.Black // 圖案設為黑色
            ) {
                // 2. 建立一個橫排 (Row)，並告訴它「左右撐開」(SpaceBetween)
                Row(
                    modifier = Modifier.fillMaxWidth(), // 寬度佔滿
                    horizontalArrangement = Arrangement.SpaceBetween, // 關鍵：左右推開
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // --- 左邊：地圖按鈕 ---
                    IconButton(onClick = { /* 點擊動作 */ }) {
                        Icon(Icons.Default.Place, null)
                    }

                    Row {
                        Icon(
                            painter = painterResource(R.drawable.baseline_location_on_24),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        for (i in 2..cityList.size.also { println(it) }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_circle_24),
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = Color.LightGray
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                    }

                    // --- 右邊：清單按鈕 ---
                    IconButton(onClick = { viewModel.push { CityListScreen(viewModel) } }) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "清單")
                    }
                }
            }
        }

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            HorizontalPager(pagerState) { nowPager: Int ->
                HomeScreen(
                    viewModel,
                    cityList[nowPager],
                    if (nowPager == 0) {
                        true
                    } else {
                        false
                    }
                )

            }

        }
    }


}