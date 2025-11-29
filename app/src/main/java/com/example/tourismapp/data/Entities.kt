package com.example.tourismapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// GUNUNG
@Entity(tableName = "gunung")
data class GunungEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val distance: String,
    val imageRes: Int
)

// EDUKASI
@Entity(tableName = "edukasi")
data class EdukasiEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val imageRes: Int
)

// HIBURAN
@Entity(tableName = "hiburan")
data class HiburanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val imageRes: Int
)

// PANTAI
@Entity(tableName = "pantai")
data class PantaiEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val imageRes: Int
)

//  BUDAYA
@Entity(tableName = "budaya")
data class BudayaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val imageRes: Int
)
//  KULINER
@Entity(tableName = "kuliner")
data class KulinerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val imageRes: Int
)
