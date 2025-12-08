package com.example.tourismapp.data

data class ForecastResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt_txt: String,          // waktu (2024-02-02 12:00:00)
    val main: MainInfo,          // suhu
    val weather: List<WeatherInfo> // icon + deskripsi
)
