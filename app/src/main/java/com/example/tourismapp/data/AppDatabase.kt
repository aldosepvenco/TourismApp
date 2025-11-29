package com.example.tourismapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        GunungEntity::class,
        EdukasiEntity::class,
        HiburanEntity::class,
        PantaiEntity::class,
        BudayaEntity::class,
        KulinerEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gunungDao(): GunungDao
    abstract fun edukasiDao(): EdukasiDao
    abstract fun hiburanDao(): HiburanDao
    abstract fun pantaiDao(): PantaiDao
    abstract fun budayaDao(): BudayaDao
    abstract fun kulinerDao(): KulinerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wisata_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
