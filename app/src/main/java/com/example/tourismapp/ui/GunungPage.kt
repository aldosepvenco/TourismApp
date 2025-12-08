package com.example.tourismapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.tourismapp.data.AppDatabase
import com.example.tourismapp.data.GunungEntity
import com.example.tourismapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun GunungPage(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val gunungDao = db.gunungDao()

    var daftarGunung by remember { mutableStateOf<List<GunungEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        daftarGunung = withContext(Dispatchers.IO) {
            gunungDao.getAll()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ============= HEADER =============
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
                text = "Wisata Gunung",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // ============= LIST GUNUNG =============
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(daftarGunung) { gunung ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("detail/gunung/${gunung.id}")
                        },
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {

                        // ==========================
                        // GAMBAR ANTI FORCE CLOSE
                        // ==========================
                        val safeImage = try {
                            if (gunung.imageRes != 0) gunung.imageRes else R.drawable.icon_sementara
                        } catch (e: Exception) {
                            R.drawable.icon_sementara
                        }

                        Image(
                            painter = painterResource(id = safeImage),
                            contentDescription = gunung.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = gunung.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = gunung.distance,
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}
