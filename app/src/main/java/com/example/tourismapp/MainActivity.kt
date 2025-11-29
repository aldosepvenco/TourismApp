package com.example.tourismapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tourismapp.ui.theme.TourismAppTheme
import com.example.tourismapp.ui.WelcomeScreen
import com.example.tourismapp.data.DatabaseInitializer
import com.example.tourismapp.ui.BudayaPage
import com.example.tourismapp.ui.EdukasiPage
import com.example.tourismapp.ui.GunungPage
import com.example.tourismapp.ui.HiburanPage
import com.example.tourismapp.ui.HomeScreen
import com.example.tourismapp.ui.KulinerPage
import com.example.tourismapp.ui.PantaiPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Insert default data ke database
        DatabaseInitializer.insertInitialData(this)

        setContent {
            TourismApp()
        }
    }
}

@Composable
fun TourismApp() {
    TourismAppTheme {

        Scaffold { innerPadding ->
            val navController = rememberNavController()

            // Untuk menentukan apakah welcome screen masih tampil atau tidak
            var showWelcomeScreen by rememberSaveable { mutableStateOf(true) }

            NavHost(
                navController = navController,
                startDestination = if (showWelcomeScreen) "welcome" else "home",
                modifier = androidx.compose.ui.Modifier.padding(innerPadding)
            ) {

                // Welcome Page
                composable("welcome") {
                    WelcomeScreen(
                        onGetStartedClick = {
                            showWelcomeScreen = false
                            navController.navigate("home") {
                                popUpTo("welcome") { inclusive = true }
                            }
                        }
                    )
                }

                // Home Page
                composable("home") {
                    HomeScreen(navController)
                }

                // Semua halaman kategori
                composable("gunung_page") { GunungPage(navController) }
                composable("pantai_page") { PantaiPage(navController) }
                composable("edukasi_page") { EdukasiPage(navController) }
                composable("hiburan_page") { HiburanPage(navController) }
                composable("budaya_page") { BudayaPage(navController) }
                composable("kuliner_page") { KulinerPage(navController) }
            }
        }
    }
}
