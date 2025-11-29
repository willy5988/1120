package com.example.a1120

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(viewModel: MainViewModel) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var searchCity by remember { mutableStateOf("") }

    var temp by remember { mutableStateOf(true) }
    val cityListXml = remember { Parse.cityList(context, "city_list.xml") }
    val nowCity by remember { mutableStateOf(cityListXml.first { it.type == "current" }) }
    val cityList =
        remember { mutableStateListOf(nowCity, City(null, "台北市", "Taipei City", "taipei.xml")) }


    var nowCityWeather by remember {
        mutableStateOf(
            Parse.weatherData(
                context,
                cityList[0].fileName
            )
        )
    }
    var nowDay by remember {
        mutableStateOf(nowCityWeather.tenDayForecast.also { println(it.size) }.first { it ->
            LocalDate.now() == LocalDate.parse(it.date)
        })
    }
    var nowHour by remember { mutableStateOf<Hour?>(null) }
    LaunchedEffect(Unit) {

        while (true) {
            val hour = LocalTime.now().hour
            nowCityWeather.hourlyForecast.forEach {
                if (hour == it.time.take(2).toInt()) {
                    nowHour = Hour(it.time, it.weather, it.temperature)
                }
            }

            delay(60_000L)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(60.dp),
                title = { Text("") },
                actions = {
                    // 2. 【修改】這裡原本只有 IconButton，現在要用 Box 包起來
                    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {

                        // 這是原本的按鈕 (觸發選單)
                        IconButton(
                            onClick = { showMenu = true } // 點擊時打開選單
                        ) {
                            // 小建議：通常下拉選單會用 MoreVert (三個點)
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        }

                        // 3. 【新增】選單內容
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false } // 點外面關閉
                        ) {
                            // 第一個選項：編輯列表
                            DropdownMenuItem(
                                text = { Text("編輯列表") },
                                onClick = { showMenu = false },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null
                                    )
                                }
                            )

                            // 第二個選項：設定
                            DropdownMenuItem(
                                text = { Text("設定") },
                                onClick = { showMenu = false },

                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Settings,
                                        contentDescription = null
                                    )
                                }
                            )

                            // 分隔線

                            HorizontalDivider()

                            // 第三個選項：攝氏
                            DropdownMenuItem(
                                text = { Text("攝氏 °C") },
                                onClick = {
                                    showMenu = false
                                    temp = true
                                }
                            )

                            // 第四個選項：華氏
                            DropdownMenuItem(
                                text = { Text("華氏 °F") },
                                onClick = {
                                    showMenu = false
                                    temp = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("天氣", fontSize = 30.sp)
            OutlinedTextField(
                searchCity,
                { searchCity = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("輸入城市地點來搜尋") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        null
                    )
                }
            )
            cityList.forEachIndexed { i, city ->
                nowCityWeather = Parse.weatherData(
                    context,
                    city.fileName
                )
                nowDay = nowCityWeather.tenDayForecast.also { println(it.size) }.first { it ->
                    LocalDate.now() == LocalDate.parse(it.date)
                }
                Card(
                    Modifier
                        .fillMaxWidth()
                        .height(125.dp)
                        .clickable { viewModel.push { HomePageScreen(viewModel, cityList) } }
                ) {


                    Box() {
                        Image(
                            painter = painterResource(nowHour?.weather.toString().weatherToImage()),
                            null,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                        Row(
                            Modifier.padding(10.dp)
                        ) {
                            Column {
                                if (i == 0) {
                                    Text("當前位置", color = Color.White)
                                    Text(nowCity.name, color = Color.White)

                                } else {
                                    Text(cityList[i].name, color = Color.White)
                                    Text(
                                        nowHour?.time?.take(2).toString() + ":00",
                                        color = Color.White
                                    )

                                }



                                Spacer(Modifier.weight(1f))
                                Text(
                                    nowHour?.weather.toString().weekEnToCh(), color = Color.White
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            val hour = LocalTime.now().hour
                            nowCityWeather.hourlyForecast.forEach {
                                if (hour == it.time.take(2).toInt()) {
                                    nowHour = Hour(it.time, it.weather, it.temperature)
                                }
                            }
                            val nowTemp = nowHour?.temperature?.filter { it.isDigit() }?.toInt()
                            Column {
                                if (temp) {
                                    Text(
                                        fontSize = 60.sp,
                                        color = Color.White,
                                        text = nowTemp.toString() + "°C"
                                    )
                                } else {
                                    val fTemp = (nowTemp ?: (1 * 9 / 5)) + 32
                                    Text(
                                        fontSize = 60.sp,
                                        color = Color.White,
                                        text = "$fTemp°F"
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                                Text(
                                    "H:" + nowDay.highTemperature + " " + "L:" + nowDay.lowTemperature,
                                    color = Color.White
                                )
                            }
                        }
                    }


                }

            }


        }
    }
}