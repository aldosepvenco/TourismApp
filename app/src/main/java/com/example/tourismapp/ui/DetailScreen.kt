package com.example.tourismapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tourismapp.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage


// Lokasi default tiap kategori
fun getLocationForCategory(category: String): Pair<Double, Double> {
    return when (category.lowercase()) {
        "gunung" -> Pair(-7.5407, 110.4456) // Merapi
        "pantai" -> Pair(-8.0203, 110.2798) // Parangtritis
        "budaya" -> Pair(-7.7972, 110.3688) // Keraton Yogya
        "edukasi" -> Pair(-7.7829, 110.3671) // Taman Pintar
        "kuliner" -> Pair(-7.8014, 110.3644) // Malioboro
        "hiburan" -> Pair(-7.7547, 110.4091) // Jogja Bay
        else -> Pair(-7.7972, 110.3688)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    category: String,
    id: Int
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val weatherApi = WeatherApi.create()
    val apiKey = "39a77512af8653e4e4efb198f824840e"

    var itemData by remember { mutableStateOf<Any?>(null) }
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }

    // --- LOAD DATA DARI DATABASE + CUACA ---
    LaunchedEffect(id) {
        // Load wisata
        itemData = withContext(Dispatchers.IO) {
            when (category) {
                "gunung" -> db.gunungDao().getById(id)
                "pantai" -> db.pantaiDao().getById(id)
                "edukasi" -> db.edukasiDao().getById(id)
                "hiburan" -> db.hiburanDao().getById(id)
                "budaya" -> db.budayaDao().getById(id)
                "kuliner" -> db.kulinerDao().getById(id)
                else -> null
            }
        }

        // Load cuaca
        val (lat, lon) = getLocationForCategory(category)

        weather = try {
            weatherApi.getCurrentWeather(
                lat = lat,
                lon = lon,
                apiKey = apiKey
            )
        } catch (e: Exception) {
            null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Wisata") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        if (itemData == null) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Memuat data...")
            }
            return@Scaffold
        }

        // Ambil data entity
        val image = itemData!!.javaClass.getMethod("getImageRes").invoke(itemData) as Int
        val name = itemData!!.javaClass.getMethod("getName").invoke(itemData) as String
        val descOrDistance = try {
            itemData!!.javaClass.getMethod("getDescription").invoke(itemData) as String
        } catch (e: Exception) {
            itemData!!.javaClass.getMethod("getDistance").invoke(itemData) as String
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // Gambar
            Image(
                painter = painterResource(id = image),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nama wisata
            Text(
                text = name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Deskripsi
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = descOrDistance,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // ============================
            //         CUACA REALTIME
            // ============================
            weather?.let { w ->

                val icon = w.weather.firstOrNull()?.icon ?: "01d"

                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // IKON CUACA DARI INTERNET
                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${icon}@2x.png",
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text("Cuaca Saat Ini", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Lokasi: ${w.name}")
                            Text("Suhu: ${w.main.temp}°C")
                            Text("Cuaca: ${w.weather.first().description}")
                        }
                    }
                }
            }
        }
    }
}
