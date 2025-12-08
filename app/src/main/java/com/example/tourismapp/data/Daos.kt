package com.example.tourismapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// =======================
// GUNUNG DAO
// =======================
@Dao
interface GunungDao {

    @Query("SELECT * FROM gunung")
    suspend fun getAll(): List<GunungEntity>

    @Query("SELECT * FROM gunung WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): GunungEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<GunungEntity>)
}


// =======================
// EDUKASI DAO
// =======================
@Dao
interface EdukasiDao {

    @Query("SELECT * FROM edukasi")
    suspend fun getAll(): List<EdukasiEntity>

    @Query("SELECT * FROM edukasi WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): EdukasiEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<EdukasiEntity>)
}


// =======================
// HIBURAN DAO
// =======================
@Dao
interface HiburanDao {

    @Query("SELECT * FROM hiburan")
    suspend fun getAll(): List<HiburanEntity>

    @Query("SELECT * FROM hiburan WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): HiburanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<HiburanEntity>)
}


// =======================
// PANTAI DAO
// =======================
@Dao
interface PantaiDao {

    @Query("SELECT * FROM pantai")
    suspend fun getAll(): List<PantaiEntity>

    @Query("SELECT * FROM pantai WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): PantaiEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PantaiEntity>)
}


// =======================
// BUDAYA DAO
// =======================
@Dao
interface BudayaDao {

    @Query("SELECT * FROM budaya")
    suspend fun getAll(): List<BudayaEntity>

    @Query("SELECT * FROM budaya WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): BudayaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<BudayaEntity>)
}


// =======================
// KULINER DAO
// =======================
@Dao
interface KulinerDao {

    @Query("SELECT * FROM kuliner")
    suspend fun getAll(): List<KulinerEntity>

    @Query("SELECT * FROM kuliner WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): KulinerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<KulinerEntity>)
}
