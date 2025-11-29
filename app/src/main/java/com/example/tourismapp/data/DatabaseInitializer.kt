package com.example.tourismapp.data

import android.content.Context
import com.example.tourismapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseInitializer {
    fun insertInitialData(context: Context) {
        val db = AppDatabase.getDatabase(context)

        CoroutineScope(Dispatchers.IO).launch {
            // Cegah double insert
            // GUNUNG
            if (db.gunungDao().getAll().isEmpty()) {
                db.gunungDao().insertAll(
                    listOf(
                        GunungEntity(

                            name = "Gunung Merapi",
                            distance = "3km to city",
                            imageRes = R.drawable.merapi
                        ),
                        GunungEntity(
                            name = "Gunung Merbabu",
                            distance = "5km to city",
                            imageRes = R.drawable.merbabu
                        ),
                        GunungEntity(
                            name = "Gunung Andong",
                            distance = "9km to city",
                            imageRes = R.drawable.merbabu
                        ),
                        GunungEntity(
                            name = "Gunung Kalitalang",
                            distance = "9km to city",
                            imageRes = R.drawable.destinasi2
                        ),
                        GunungEntity(
                            name = "Gunung Lawu",
                            distance = "9km to city",
                            imageRes = R.drawable.destinasi2
                        ),
                        GunungEntity(
                            name = "Gunung Sumbing",
                            distance = "9km to city",
                            imageRes = R.drawable.merbabu
                        )
                    )
                )
            }

            // EDUKASI
            if (db.edukasiDao().getAll().isEmpty()) {
                db.edukasiDao().insertAll(
                    listOf(
                        EdukasiEntity(
                            name = "Taman Pintar Yogyakarta",
                            description = "Pusat sains interaktif anak-anak",
                            imageRes = R.drawable.taman_pintar
                        ),
                        EdukasiEntity(
                            name = "Museum Benteng Vredeburg",
                            description = "Sejarah perjuangan bangsa",
                            imageRes = R.drawable.vredeburg
                        ),
                        EdukasiEntity(
                            name = "Museum Sonobudoyo",
                            description = "Budaya Jawa klasik",
                          imageRes =   R.drawable.sonobudoyo
                        ),
                        EdukasiEntity(
                           name =  "Desa Wisata Krebet",
                            description = "Belajar membatik kayu",
                        imageRes =        R.drawable.krebet
                        ),
                        EdukasiEntity(
                       name =      "Kasongan",
                           description =  "Gerabah & keramik tradisional",
                      imageRes =       R.drawable.kasongan
                        ),
                        EdukasiEntity(
                        name =     "Kebun Buah Langka Sedayu",
                         description =    "Wisata agrikultur",
                         imageRes =    R.drawable.sedayu
                        )
                    )
                )
            }

            // HIBURAN
            if (db.hiburanDao().getAll().isEmpty()) {
                db.hiburanDao().insertAll(
                    listOf(
                        HiburanEntity(
                            name = "Sindu Kusuma Edupark",
                            description = "Taman bermain keluarga",
                            imageRes = R.drawable.ske
                        ),
                        HiburanEntity(
                            name = "HeHa Sky View",
                            description = "Spot selfie & restoran",
                            imageRes = R.drawable.icon_sementara
                        ),
                        HiburanEntity(
                           name = "Jogja Bay Waterpark",
                            description =   "Wahana air terbesar",
                            imageRes = R.drawable.icon_sementara
                        ),
                        HiburanEntity(
                            name = "Obelix Hills",
                            description = "Sunset & kuliner",
                            imageRes = R.drawable.obelix
                        ),
                        HiburanEntity(
                            name = "Tebing Breksi",
                            description =   "Wisata alam & pertunjukan terbuka",
                            imageRes = R.drawable.icon_sementara
                        ),
                        HiburanEntity(
                            name =  "HeHa Ocean View",
                            description =  "View laut & foto estetik",
                            imageRes = R.drawable.icon_sementara
                        )
                    )
                )
            }
            // KULINER
            if (db.kulinerDao().getAll().isEmpty()) {
                db.kulinerDao().insertAll(
                    listOf(
                        KulinerEntity(
                            name = "Nama Makanan 1",
                            description = "Khas daerah...",
                            imageRes = R.drawable.icon_sementara
                        ),
                        KulinerEntity(
                            name = "Nama Makanan 2",
                            description = "Khas daerah...",
                            imageRes = R.drawable.icon_sementara
                        ),
                        KulinerEntity(
                            name = "Nama Makanan 3",
                            description = "Khas daerah...",
                            imageRes = R.drawable.icon_sementara
                        ),
                        KulinerEntity(
                            name = "Nama Makanan 4",
                            description = "Khas daerah...",
                            imageRes =R.drawable.icon_sementara)
                    )
                )
            }

// BUDAYA
            if (db.budayaDao().getAll().isEmpty()) {
                db.budayaDao().insertAll(
                    listOf(
                        BudayaEntity(
                            name = "Nama Budaya 1",
                            description = "Deskripsi singkat...",
                            imageRes = R.drawable.icon_sementara
                        ),
                        BudayaEntity(
                            name = "Nama Budaya 2",
                            description = "Deskripsi singkat...",
                            imageRes = R.drawable.icon_sementara
                        ),
                        BudayaEntity(
                            name = "Nama Budaya 3",
                            description = "Deskripsi singkat...",
                            imageRes = R.drawable.icon_sementara
                        ),
                        BudayaEntity(
                            name = "Nama Budaya 4",
                            description = "Deskripsi singkat...",
                            imageRes = R.drawable.icon_sementara
                        )
                    )
                )
            }

//          PANTAI
            if (db.pantaiDao().getAll().isEmpty()) {
                db.pantaiDao().insertAll(
                    listOf(
                        PantaiEntity(
                            name = "Nama Pantai 1",
                            description = "Pantai yang indah",
                            imageRes = R.drawable.icon_sementara),
                        PantaiEntity(
                            name = "Nama Pantai 2",
                            description = "Pantai yang indah",
                            imageRes = R.drawable.icon_sementara),
                        PantaiEntity(
                            name = "Nama Pantai 3",
                            description ="Pantai yang indah",
                            imageRes = R.drawable.icon_sementara),
                        PantaiEntity(
                            name = "Nama Pantai 4",
                            description ="Pantai yang indah",
                            imageRes = R.drawable.icon_sementara),
                        PantaiEntity(
                            name = "Nama Pantai 5",
                            description ="Pantai yang indah",
                            imageRes = R.drawable.icon_sementara),
                        PantaiEntity(
                            name = "Nama Pantai 6",
                            description ="Pantai yang indah",
                            imageRes = R.drawable.icon_sementara)
                    )
                )
            }
        }
    }
}
