package com.example.tourismapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tourismapp.ui.theme.TourismAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TourismAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomeScreen(
                        onGetStartedClick = { /*TODO*/ },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(onGetStartedClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        // Background image full-screen
        Image(
            painter = painterResource(R.drawable.tugu_welcome),
            contentDescription = "Background Tugu",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp)) // ruang atas supaya logo tidak terlalu menempel statusbar
            // jika punya icon logo, aktifkan Image di bawah:
            // Image(painter = painterResource(R.drawable.ic_logo), contentDescription = "logo", modifier = Modifier.size(56.dp))
            Text(
                text = "JogjaGo",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 28.sp
                ),
                modifier = Modifier.padding(top = 8.dp)
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
