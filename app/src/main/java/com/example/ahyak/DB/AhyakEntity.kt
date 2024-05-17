package com.example.ahyak.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AhyakTable" )
data class AhyakEntity(
    val searchsympotmName : String
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}

@Entity(tableName = "PrescriptionTable")
data class PrescriptionEntity(
    @PrimaryKey
    val Prescription: String,
    val Month: Int,
    val Day: Int,
    val Slot: String,
    val Hospital: String,
    val Start_Date: String,
    val End_Date : String
)

@Entity(tableName = "MedicineTable")
data class MedicineEntity(
    @PrimaryKey
    val MedicineName : String,
    val PrescriptionName: String,
    val MedicineMonth: Int,
    val MedicineDay: Int,
    val MedicineSlot: String,
    val MedicineVolume:Float,
    val MedicineType : String,
    val MedicineTake : Boolean,
    val FreeRegister : Boolean
)

@Entity(tableName = "ExtraPillTable")
data class ExtraPillEntity(
    val PillName : String,
    val PillMonth : Int,
    val PillDay : Int,
    val PillSlot : String,
    val PillVolume : Float,
    val PillType : String,
    val PillTime : String
)

