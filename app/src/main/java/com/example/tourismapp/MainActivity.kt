package com.example.tourismapp

import android.R.attr.text
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tourismapp.ui.theme.TourismAppTheme
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

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

data class Gunung(val name: String, val distance: String, val imageRes: Int)
//data class Pantai(val name: String, val imageRes: Int)

@Composable
fun gunungPage(navController: NavController, modifier: Modifier = Modifier){
    val daftarGunung = remember {
        listOf(
            Gunung("Gunung Merapi", "3km to city", R.drawable.merapi),
            Gunung("Gunung Merbabu", "5km to city", R.drawable.merbabu),
            Gunung("Gunung Andong", "9km to city", R.drawable.merbabu), // Assuming merbabu drawable is a placeholder
            Gunung("Gunung Kalitalang", "9km to city", R.drawable.destinasi2),
            Gunung("Gunung Lawu", "9km to city", R.drawable.destinasi2),
            Gunung("Gunung Sumbing", "9km to city", R.drawable.merbabu) // Assuming merbabu drawable is a placeholder
        )
    }

    Column(modifier = modifier.fillMaxSize().padding(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
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
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(daftarGunung.size) { index ->
                val gunung = daftarGunung[index]
                Column(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = gunung.imageRes),
                        contentDescription = gunung.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = gunung.name, fontWeight = FontWeight.Bold)
                        Text(text = gunung.distance, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

data class Pantai(val name: String, val imageRes: Int)

@Composable
fun PantaiPage(navController: NavController, modifier: Modifier = Modifier) {
    val daftarPantai = remember {
        listOf(
            Pantai("Pantai Marina", R.drawable.pantai1),
            Pantai("Pantai Indah", R.drawable.pantai2),
            Pantai("Pantai Baron", R.drawable.pantai3),
            Pantai("Pantai Kukup", R.drawable.pantai1),
            Pantai("Pantai Krakal", R.drawable.pantai2),
            Pantai("Pantai Drini", R.drawable.pantai3)
        )
    }

    Column(modifier = modifier.fillMaxSize().padding(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
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
                modifier = Modifier.fillMaxWidth()
                    .padding(end = 48.dp) // Adjust padding to center title with the back button
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(daftarPantai) { pantai ->
                Column(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = pantai.imageRes),
                        contentDescription = pantai.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Text(
                        text = pantai.name,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
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
