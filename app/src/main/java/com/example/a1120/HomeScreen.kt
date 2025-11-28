package com.example.a1120

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a1120.ui.theme.Color1
import com.example.a1120.ui.theme.Color2
import com.example.a1120.ui.theme.Color3
import com.example.a1120.ui.theme.Color4
import com.example.a1120.ui.theme.Color5
import com.example.a1120.ui.theme.Color6
import com.example.a1120.ui.theme.Color7
import com.example.a1120.ui.theme.DarkBlue
import com.example.a1120.ui.theme.LightBlue
import com.example.a1120.ui.theme.Orange
import java.time.LocalDate

fun weatherToIcon(a: String) = when (a) {
    "cloudy" -> R.drawable.cloudy2
    "sunny" -> R.drawable.sunny2
    "rain" -> R.drawable.rain2
    "thunder" -> R.drawable.thunder2
    "overcast" -> R.drawable.overcast2
    else -> R.drawable.sunny2
}

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    nowCity: City,
    nowCityWeather: WeatherData,
    nowDay: Day,
    nowHour: Hour
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box {
        Image(
            painter = painterResource(
                weatherToImage(nowHour.weather)
            ),
            null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text("當前位置", fontSize = 30.sp, color = Color.White)
            Text(nowCity.name, color = Color.White)
            Text(nowHour.temperature, color = Color.White, fontSize = 50.sp)
            Text(
                when (nowHour.weather) {
                    "cloudy" -> "多雲"
                    "sunny" -> "晴天"
                    "rain" -> "下雨"
                    "thunder" -> "打雷"
                    "overcast" -> "陰天"
                    else -> "晴天"
                },
                color = Color.White
            )
            Text(
                "H:" + nowDay.highTemperature + "  L:" + nowDay.lowTemperature,
                color = Color.White
            )
            //上
            Card(
                colors = CardDefaults.cardColors(
                    // 0.3f 代表 30% 不透明 (很透)
                    // 0.8f 代表 80% 不透明 (稍微透)
                    containerColor = Color.White.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .size(350.dp, 100.dp)
            ) {
                var n = 1
                Row(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(35.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    nowCityWeather.hourlyForecast.forEach {
                        if (n >= 7) {
                            println("return")
                            return@Row
                        }
                        if (nowHour.time == it.time) {


                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("現在", color = Color.White)
                                Icon(
                                    painter = painterResource(
                                        when (it.weather) {
                                            "cloudy" -> R.drawable.cloudy2
                                            "sunny" -> R.drawable.sunny2
                                            "rain" -> R.drawable.rain2
                                            "thunder" -> R.drawable.thunder2
                                            "overcast" -> R.drawable.overcast2
                                            else -> R.drawable.sunny2
                                        }
                                    ),
                                    null,
                                    tint = Color.White
                                )
                                Text(it.temperature, color = Color.White)
                            }
                            n++
                        }
                        if (n > 1) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(it.time.take(2), color = Color.White)
                                Icon(
                                    painter = painterResource(
                                        when (it.weather) {
                                            "cloudy" -> R.drawable.cloudy2
                                            "sunny" -> R.drawable.sunny2
                                            "rain" -> R.drawable.rain2
                                            "thunder" -> R.drawable.thunder2
                                            "overcast" -> R.drawable.overcast2
                                            else -> R.drawable.sunny2
                                        }
                                    ),
                                    null,
                                    tint = Color.White
                                )
                                Text(it.temperature, color = Color.White)
                            }
                            n++
                        }

                    }

                }
            }
            Spacer(modifier = Modifier.size(4.dp))
            //中
            Card(
                colors = CardDefaults.cardColors(
                    // 0.3f 代表 30% 不透明 (很透)
                    // 0.8f 代表 80% 不透明 (稍微透)
                    containerColor = Color.White.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .size(350.dp, 200.dp)
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_calendar_month_24),
                        null,
                        tint = Color.White
                    )
                    Text("10天內天氣預報", color = Color.White)
                }
                var a = 0
                HorizontalDivider(thickness = 2.dp)

                var max = 0
                var min = 0
                for (i in 0 until nowCityWeather.tenDayForecast.size) {
                    if (nowDay == nowCityWeather.tenDayForecast[i]
                    ) {
                        a = 1
                    }



                    if (a in 2..7) {
                        if (max < nowCityWeather.tenDayForecast[i].highTemperature.filter { it.isDigit() }
                                .toInt()) {
                            max =
                                nowCityWeather.tenDayForecast[i].highTemperature.filter { it.isDigit() }
                                    .toInt()

                        }
                        if (min > nowCityWeather.tenDayForecast[i].lowTemperature.filter { it.isDigit() }
                                .toInt()) {
                            min =
                                nowCityWeather.tenDayForecast[i].lowTemperature.filter { it.isDigit() }
                                    .toInt()

                        }
                        a++
                    }
                    if (a == 1) {
                        max =
                            nowCityWeather.tenDayForecast[i].highTemperature.filter { it.isDigit() }
                                .toInt()

                        min =
                            nowCityWeather.tenDayForecast[i].lowTemperature.filter { it.isDigit() }
                                .toInt()
                        a++
                    }
                }

                for (i in 0 until nowCityWeather.tenDayForecast.size) {
                    if (nowDay == nowCityWeather.tenDayForecast[i]
                    ) {

                        a = 1
                        println("true")
                    }


                    if (a in 1..7) {
                        Row {
                            if (a == 1) {
                                Text("今天", color = Color.White)
                                Spacer(Modifier.width(65.dp))
                            } else {
                                Text(
                                    weekEnToCh(LocalDate.parse(nowCityWeather.tenDayForecast[i].date).dayOfWeek.toString()),
                                    color = Color.White
                                )
                                Spacer(Modifier.width(50.dp))
                            }
                            a++
                            Icon(
                                painter = painterResource(weatherToIcon(nowCityWeather.tenDayForecast[i].weatherCondition)),
                                null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(50.dp))
                            val low =
                                nowCityWeather.tenDayForecast[i].lowTemperature.takeWhile { it.isDigit() }
                            val high =
                                nowCityWeather.tenDayForecast[i].highTemperature.takeWhile { it.isDigit() }
                            Text("$low°", color = Color.White)
                            val lineStart =
                                ((low.toFloat() - min.toFloat()) / (max.toFloat() - min.toFloat())) * 230f + 20f
                            val lineEnd =
                                ((high.toFloat() - min.toFloat()) / (max.toFloat() - min.toFloat())) * 230f + 20f
                            var lineColor = listOf(LightBlue, DarkBlue)
                            if (high.toInt() < 15) {
                                lineColor = listOf(LightBlue, DarkBlue)
                            } else if (low.toInt() > 15) {
                                lineColor = listOf(Color.Yellow, Orange, Color.Red)
                            } else if (high.toInt() > 15 && low.toInt() < 15) {
                                lineColor = listOf(Color.Yellow)
                            }
                            Canvas(modifier = Modifier.size(100.dp, 10.dp)) {
                                drawLine(
                                    color = Color.Black,
                                    start = Offset(20f, 25f),
                                    end = Offset(250f, 25f),
                                    strokeWidth = 5f,
                                    cap = StrokeCap.Round
                                )
                                drawLine(
                                    brush = Brush.linearGradient(
                                        lineColor
                                    ),
                                    start = Offset(lineStart, 25f),
                                    end = Offset(lineEnd, 25f),
                                    strokeWidth = 10f,
                                    cap = StrokeCap.Round
                                )
                                println(min.toString() + " " + lineStart)
                            }
                            Text("$high°", color = Color.White)

                        }


                    }


                }

            }
            //下
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .size(350.dp, 250.dp)

            ) {
                Column(
                    modifier = Modifier
                        .size(350.dp, 350.dp)
                        .padding(10.dp),
                ) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.round_timeline_24),
                            null,
                            tint = Color.White
                        )
                        Text("空氣指標", color = Color.White)
                    }
                    HorizontalDivider()

                    val aqi = nowCityWeather.aqi
                    Spacer(Modifier.height(20.dp))
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier.size(150.dp)
                        ) {
                            drawArc(
                                color = Color.LightGray,
                                startAngle = 170f,
                                sweepAngle = 200f,
                                useCenter = false,
                                style = Stroke(width = 20f, cap = StrokeCap.Round)
                            )
                        }
                        Canvas(
                            modifier = Modifier.size(150.dp)
                        ) {
                            drawArc(
                                color = when (aqi) {
                                    in 0..25 -> Color1
                                    in 25..50 -> Color2
                                    in 50..75 -> Color3
                                    in 75..100 -> Color4
                                    in 100..125 -> Color5
                                    in 125..150 -> Color6
                                    in 150..175 -> Color7
                                    else -> Color.Black
                                },
                                startAngle = 170f,
                                sweepAngle = aqi / 170f * 200f,
                                useCenter = false,
                                style = Stroke(width = 20f, cap = StrokeCap.Round)
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(20.dp))
                            Text(aqi.toString(), fontSize = 30.sp, color = Color.White)
                            Text("AQI", color = Color.White)
                            var aqiIsGoodOrNot = ""
                            if (aqi in 0..50) {
                                aqiIsGoodOrNot = "良"
                            } else if (aqi in 51..100) {
                                aqiIsGoodOrNot = "普通"
                            } else if (aqi > 100) {
                                aqiIsGoodOrNot = "不健康"
                            }

                            Spacer(Modifier.height(20.dp))

                            Text(
                                aqiIsGoodOrNot,
                                color = Color.White,
                                modifier = Modifier
                                    // 2. 先畫綠色背景 (還要有點圓角)
                                    .background(
                                        color = when (aqi) {
                                            in 0..25 -> Color1
                                            in 25..50 -> Color2
                                            in 50..75 -> Color3
                                            in 75..100 -> Color4
                                            in 100..125 -> Color5
                                            in 125..150 -> Color6
                                            in 150..175 -> Color7
                                            else -> Color.Black
                                        }, // 這是類似圖片裡的淺綠色
                                        shape = RoundedCornerShape(4.dp) // 微微的圓角
                                    )
                                    .padding(horizontal = 24.dp, vertical = 6.dp)
                            )

                        }
                    }

                }
            }

        }
    }
}


fun weekEnToCh(week: String) = when (week) {
    "SATURDAY" -> "星期六"
    "SUNDAY" -> "星期日"
    "FRIDAY" -> "星期五"
    "THURSDAY" -> "星其四"
    "WEDNESDAY" -> "星期三"
    "TUESDAY" -> "星期二"
    "MONDAY" -> "星期一"
    else -> "星期一"

}

fun weatherToImage(a: String) = when (a) {
    "cloudy" -> R.drawable.cloudy
    "sunny" -> R.drawable.sunny
    "rain" -> R.drawable.rain
    "thunder" -> R.drawable.thunder
    "overcast" -> R.drawable.overcast
    else -> R.drawable.sunny
}
