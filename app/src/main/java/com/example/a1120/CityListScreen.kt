package com.example.a1120

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Collections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(viewModel: MainViewModel) {
    val focusManger = LocalFocusManager.current
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var searchCity by remember { mutableStateOf("") }
    val verticalScrollState = rememberScrollState()
    var isSearchOrEdit by remember { mutableIntStateOf(0) }// 0 cityList , 1 search , 2 edit

    var localTime by remember {
        mutableStateOf(
            LocalTime.now()
                .format(DateTimeFormatter.ofPattern("h:mm a"))
        )

    }


    val chOrEn by remember { mutableStateOf(Prefs.takeLanguage(context)) }


    val cityListXml = remember { Parse.cityList(context, "city_list.xml") }
    val prefs = cityListXml.filter {
        Prefs.takeCity(context, it.name)
    }
    val nowCity by remember { mutableStateOf(cityListXml.first { it.type == "current" }) }
    val cityList = remember {
        if (prefs.isNotEmpty()) {
            prefs.toMutableStateList()
        } else {
            mutableStateListOf(nowCity)
        }
    }
    var searchCityList by remember {
        mutableStateOf(cityListXml)
    }

    var nowCityWeather by remember {
        mutableStateOf(
            Parse.weatherData(
                context,
                cityList[0].fileName
            )
        )
    }
    var nowDay by remember {
        mutableStateOf(nowCityWeather.tenDayForecast.also { println(it.size) }.first {
            LocalDate.now() == LocalDate.parse(it.date)
        })
    }
    var nowHour by remember { mutableStateOf<Hour?>(null) }
    LaunchedEffect(nowCityWeather) {

        while (true) {

            val hour = LocalTime.now().hour
            nowCityWeather.hourlyForecast.forEach {
                if (hour == it.time.take(2).toInt()) {
                    nowHour = Hour(it.time, it.weather, it.temperature)
                }
            }
            localTime = LocalTime.now()
                .format(DateTimeFormatter.ofPattern("h:mm a"))

            delay(60_000L)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(30.dp),
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
                                text = {
                                    Text(
                                        changeLanguage(
                                            chOrEn,
                                            "編輯列表",
                                            "edit"
                                        ),
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    isSearchOrEdit = 2
                                },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null
                                    )
                                }
                            )

                            // 第二個選項：設定
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        changeLanguage(
                                            chOrEn,
                                            "設定",
                                            "setting"
                                        ),
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    viewModel.push { SettingScreen(viewModel) }
                                },

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
                                text = {
                                    Text(
                                        changeLanguage(
                                            chOrEn,
                                            "攝氏",
                                            "°C"
                                        )
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    Prefs.rememberTemp(context, true)
                                }
                            )

                            // 第四個選項：華氏
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        changeLanguage(
                                            chOrEn,
                                            "華氏",
                                            "°F"
                                        )
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    Prefs.rememberTemp(context, false)
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
                .padding(20.dp)
                .verticalScroll(verticalScrollState)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        // 4. 當點擊發生時，清除焦點 (鍵盤就會收起來)
                        focusManger.clearFocus()
                        isSearchOrEdit = 0
                        searchCity = ""
                    })
                },
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                changeLanguage(
                    chOrEn,
                    "天氣",
                    "Weather"
                ), fontSize = 30.sp
            )
            OutlinedTextField(//搜尋框
                searchCity,
                { s ->
                    searchCity = s
                    if (s.isNotBlank()) {
                        isSearchOrEdit = 1
                    }// 1 search

                    searchCityList = if (isSearchOrEdit == 1) {
                        cityListXml
                            .filter { it.name.contains(searchCity) }
                            .map { city ->
                                City(city.type, city.name, city.nameEn, city.fileName)
                            }.toMutableStateList()
                    } else {
                        cityListXml.toMutableStateList()
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        changeLanguage(
                            chOrEn,
                            "輸入城市地點來搜尋",
                            "Input city to search"
                        )
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        null
                    )
                },
                singleLine = true
            )
            when (isSearchOrEdit) {
                1 -> { //搜尋時的列表
                    searchCityList.forEachIndexed { i, city ->
                        nowCityWeather = Parse.weatherData(
                            context,
                            city.fileName
                        )
                        nowDay = nowCityWeather.tenDayForecast.first {
                            LocalDate.now() == LocalDate.parse(it.date)
                        }
                        Row(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (!cityList.contains(city)) {


                                Card(
                                    Modifier
                                        .fillMaxWidth(.8f)
                                        .height(150.dp)
                                        .clickable {
                                            viewModel.push {
                                                HomePageScreen(
                                                    viewModel,
                                                    cityList
                                                )
                                            }
                                        }
                                ) {


                                    Box {
                                        val hour = LocalTime.now().hour
                                        nowCityWeather.hourlyForecast.forEach {
                                            if (hour == it.time.take(2).toInt()) {
                                                nowHour = Hour(it.time, it.weather, it.temperature)
                                            }
                                        }
                                        Image(

                                            painter = painterResource(
                                                nowHour?.weather.toString().weatherToImage()
                                            ),
                                            null,
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                        )
                                        Row(
                                            Modifier.padding(10.dp)
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.Start
                                            ) {
                                                if (i == 0) {
                                                    Text(
                                                        changeLanguage(chOrEn, "當前位置", "Local"),
                                                        color = Color.White
                                                    )
                                                    Text(
                                                        changeLanguage(
                                                            chOrEn,
                                                            nowHour?.weather.toString()
                                                                .weatherToCh(),
                                                            nowHour?.weather.toString()
                                                        ), color = Color.White
                                                    )
                                                    Spacer(Modifier.weight(1f))
                                                    Text(
                                                        changeLanguage(
                                                            chOrEn,
                                                            nowCity.name,
                                                            nowCity.nameEn
                                                        ), color = Color.White
                                                    )

                                                } else {
                                                    Text(
                                                        changeLanguage(
                                                            chOrEn,
                                                            nowCity.name,
                                                            nowCity.nameEn
                                                        ), color = Color.White
                                                    )
                                                    Text(
                                                        changeLanguage(
                                                            chOrEn,
                                                            nowHour?.weather.toString()
                                                                .weatherToCh(),
                                                            nowHour?.weather.toString()
                                                        ), color = Color.White
                                                    )
                                                    Spacer(Modifier.weight(1f))



                                                    Text(

                                                        localTime,
                                                        color = Color.White
                                                    )

                                                }
                                            }
                                            Spacer(Modifier.weight(1f))
                                            nowCityWeather.hourlyForecast.forEach {
                                                if (hour == it.time.take(2).toInt()) {
                                                    nowHour =
                                                        Hour(it.time, it.weather, it.temperature)
                                                }
                                            }
                                            val nowTemp =
                                                nowHour?.temperature?.filter { it.isDigit() }
                                                    ?.toInt()
                                            Column {
                                                if (Prefs.takeTemp(context)) {
                                                    Text(
                                                        fontSize = 60.sp,
                                                        color = Color.White,
                                                        text = nowTemp.toString() + "°C"
                                                    )
                                                    Text(
                                                        "H:" + nowDay.highTemperature + " " + "L:" + nowDay.lowTemperature,
                                                        color = Color.White
                                                    )
                                                } else {
                                                    val fTemp = (nowTemp ?: (1 * 9 / 5)) + 32
                                                    Text(
                                                        fontSize = 60.sp,
                                                        color = Color.White,
                                                        text = "$fTemp°F"
                                                    )
                                                    val fTempH =
                                                        (nowDay.highTemperature.filter { it.isDigit() }
                                                            .toInt() * 9 / 5) + 32
                                                    val fTempL =
                                                        (nowDay.lowTemperature.filter { it.isDigit() }
                                                            .toInt() * 9 / 5) + 32

                                                    Text(
                                                        "H:$fTempH°F L:$fTempL°F",
                                                        color = Color.White
                                                    )
                                                }
                                                Spacer(Modifier.weight(1f))

                                            }
                                        }
                                    }
                                }
                                Spacer(Modifier.weight(1f))


                                if (i != 0) {
                                    Button(
                                        {
                                            if (!cityList.any { it.name == city.name }) {
                                                cityList.add(city)
                                                Prefs.rememberCity(context, city.name, true)
                                                searchCity = ""
                                                isSearchOrEdit = 0
                                            }
                                        },
                                        modifier = Modifier.height(150.dp)
                                    ) { Text("+") }
                                }
                            }


                        }

                    }
                }

                0 -> {
                    cityList.forEachIndexed { i, city ->
                        nowCityWeather = Parse.weatherData(
                            context,
                            city.fileName
                        )
                        nowDay = nowCityWeather.tenDayForecast.first {
                            LocalDate.now() == LocalDate.parse(it.date)
                        }
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clickable {
                                    viewModel.push {
                                        HomePageScreen(
                                            viewModel,
                                            cityList
                                        )
                                    }
                                }
                        ) {


                            Box {
                                val hour = LocalTime.now().hour
                                nowCityWeather.hourlyForecast.forEach {
                                    if (hour == it.time.take(2).toInt()) {
                                        nowHour = Hour(it.time, it.weather, it.temperature)
                                    }
                                }
                                Image(

                                    painter = painterResource(
                                        nowHour?.weather.toString().weatherToImage()
                                    ),
                                    null,
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                                Row(
                                    Modifier.padding(10.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        if (i == 0) {
                                            Text(
                                                changeLanguage(chOrEn, "當前位置", "Local"),
                                                color = Color.White
                                            )
                                            Text(
                                                changeLanguage(
                                                    chOrEn,
                                                    nowCity.name,
                                                    nowCity.nameEn
                                                ), color = Color.White
                                            )

                                        } else {
                                            Text(
                                                changeLanguage(
                                                    chOrEn,
                                                    cityList[i].name,
                                                    cityList[i].nameEn
                                                ), color = Color.White
                                            )



                                            Text(

                                                localTime,
                                                color = Color.White
                                            )

                                        }
                                    }
                                    Spacer(Modifier.weight(1f))

                                    nowCityWeather.hourlyForecast.forEach {
                                        if (hour == it.time.take(2).toInt()) {
                                            nowHour = Hour(it.time, it.weather, it.temperature)
                                        }
                                    }
                                    val nowTemp =
                                        nowHour?.temperature?.filter { it.isDigit() }?.toInt()
                                    Column(
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        if (Prefs.takeTemp(context)) {
                                            Text(
                                                fontSize = 60.sp,
                                                color = Color.White,
                                                text = nowTemp.toString() + "°C"
                                            )
                                            Text(
                                                changeLanguage(
                                                    chOrEn,
                                                    nowHour?.weather.toString().weatherToCh(),
                                                    nowHour?.weather.toString()
                                                ), color = Color.White
                                            )
                                            Spacer(Modifier.weight(1f))
                                            Text(
                                                "H:" + nowDay.highTemperature + " " + "L:" + nowDay.lowTemperature,
                                                color = Color.White
                                            )
                                        } else {
                                            val fTemp = (nowTemp ?: (1 * 9 / 5)) + 32
                                            Text(
                                                fontSize = 60.sp,
                                                color = Color.White,
                                                text = "$fTemp°F"
                                            )
                                            Text(
                                                changeLanguage(
                                                    chOrEn,
                                                    nowHour?.weather.toString().weatherToCh(),
                                                    nowHour?.weather.toString()
                                                ), color = Color.White
                                            )
                                            Spacer(Modifier.weight(1f))
                                            val fTempH =
                                                (nowDay.highTemperature.filter { it.isDigit() }
                                                    .toInt() * 9 / 5) + 32
                                            val fTempL =
                                                (nowDay.lowTemperature.filter { it.isDigit() }
                                                    .toInt() * 9 / 5) + 32

                                            Text(
                                                "H:$fTempH°F L:$fTempL°F",
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {//2 edit
                    Button(
                        onClick = { isSearchOrEdit = 0 }, modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            changeLanguage(chOrEn, "完成", "finish")
                        )
                    }
                    cityList.forEachIndexed { i, city ->
                        nowCityWeather = Parse.weatherData(
                            context,
                            city.fileName
                        )
                        nowDay = nowCityWeather.tenDayForecast.first {
                            LocalDate.now() == LocalDate.parse(it.date)
                        }
                        Row {
                            Card(
                                Modifier
                                    .fillMaxWidth(.8f)
                                    .height(150.dp)
                                    .clickable {
                                        viewModel.push {
                                            HomePageScreen(
                                                viewModel,
                                                cityList
                                            )
                                        }
                                    }
                            ) {


                                Box {
                                    val hour = LocalTime.now().hour
                                    nowCityWeather.hourlyForecast.forEach {
                                        if (hour == it.time.take(2).toInt()) {
                                            nowHour = Hour(it.time, it.weather, it.temperature)
                                        }
                                    }
                                    Image(

                                        painter = painterResource(
                                            nowHour?.weather.toString().weatherToImage()
                                        ),
                                        null,
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentScale = ContentScale.Crop,
                                    )
                                    Row(
                                        Modifier.padding(10.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.Start
                                        ) {


                                            if (i == 0) {
                                                Text(
                                                    changeLanguage(chOrEn, "當前位置", "Local"),
                                                    color = Color.White
                                                )
                                                Text(
                                                    changeLanguage(
                                                        chOrEn,
                                                        nowCity.name,
                                                        nowCity.nameEn
                                                    ), color = Color.White
                                                )

                                            } else {
                                                Text(
                                                    changeLanguage(
                                                        chOrEn,
                                                        cityList[i].name,
                                                        cityList[i].nameEn
                                                    ), color = Color.White
                                                )



                                                Text(

                                                    localTime,
                                                    color = Color.White
                                                )

                                            }
                                        }
                                        Spacer(Modifier.weight(1f))
                                        nowCityWeather.hourlyForecast.forEach {
                                            if (hour == it.time.take(2).toInt()) {
                                                nowHour = Hour(it.time, it.weather, it.temperature)
                                            }
                                        }
                                        val nowTemp =
                                            nowHour?.temperature?.filter { it.isDigit() }?.toInt()
                                        Column(
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            if (Prefs.takeTemp(context)) {
                                                Text(
                                                    fontSize = 50.sp,
                                                    color = Color.White,
                                                    text = nowTemp.toString() + "°C"
                                                )
                                                Text(
                                                    changeLanguage(
                                                        chOrEn,
                                                        nowHour?.weather.toString().weatherToCh(),
                                                        nowHour?.weather.toString()
                                                    ), color = Color.White
                                                )
                                                Spacer(Modifier.weight(1f))
                                                Text(
                                                    "H:" + nowDay.highTemperature + " " + "L:" + nowDay.lowTemperature,
                                                    color = Color.White
                                                )
                                            } else {
                                                val fTemp = (nowTemp ?: (1 * 9 / 5)) + 32
                                                Text(
                                                    fontSize = 50.sp,
                                                    color = Color.White,
                                                    text = "$fTemp°F"
                                                )
                                                Text(
                                                    changeLanguage(
                                                        chOrEn,
                                                        nowHour?.weather.toString().weatherToCh(),
                                                        nowHour?.weather.toString()
                                                    ), color = Color.White
                                                )
                                                Spacer(Modifier.weight(1f))
                                                val fTempH =
                                                    (nowDay.highTemperature.filter { it.isDigit() }
                                                        .toInt() * 9 / 5) + 32
                                                val fTempL =
                                                    (nowDay.lowTemperature.filter { it.isDigit() }
                                                        .toInt() * 9 / 5) + 32

                                                Text(
                                                    "H:$fTempH°F L:$fTempL°F",
                                                    color = Color.White
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.weight(1f))
                            if (i != 0) {


                                Column {
                                    Button(
                                        {
                                            cityList.removeAt(i)
                                            Prefs.rememberCity(context, city.name, false)
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("-", fontSize = 25.sp)
                                    }
                                    if (i != 1) {

                                        Button(
                                            { Collections.swap(cityList, i, i - 1) },
                                            modifier = Modifier.fillMaxWidth()

                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.baseline_keyboard_arrow_up_24),
                                                null
                                            )
                                        }
                                    }
                                    if (i != cityList.size - 1) {

                                        Button(
                                            {
                                                Collections.swap(cityList, i, i + 1)
                                            },
                                            modifier = Modifier.fillMaxWidth()


                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                                                null
                                            )
                                        }
                                    }

                                }
                            }
                            Spacer(Modifier.weight(1f))

                        }
                    }

                }
            }
        }
    }
}


fun changeLanguage(b: Boolean, c: String, e: String): String {
    if (b) {
        return c
    }
    return e
}



