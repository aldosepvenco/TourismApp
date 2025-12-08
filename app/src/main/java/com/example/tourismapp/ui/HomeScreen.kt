package com.example.tourismapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tourismapp.R
import com.example.tourismapp.data.ForecastResponse
import com.example.tourismapp.data.WeatherApi
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// =====================================================
// DATA MODELS
// =====================================================

private data class StoryItem(val label: String, val imageRes: Int, val route: String)
private data class CategoryItem(val label: String, val imageRes: Int, val route: String)
private data class RecommendationItem(val imageRes: Int, val route: String)

// Kabupaten DIY
private val jogjaLocations = listOf(
    Triple("Sleman", -7.7320, 110.3350),
    Triple("Bantul", -7.8840, 110.3330),
    Triple("Gunungkidul", -8.0300, 110.6160),
    Triple("Kulon Progo", -7.8500, 110.1640),
    Triple("Yogyakarta", -7.7972, 110.3688)
)

// =====================================================
// HOME SCREEN
// =====================================================

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val fused = LocationServices.getFusedLocationProviderClient(context)

    val api = WeatherApi.create()
    val apiKey = "39a77512af8653e4e4efb198f824840e"

    var gpsLocation by remember { mutableStateOf<Location?>(null) }
    var gpsWeather by remember { mutableStateOf<ForecastResponse?>(null) }
    var permissionGranted by remember { mutableStateOf(false) }

    var kabupatenWeather by remember { mutableStateOf<List<ForecastResponse?>>(emptyList()) }

    // =====================================================
    // PERMISSION HANDLER
    // =====================================================

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permissionGranted =
            result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    // Request permission on first load
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // =====================================================
    // Load GPS cuaca realtime user
    // =====================================================

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {

            val request = com.google.android.gms.location.LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                2000L // update tiap 2 detik
            )
                .setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(2000L)
                .build()

            val callback = object : com.google.android.gms.location.LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val loc = result.lastLocation ?: return

                    gpsLocation = loc

                    // Panggil API di background thread
                    CoroutineScope(Dispatchers.IO).launch {
                        gpsWeather = try {
                            api.getForecast(
                                lat = loc.latitude,
                                lon = loc.longitude,
                                apiKey = apiKey // nama parameter HARUS apiKey
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }

                    // Stop update lokasi setelah dapat 1x
                    fused.removeLocationUpdates(this)
                }
            }

            fused.requestLocationUpdates(
                request,
                callback,
                Looper.getMainLooper()
            )
        }
    }

    // =====================================================
    // Load cuaca kabupaten DIY
    // =====================================================

    LaunchedEffect(Unit) {
        val results = mutableListOf<ForecastResponse?>()
        for ((_, lat, lon) in jogjaLocations) {
            val data = try {
                api.getForecast(
                    lat = lat,
                    lon = lon,
                    apiKey = apiKey
                )
            } catch (_: Exception) {
                null
            }
            results.add(data)
        }
        kabupatenWeather = results
    }

    // =====================================================
    // UI HOME
    // =====================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEAF3FF), Color(0xFFF7F9FC))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {

            HeaderSection()
            StorySection(navController)
            CategorySection(navController)

            // ============================
            // Cuaca Lokasi Pengguna
            // ============================

            gpsWeather?.list?.firstOrNull()?.let { item ->
                Spacer(Modifier.height(22.dp))

                Text(
                    "Cuaca Lokasi Anda",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(Modifier.height(12.dp))

                WeatherCard(
                    location = "Lokasi Saat Ini",
                    temp = item.main.temp,
                    icon = item.weather.first().icon,
                    desc = item.weather.first().main
                )
            }

            // ============================
            // Cuaca Kabupaten DIY
            // ============================

            Spacer(Modifier.height(28.dp))

            Text(
                "Cuaca Kabupaten DIY",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                itemsIndexed(jogjaLocations) { index, (name, _, _) ->
                    val data = kabupatenWeather.getOrNull(index)?.list?.firstOrNull()

                    WeatherCard(
                        location = name,
                        temp = data?.main?.temp ?: 0.0,
                        icon = data?.weather?.firstOrNull()?.icon ?: "01d",
                        desc = data?.weather?.firstOrNull()?.main ?: "-"
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            RecommendationSection(navController)

            Spacer(Modifier.height(40.dp))
        }
    }
}

// =====================================================
// HEADER SECTION
// =====================================================

@Composable
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
    ) {

        Image(
            painter = painterResource(id = R.drawable.tugu_welcome_page),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent),
                        startY = 0f,
                        endY = 600f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 24.dp)
        ) {
            Text("Hi bubs,", color = Color.White, fontSize = 18.sp)
            Text(
                "Mau kemana\nhari ini?",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// =====================================================
// STORY SECTION
// =====================================================

@Composable
private fun StorySection(nav: NavController) {

    val stories = listOf(
        StoryItem("Gunung", R.drawable.merapi, "gunung_page"),
        StoryItem("Pantai", R.drawable.pantai1, "pantai_page"),
        StoryItem("Budaya", R.drawable.sonobudoyo, "budaya_page"),
        StoryItem("Edukasi", R.drawable.taman_pintar, "edukasi_page"),
        StoryItem("Kuliner", R.drawable.sedayu, "kuliner_page"),
        StoryItem("Hiburan", R.drawable.jogja_bay, "hiburan_page")
    )

    Spacer(Modifier.height(18.dp))

    Text(
        "Story Wisata",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )

    Spacer(Modifier.height(10.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.padding(start = 20.dp)
    ) {
        itemsIndexed(stories) { _, item ->
            StoryCircle(item) {
                nav.navigate(item.route) {
                    launchSingleTop = true
                }
            }
        }
    }
}

// =====================================================
// CATEGORY SECTION
// =====================================================

@Composable
private fun CategorySection(nav: NavController) {

    // 5 kabupaten DIY
    val categories = listOf(
        // kalau belum punya gambar sleman/bantul/... pakai dulu R.drawable.icon_sementara
        CategoryItem("Sleman",      R.drawable.icon_sementara, "kab_sleman"),
        CategoryItem("Bantul",      R.drawable.icon_sementara, "kab_bantul"),
        CategoryItem("Gunungkidul", R.drawable.icon_sementara, "kab_gunungkidul"),
        CategoryItem("Kulon Progo", R.drawable.icon_sementara, "kab_kulonprogo"),
        CategoryItem("Yogyakarta",  R.drawable.icon_sementara, "kab_yogyakarta"),
    )

    Spacer(Modifier.height(24.dp))

    Text(
        "Jelajahi Kabupaten",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )

    Spacer(Modifier.height(10.dp))

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.85f),
                        Color.White.copy(alpha = 0.6f)
                    )
                )
            )
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            itemsIndexed(categories) { _, item ->
                GlassCategoryCard(item) {
                    nav.navigate(item.route)
                }
            }
        }
    }
}


// =====================================================
// REKOMENDASI SECTION
// =====================================================

@Composable
private fun RecommendationSection(nav: NavController) {

    val rekom = listOf(
        RecommendationItem(R.drawable.destinasi1, "gunung_page"),
        RecommendationItem(R.drawable.destinasi2, "pantai_page"),
        RecommendationItem(R.drawable.pantai3, "pantai_page"),
        RecommendationItem(R.drawable.tugu_welcome_page, "budaya_page")
    )

    Text(
        text = "Rekomendasi Wisata",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        itemsIndexed(rekom) { _, item ->
            GlassBigCard(
                imageRes = item.imageRes,
                onClick = {
                    nav.navigate(item.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

// =====================================================
// WEATHER CARD
// =====================================================

@Composable
private fun WeatherCard(
    location: String,
    temp: Double,
    icon: String,
    desc: String
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
        modifier = Modifier
            .width(160.dp)
            .height(180.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(location, fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Spacer(Modifier.height(6.dp))

            AsyncImage(
                model = "https://openweathermap.org/img/wn/$icon@2x.png",
                contentDescription = "",
                modifier = Modifier.size(58.dp)
            )

            Text("${temp}°C", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(desc, fontSize = 14.sp)
        }
    }
}

// =====================================================
// STORY CIRCLE
// =====================================================

@Composable
private fun StoryCircle(item: StoryItem, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                .padding(3.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(4.dp))
        Text(item.label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

// =====================================================
// CATEGORY CARD
// =====================================================

@Composable
private fun GlassCategoryCard(item: CategoryItem, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.40f)),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF5C9DFF).copy(alpha = 0.9f),
                                Color(0xFF9DE2FF).copy(alpha = 0.9f)
                            )
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(6.dp))
            Text(item.label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// =====================================================
// BIG RECOMMENDATION CARD
// =====================================================

@Composable
private fun GlassBigCard(imageRes: Int, onClick: () -> Unit, height: Dp = 150.dp) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
        modifier = Modifier
            .width(260.dp)
            .height(height)
            .clickable { onClick() }
    ) {
        Box {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.45f))
                        )
                    )
            )
        }
    }
}
