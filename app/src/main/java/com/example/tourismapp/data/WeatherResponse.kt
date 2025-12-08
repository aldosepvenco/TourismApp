package com.example.tourismapp.data

data class WeatherResponse(
    val main: MainInfo,
    val weather: List<WeatherInfo>,
    val name: String
)

data class MainInfo(
    val temp: Double
)

data class WeatherInfo(
    val main: String,
    val description: String,
    val icon: String
)
