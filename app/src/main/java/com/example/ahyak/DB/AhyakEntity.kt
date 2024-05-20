package com.example.ahyak.DB

import androidx.room.ColumnInfo
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
    val Prescription: String,
    val Month: Int,
    val Day: Int,
    val Slot: String,
    val Hospital: String,
    val Start_Date: String,
    val End_Date : String
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}


@Entity(tableName = "MedicineTable")
data class MedicineEntity(
    val MedicineName : String,
    val PrescriptionName: String,
    val MedicineMonth: Int,
    val MedicineDay: Int,
    val MedicineSlot: String,
    val MedicineVolume:Float,
    val MedicineType : String,
    var MedicineTake : Boolean,
    val FreeRegister : Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}

@Entity(tableName = "ExtraPillTable")
data class ExtraPillEntity(
    val PillName : String,
    val PillMonth : Int,
    val PillDay : Int,
    val PillSlot : String,
    val PillVolume : String,
    val PillType : String,
    val PillTime : String
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}

@Entity(tableName = "TodayRecordContentTable")
data class TodayRecordEntity(
    val RecordContent : String,
    val RecordMonth : Int,
    val RecordDay : Int
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}

@Entity(tableName = "TodayRecordSymptomTable")
data class TodayRecordSymptomEntity(
    val SymptomName : String,
    val SymptomStrength : Int,
    val RecordSymptomMonth : Int,
    val RecordSymptomDay : Int
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}

@Entity(tableName = "FreeMedicineTable")
data class FreeMedicineEntity(
    @PrimaryKey
    val FreeMedicineName : String,
    val FreeMedicineCode : String,
    val FreeMedicineShape : String,
    val FreeMedicineColor : String,
    val FreeMedicineType : String,
    val FreeMedicineLine : String
)