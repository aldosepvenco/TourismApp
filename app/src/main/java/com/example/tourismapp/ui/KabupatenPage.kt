package com.example.tourismapp.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun KabupatenPage(
    navController: NavController,
    namaKabupaten: String
) {
    val context = LocalContext.current

    // Wisata yang tersedia di masing-masing kabupaten
    val wisataTersedia = mapOf(
        "Sleman"       to listOf("Gunung", "Budaya", "Edukasi", "Kuliner", "Hiburan"),
        "Bantul"       to listOf("Pantai", "Budaya", "Kuliner", "Hiburan"),
        "Gunungkidul"  to listOf("Pantai", "Budaya", "Kuliner", "Hiburan"),
        "Kulon Progo"  to listOf("Gunung", "Pantai", "Budaya", "Kuliner", "Hiburan"),
        "Yogyakarta"   to listOf("Budaya", "Edukasi", "Kuliner", "Hiburan")
    )

    // List menu kategori
    val jenisWisata = listOf(
        "Gunung" to "gunung_page",
        "Pantai" to "pantai_page",
        "Budaya" to "budaya_page",
        "Edukasi" to "edukasi_page",
        "Kuliner" to "kuliner_page",
        "Hiburan" to "hiburan_page"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // HEADER
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Kabupaten $namaKabupaten",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Text(
            text = "Pilih jenis wisata di $namaKabupaten",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(jenisWisata) { (label, route) ->

                JenisWisataItem(
                    label = label,
                    onClick = {
                        val allowed = wisataTersedia[namaKabupaten] ?: emptyList()

                        if (allowed.contains(label)) {
                            // Jika wisata tersedia → navigate ke halaman
                            navController.navigate(route)
                        } else {
                            // Jika tidak tersedia → tampilkan pesan
                            Toast.makeText(
                                context,
                                "Maaf, wisata ini tidak ada di daerah ini.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun JenisWisataItem(
    label: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
