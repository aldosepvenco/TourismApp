package com.example.tourismapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GunungDao {
    @Query("SELECT * FROM gunung")
    suspend fun getAll(): List<GunungEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<GunungEntity>)
}

@Dao
interface EdukasiDao {
    @Query("SELECT * FROM edukasi")
    suspend fun getAll(): List<EdukasiEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<EdukasiEntity>)
}

@Dao
interface HiburanDao {
    @Query("SELECT * FROM hiburan")
    suspend fun getAll(): List<HiburanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<HiburanEntity>)
}

@Dao
interface PantaiDao {
    @Query("SELECT * FROM pantai")
    suspend fun getAll(): List<PantaiEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PantaiEntity>)
}

@Dao
interface BudayaDao {
    @Query("SELECT * FROM budaya")
    suspend fun getAll(): List<BudayaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<BudayaEntity>)
}

@Dao
interface KulinerDao {
    @Query("SELECT * FROM kuliner")
    suspend fun getAll(): List<KulinerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<KulinerEntity>)
}
