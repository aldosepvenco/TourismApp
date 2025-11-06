package com.example.tourismapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tourismapp.ui.theme.TourismAppTheme
import androidx.navigation.navArgument
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TourismApp()
        }
    }
}

@Composable
fun TourismApp(modifier: Modifier = Modifier) {
    Scaffold { innerPadding ->
        val navController = rememberNavController()

        // This determines if the welcome screen should be shown.
        // We use rememberSaveable to keep the state across configuration changes.
        // The NavHost startDestination will be conditional based on this.
        val (showWelcomeScreen, setShowWelcomeScreen) = rememberSaveable { mutableStateOf(true) }

        NavHost(
            navController = navController,
            startDestination = if (showWelcomeScreen) "welcome" else "home",
            modifier = modifier.padding(innerPadding)
        ) {
            composable("welcome") {
                WelcomeScreen(onGetStartedClick = {
                    // When the button is clicked, we navigate to home
                    // and update the state so the welcome screen is not shown again.
                    setShowWelcomeScreen(false) // No longer show welcome screen on recomposition
                    navController.navigate("home") {
                        // Pop up to the start destination of the graph to avoid building up a large back stack
                        // on the user's way to the home screen.
                        popUpTo("welcome") { inclusive = true }
                    }
                })
            }
            composable("home") { HomeScreen(navController = navController) }
            composable("gunung_page") { gunungPage(navController = navController) }
            composable(
                "detail_gunung/{gunungId}",
                arguments = listOf(navArgument("gunungId") { type = NavType.StringType })
            ) { backStackEntry ->
                detailGunung(
                    navController = navController,
                    gunungId = backStackEntry.arguments?.getString("gunungId")
                )
            }
            composable("detail_pantai/{pantaiId}",
                arguments = listOf(navArgument("pantaiId") { type = NavType.StringType })) { backStackEntry ->
                detailPantai(navController = navController, pantaiId = backStackEntry.arguments?.getString("pantaiId"))
            }
            // Add other routes for the new pages
            composable("pantai_page") { PantaiPage(navController = navController) }
//            composable("budaya_page") { BudayaPage(navController = navController) }
//            composable("edukasi_page") { EdukasiPage(navController = navController) }
//            composable("kuliner_page") { KulinerPage(navController = navController) }
//            composable("hiburan_page") { HiburanPage(navController = navController) }

        }
    }
}

@Composable
fun WelcomeScreen(onGetStartedClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        // Background image full-screen
        Image(
            painter = painterResource(R.drawable.tugu_welcome_page),
            contentDescription = "Background Tugu",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.colorMatrix(
                ColorMatrix().apply {
                    setToScale(0.8f, 0.8f, 0.8f, 1f) // reduce brightness/intensity
                }
            )
        )

        // Scrim / overlay gelap agar teks putih terlihat seperti referensi
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0x80000000), Color(0x00000000)),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Logo + Judul di tengah atas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 350.dp), // Adjust this value to position vertically
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // jika punya icon logo, aktifkan Image di bawah:
            Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "logo aplikasi",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
            // Image(painter = painterResource(R.drawable.ic_logo), contentDescription = "logo", modifier = Modifier.size(56.dp))
            Text(
                text = "JogjaGo",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 28.sp
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        // Panel biru sebagai sibling, menempel ke bawah (dari ujung-ke-ujung)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)         // penting: posisikan di bawah
                .fillMaxWidth()                        // lebar ujung-ke-ujung
                .height(220.dp)                        // tinggi sesuai referensi (ubah bila perlu)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF2196F3), Color(0xFF64B5F6))
                    ),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp) // sudut besar atas
                )
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Konten panel: teks rata kiri, tapi tombol dipusatkan horizontal
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Welcome to",
                    color = Color.White.copy(alpha = 0.95f),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Indonesia Tourism App",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )

                // Tombol: letakkan spacer agar tombol sedikit ke bawah, lalu center horizontally
                Spacer(modifier = Modifier.height(25.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onGetStartedClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Mulai sekarang",
                            color = Color(0xFF1976D2),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    TourismAppTheme {
        WelcomeScreen(onGetStartedClick = { })
    }
}

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Adjust height as needed
        ) {
            Image(
                painter = painterResource(R.drawable.home1),
                contentDescription = "Background",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(
                    ColorMatrix().apply {
                        setToScale(0.8f, 0.8f, 0.8f, 1f) // reduce brightness/intensity
                    }
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hi bubs,",
                    color = Color.White,
                    fontSize = 17.sp,
                )
                Text(
                    text = "Mau kemana",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold

                )

                Text(
                    text = "hari ini?",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Wisata",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .padding(top = 8.dp)
        )

        // Categories
        val categories = listOf("Gunung", "Pantai", "Budaya")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            items(categories) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        when (category) {
                            "Gunung" -> navController.navigate("gunung_page")
                            "Pantai" -> navController.navigate("pantai_page")
                            "Budaya" -> navController.navigate("budaya_page")
                        }
                    }
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_sementara),
                        contentDescription = category,
                        modifier = Modifier.size(64.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = category, fontSize = 14.sp)
                }
            }
        }

        // Categories
        val categorie = listOf("Edukasi", "Kuliner", "Hiburan")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            items(categorie) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            when (category) {
                                "Edukasi" -> navController.navigate("edukasi_page")
                                "Kuliner" -> navController.navigate("kuliner_page")
                                "Hiburan" -> navController.navigate("hiburan_page")
                            }
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_sementara),
                        contentDescription = category,
                        modifier = Modifier
                            .size(64.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = category, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Rekomendasi Wisata",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .padding(top = 8.dp)
        )

        // Recommendation Grid
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.destinasi1),
                    contentDescription = "destinasi wisata 1",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Image(
                    painter = painterResource(R.drawable.home1),
                    contentDescription = "destinasi wisata 2",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.destinasi2),
                    contentDescription = "destinasi wisata 3",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Image(
                    painter = painterResource(R.drawable.tugu_welcome_page),
                    contentDescription = "destinasi wisata 4",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

data class Gunung(val id: String, val name: String, val distance: String, val imageRes: Int, val description: String, val location: String)

@Composable
fun gunungPage(navController: NavController, modifier: Modifier = Modifier){
    val daftarGunung = remember {
        listOf(
            Gunung("merapi", "Gunung Merapi", "3km to city", R.drawable.merapi, "Gunung Merapi adalah gunung berapi paling aktif di Indonesia dan telah meletus secara teratur sejak 1548. Terletak di perbatasan antara Jawa Tengah dan Yogyakarta, gunung ini sangat penting bagi orang Jawa, yang percaya bahwa itu adalah tempat kediaman dewa.", "Sleman, Yogyakarta"),
            Gunung("merbabu", "Gunung Merbabu", "5km to city", R.drawable.merbabu, "Gunung Merbabu adalah gunung berapi stratovolcano di Jawa Tengah. Namanya secara harfiah berarti 'Gunung Abu'. Pemandangan dari puncaknya sangat indah, terutama saat matahari terbit, dengan pemandangan Gunung Merapi di dekatnya.", "Boyolali, Jawa Tengah"),
            Gunung("andong", "Gunung Andong", "9km to city", R.drawable.merbabu, "Gunung Andong adalah gunung yang ramah bagi pendaki pemula. Dengan ketinggian sekitar 1.726 mdpl, puncaknya menawarkan pemandangan 360 derajat yang menakjubkan dari gunung-gunung sekitarnya.", "Magelang, Jawa Tengah"), // Assuming merbabu drawable is a placeholder
            Gunung("kalitalang", "Gunung Kalitalang", "9km to city", R.drawable.destinasi2, "Kalitalang adalah sebuah desa wisata di lereng Gunung Merapi yang menawarkan pemandangan alam yang indah dan udara sejuk. Tempat ini menjadi populer sebagai spot foto dengan latar belakang gagahnya Gunung Merapi.", "Klaten, Jawa Tengah"),
            Gunung("lawu", "Gunung Lawu", "9km to city", R.drawable.destinasi2, "Gunung Lawu terletak di perbatasan Jawa Tengah dan Jawa Timur. Gunung ini memiliki signifikansi historis dan spiritual, dengan beberapa candi (candi) di lerengnya. Puncaknya adalah Hargo Dumilah.", "Karanganyar, Jawa Tengah"),
            Gunung("sumbing", "Gunung Sumbing", "9km to city", R.drawable.merbabu, "Gunung Sumbing adalah gunung tertinggi ketiga di Jawa, setelah Gunung Semeru dan Gunung Slamet. Berdampingan dengan Gunung Sindoro, keduanya sering disebut sebagai 'gunung kembar'.", "Temanggung, Jawa Tengah") // Assuming merbabu drawable is a placeholder
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Return"
                )
            }
            Text(
                text = "Wisata Gunung",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(daftarGunung) { gunung ->
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .shadow(4.dp, RoundedCornerShape(8.dp))
                        .clickable {
                            navController.navigate("detail_gunung/${gunung.id}")
                        }
                ) {
                    Image(
                        painter = painterResource(id = gunung.imageRes),
                        contentDescription = gunung.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    )
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(
                            text = gunung.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = gunung.distance,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun detailGunung(navController: NavController, gunungId: String?) {
    // This is a simple way to get the list of mountains again.
    // In a real app, you might use a ViewModel to provide this data.
    val daftarGunung = remember {
        listOf(
            Gunung("merapi", "Gunung Merapi", "3km to city", R.drawable.merapi, "Gunung Merapi adalah gunung berapi paling aktif di Indonesia dan telah meletus secara teratur sejak 1548. Terletak di perbatasan antara Jawa Tengah dan Yogyakarta, gunung ini sangat penting bagi orang Jawa, yang percaya bahwa itu adalah tempat kediaman dewa.", "Sleman, Yogyakarta"),
            Gunung("merbabu", "Gunung Merbabu", "5km to city", R.drawable.merbabu, "Gunung Merbabu adalah gunung berapi stratovolcano di Jawa Tengah. Namanya secara harfiah berarti 'Gunung Abu'. Pemandangan dari puncaknya sangat indah, terutama saat matahari terbit, dengan pemandangan Gunung Merapi di dekatnya.", "Boyolali, Jawa Tengah"),
            Gunung("andong", "Gunung Andong", "9km to city", R.drawable.merbabu, "Gunung Andong adalah gunung yang ramah bagi pendaki pemula. Dengan ketinggian sekitar 1.726 mdpl, puncaknya menawarkan pemandangan 360 derajat yang menakjubkan dari gunung-gunung sekitarnya.", "Magelang, Jawa Tengah"),
            Gunung("kalitalang", "Gunung Kalitalang", "9km to city", R.drawable.destinasi2, "Kalitalang adalah sebuah desa wisata di lereng Gunung Merapi yang menawarkan pemandangan alam yang indah dan udara sejuk. Tempat ini menjadi populer sebagai spot foto dengan latar belakang gagahnya Gunung Merapi.", "Klaten, Jawa Tengah"),
            Gunung("lawu", "Gunung Lawu", "9km to city", R.drawable.destinasi2, "Gunung Lawu terletak di perbatasan Jawa Tengah dan Jawa Timur. Gunung ini memiliki signifikansi historis dan spiritual, dengan beberapa candi (candi) di lerengnya. Puncaknya adalah Hargo Dumilah.", "Karanganyar, Jawa Tengah"),
            Gunung("sumbing", "Gunung Sumbing", "9km to city", R.drawable.merbabu, "Gunung Sumbing adalah gunung tertinggi ketiga di Jawa, setelah Gunung Semeru dan Gunung Slamet. Berdampingan dengan Gunung Sindoro, keduanya sering disebut sebagai 'gunung kembar'.", "Temanggung, Jawa Tengah")
        )
    }

    val gunung = daftarGunung.find { it.id == gunungId }

    if (gunung == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Gunung tidak ditemukan!")
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Image Header
        item {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                Image(
                    painter = painterResource(id = gunung.imageRes),
                    contentDescription = gunung.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Back button
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
            }
        }

        // Content
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = gunung.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = gunung.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tentang",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = gunung.description, style = MaterialTheme.typography.bodyLarge)
            }
        }

        //Image
        item{
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .height(200.dp), // Set a fixed height for the Row
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = gunung.imageRes),
                    contentDescription = gunung.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                )
                Column(modifier = Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Image(
                        painter = painterResource(id = gunung.imageRes),
                        contentDescription = gunung.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    )
                    Image(
                        painter = painterResource(id = gunung.imageRes),
                        contentDescription = gunung.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(12.dp))                    )
                }
            }
        }
    }
}

data class Pantai(val id: String, val name: String, val distance: String, val imageRes: Int, val description: String, val location: String)

@Composable
fun PantaiPage(navController: NavController, modifier: Modifier = Modifier) {
    val daftarPantai = remember {
        listOf(
            Pantai("parangtritis", "Pantai Parangtritis", "27km to city", R.drawable.pantai1, "Pantai Parangtritis adalah salah satu pantai paling terkenal di Yogyakarta, dikenal dengan pemandangan matahari terbenamnya yang memukau, gumuk pasir, dan legenda Nyi Roro Kidul.", "Bantul, Yogyakarta"),
            Pantai("indrayanti", "Pantai Indrayanti", "66km to city", R.drawable.pantai2, "Dikenal dengan pasir putihnya yang bersih dan air laut yang jernih, Pantai Indrayanti menawarkan berbagai fasilitas modern seperti restoran dan watersport.", "Gunungkidul, Yogyakarta"),
            Pantai("baron", "Pantai Baron", "65km to city", R.drawable.pantai3, "Pantai Baron terkenal dengan sungai bawah tanah yang bertemu langsung dengan laut, menciptakan fenomena alam yang unik. Terdapat juga pasar ikan segar di pantai ini.", "Gunungkidul, Yogyakarta"),
            Pantai("kukup", "Pantai Kukup", "65km to city", R.drawable.pantai1, "Pantai Kukup memiliki pulau karang kecil yang bisa dijangkau dengan jembatan, menawarkan pemandangan laut yang luas dari atas.", "Gunungkidul, Yogyakarta"),
            Pantai("krakal", "Pantai Krakal", "66km to city", R.drawable.pantai2, "Pantai Krakal adalah surga bagi para peselancar dengan ombaknya yang menantang dan garis pantai yang panjang.", "Gunungkidul, Yogyakarta"),
            Pantai("drini", "Pantai Drini", "60km to city", R.drawable.pantai3, "Pantai Drini adalah pantai unik yang dipisahkan oleh sebuah pulau kecil, sehingga memiliki dua sisi pantai dengan karakteristik ombak yang berbeda.", "Gunungkidul, Yogyakarta")
        )
    }
    Column(
        modifier = modifier
        .fillMaxSize()
        .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ){
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Return"
                )
            }

            Text(
                text = "Wisata Pantai",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(daftarPantai) { pantai ->
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .shadow(4.dp, RoundedCornerShape(8.dp))
                        .clickable{
                            navController.navigate("detail_pantai/${pantai.id}")
                        }
                ) {
                    Image(
                        painter = painterResource(id = pantai.imageRes),
                        contentDescription = pantai.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp) // Adjusted height
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    )
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = pantai.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun detailPantai(navController: NavController, pantaiId: String?) {
    // This is a simple way to get the list of beaches again.
    // In a real app, you might use a ViewModel to provide this data.
    val daftarPantai = remember {
        listOf(
            Pantai("parangtritis", "Pantai Parangtritis", "27km to city", R.drawable.pantai1, "Pantai Parangtritis adalah salah satu pantai paling terkenal di Yogyakarta, dikenal dengan pemandangan matahari terbenamnya yang memukau, gumuk pasir, dan legenda Nyi Roro Kidul.", "Bantul, Yogyakarta"),
            Pantai("indrayanti", "Pantai Indrayanti", "66km to city", R.drawable.pantai2, "Dikenal dengan pasir putihnya yang bersih dan air laut yang jernih, Pantai Indrayanti menawarkan berbagai fasilitas modern seperti restoran dan watersport.", "Gunungkidul, Yogyakarta"),
            Pantai("baron", "Pantai Baron", "65km to city", R.drawable.pantai3, "Pantai Baron terkenal dengan sungai bawah tanah yang bertemu langsung dengan laut, menciptakan fenomena alam yang unik. Terdapat juga pasar ikan segar di pantai ini.", "Gunungkidul, Yogyakarta"),
            Pantai("kukup", "Pantai Kukup", "65km to city", R.drawable.pantai1, "Pantai Kukup memiliki pulau karang kecil yang bisa dijangkau dengan jembatan, menawarkan pemandangan laut yang luas dari atas.", "Gunungkidul, Yogyakarta"),
            Pantai("krakal", "Pantai Krakal", "66km to city", R.drawable.pantai2, "Pantai Krakal adalah surga bagi para peselancar dengan ombaknya yang menantang dan garis pantai yang panjang.", "Gunungkidul, Yogyakarta"),
            Pantai("drini", "Pantai Drini", "60km to city", R.drawable.pantai3, "Pantai Drini adalah pantai unik yang dipisahkan oleh sebuah pulau kecil, sehingga memiliki dua sisi pantai dengan karakteristik ombak yang berbeda.", "Gunungkidul, Yogyakarta")
        )
    }
    val pantai = daftarPantai.find { it.id == pantaiId }

    if (pantai == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Pantai tidak ditemukan!")
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Image Header
        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)) {
                Image(
                    painter = painterResource(id = pantai.imageRes),
                    contentDescription = pantai.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Back button
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
            }
        }

        // Content
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = pantai.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = pantai.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tentang",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = pantai.description, style = MaterialTheme.typography.bodyLarge)
            }
        }

        //Image
        item{
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .height(200.dp), // Set a fixed height for the Row
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = pantai.imageRes),
                    contentDescription = pantai.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                )
                Column(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.pantai2), // Placeholder image
                        contentDescription = pantai.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Image(
                        painter = painterResource(id = R.drawable.pantai3), // Placeholder image
                        contentDescription = pantai.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(12.dp))                    )
                }
            }
        }
    }
}

//@Composable
//fun BudayaPage(navController: NavController, modifier: Modifier = Modifier) {
//    val daftarBudaya = remember {
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//            modifier = Modifier.padding(horizontal = 8.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .clip(RoundedCornerShape(8.dp))
//            ) {
//                Column {
//                    Image(
//                        painter = painterResource(id = R.drawable.pantai1),
//                        contentDescription = "Pantai Marina",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp) // Adjusted height for the first image
//                    )
//                }
//
//                Spacer(
//                    modifier = Modifier.height(15.dp)
//                )
//
//                Column {
//                    Image(
//                        painter = painterResource(id = R.drawable.pantai2),
//                        contentDescription = "Pantai Indah",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp)
//                    )
//                }
//
//                Spacer(
//                    modifier = Modifier.height(15.dp)
//                )
//
//                Column {
//                    Image(
//                        painter = painterResource(id = R.drawable.pantai3),
//                        contentDescription = "Pantai Baron",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
//                }
//
//                Spacer(
//                    modifier = Modifier.height(15.dp)
//                )
//
//                Column {
//                    Image(
//                        painter = painterResource(id = R.drawable.pantai1),
//                        contentDescription = "Pantai Baron",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
//                }
//
//                Spacer(
//                    modifier = Modifier.height(15.dp)
//                )
//
//                Column {
//                    Row {
//                        Image(
//                            painter = painterResource(id = R.drawable.pantai2),
//                            contentDescription = "Pantai Baron",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(120.dp)
//                                .clip(RoundedCornerShape(8.dp))
//                        )
//                    }
//
//                    Row {
//                        Image(
//                            painter = painterResource(id = R.drawable.pantai3),
//                            contentDescription = "Pantai Baron",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(120.dp)
//                                .clip(RoundedCornerShape(8.dp))
//                        )
//                    }
//                }
//            }
//
//        }
//    }
//
//    Column(modifier = modifier.fillMaxSize().padding(8.dp)) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(bottom = 16.dp)
//        ) {
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "Return"
//                )
//            }
//            Text(
//                text = "Wisata Gunung",
//                style = MaterialTheme.typography.headlineMedium
//            )
//        }
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
//            items(daftarBudaya.size) { index ->
//                val budaya = daftarBudaya[index]
//                Column (
//                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
//                ) {
//                    Image(
//                        painter = painterResource(id = budaya.imageRes),
//                        contentDescription = budaya.name,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp)
//                    )
//                    Column(modifier = Modifier.padding(8.dp)) {
//                        Text(text = budaya.name, fontWeight = FontWeight.Bold)
//                        Text(text = budaya.distance, style = MaterialTheme.typography.bodySmall)
//                    }
//                }
//            }
//        }
//    }
//    Column(modifier = modifier.fillMaxSize().padding(8.dp)) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(bottom = 16.dp)
//        ) {
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "Return"
//                )
//            }
//            Text(
//                text = "Wisata Gunung",
//                style = MaterialTheme.typography.headlineMedium
//            )
//        }
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
//            items(daftarEdukasi.size) { index ->
//                val edukasi = daftarEdukasi[index]
//                Column (
//                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
//                ) {
//                    Image(
//                        painter = painterResource(id = edukasi.imageRes),
//                        contentDescription = edukasi.name,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp)
//                    )
//                    Column(modifier = Modifier.padding(8.dp)) {
//                        Text(text = edukasi.name, fontWeight = FontWeight.Bold)
//                        Text(text = edukasi.distance, style = MaterialTheme.typography.bodySmall)
//                    }
//                }
//            }
//        }
//    }
//    Column(modifier = modifier.fillMaxSize().padding(8.dp)) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(bottom = 16.dp)
//        ) {
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "Return"
//                )
//            }
//            Text(
//                text = "Wisata Gunung",
//                style = MaterialTheme.typography.headlineMedium
//            )
//        }
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
//            items(daftarKuliner.size) { index ->
//                val kuliner = daftarKuliner[index]
//                Column (
//                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
//                ) {
//                    Image(
//                        painter = painterResource(id = kuliner.imageRes),
//                        contentDescription = kuliner.name,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp)
//                    )
//                    Column(modifier = Modifier.padding(8.dp)) {
//                        Text(text = kuliner.name, fontWeight = FontWeight.Bold)
//                        Text(text = kuliner.distance, style = MaterialTheme.typography.bodySmall)
//                    }
//                }
//            }
//        }
//    }
//
//    Column(modifier = modifier.fillMaxSize().padding(8.dp)) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(bottom = 16.dp)
//        ) {
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "Return"
//                )
//            }
//            Text(
//                text = "Wisata Gunung",
//                style = MaterialTheme.typography.headlineMedium
//            )
//        }
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
//            items(daftarHiburan.size) { index ->
//                val hiburan = daftarHiburan[index]
//                Column (
//                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
//                ) {
//                    Image(
//                        painter = painterResource(id = hiburan.imageRes),
//                        contentDescription = hiburan.name,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp)
//                    )
//                    Column(modifier = Modifier.padding(8.dp)) {
//                        Text(text = hiburan.name, fontWeight = FontWeight.Bold)
//                        Text(text = hiburan.distance, style = MaterialTheme.typography.bodySmall)
//                    }
//                }
//            }
//        }
//    }
//}
//}
