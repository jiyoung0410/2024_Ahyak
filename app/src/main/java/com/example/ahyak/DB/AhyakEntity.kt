package com.example.ahyak.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//API 연동
data class MessageResponse(
    @SerializedName("message") val message : String
)

data class SignupRequest(
    @SerializedName("nickName") val nickname : String,
    @SerializedName("email") val email : String
)

data class LoginResponse(
    @SerializedName("accessToken") val accessToken : String,
    @SerializedName("refreshToken") val refreshToken : String
)

data class RefreshTokenRequest(
    @SerializedName("refreshToken") val refreshToken: String
)


data class RegistPresRequest(
    @SerializedName("name") val name: String,
    @SerializedName("hospital") val hospital: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String
)

data class RegistPresResponse(
    @SerializedName("prescription") val prescription: Prescription
)

data class Prescription(
    @SerializedName("user_id") val user_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("hospital") val hospital: String,
    @SerializedName("start_date") val start_date: String,
    @SerializedName("end_date") val end_date: String,
    @SerializedName("is_Active") val is_Active: Boolean,
    @SerializedName("_id") val _id: String,
    @SerializedName("__v") val __v: Int
)

data class ModifyPresRequest(
    @SerializedName("name") val name: String,
    @SerializedName("hospital") val hospital: String,
    @SerializedName("start_date") val start_date: String,
    @SerializedName("end_date") val end_date: String,
    @SerializedName("prescription_id") val prescriptionId: String,
)

//DailyStatus
// 조회(Request) - DailyStatus
data class DailyStatusResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val dailyStatus: DailyStatus
)

data class DailyStatus(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("date") val date: String,
    @SerializedName("discomforts") val discomforts: List<Discomfort>,
    @SerializedName("additional_info") val additionalInfo: String,
    @SerializedName("__v") val version: Int
)

data class Discomfort(
    @SerializedName("description") val description: String,
    @SerializedName("severity") val severity: Int,
    @SerializedName("_id") val id: String
)

data class AdditionMedRegiRequest(
    @SerializedName("name") val name: String,
    @SerializedName("dose") val dose: String,
    @SerializedName("unit") val unit: String,
    @SerializedName("date") val date: String
)

data class AdditionMedDataWrapper <T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T
)

data class AdditionMedRegiResponse(
    @SerializedName("user_id") val user_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("dose") val dose: String,
    @SerializedName("unit") val unit: String,
    @SerializedName("takenTime") val takenTime: String,
    @SerializedName("is_Active") val is_Active: Boolean,
    @SerializedName("_id") val _id: String,
    @SerializedName("__v") val __v: Int
)

data class GetAddMedResponse(
    @SerializedName("_id") val _id: String,
    @SerializedName("name") val name: String,
    @SerializedName("dose") val dose: String,
    @SerializedName("unit") val unit: String,
    @SerializedName("takenTime") val takenTime: String
)

//등록(Response) - DailyStatus
data class DiscomfortRequest(
    @SerializedName("description") val description: String,
    @SerializedName("severity") val severity: Int
)

data class SymptomRequest(
    @SerializedName("date") val date: String,
    @SerializedName("discomforts") val discomforts: List<DiscomfortRequest>,
    @SerializedName("additionalInfo") val additionalInfo: String
)

//Medicine > 약 정보 조회(모양으로 저장한 약)
data class MedicineResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data")   val data: MedicineData
)

data class MedicineData(
    @SerializedName("medicine") val medicine: List<Medicine>
)

//Medicine-약 추가하기(모양으로 약 추가)
// 1) POST 요청 바디용 DTO (요청할 때 이걸 씁니다)
data class PostMedicineRequest(
    @SerializedName("name")  val name: String,
    @SerializedName("print") val print: String,
    @SerializedName("shape") val shape: String,
    @SerializedName("color") val color: String,
    @SerializedName("type")  val type: String,
    @SerializedName("line")  val line: String
)

// 2) 응답 전체를 감쌀 최상위 DTO
data class PostMedicineResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data")   val wrapper: PostMedicineWrapper
)

// 3) 첫 번째 "data" 객체
data class PostMedicineWrapper(
    @SerializedName("status") val status: String,
    @SerializedName("data")   val medicine: Medicine
)

// 4) 실제 약 정보 객체 (seq 필드도 포함)
data class Medicine(
    @SerializedName("seq")       val seq: String,
    @SerializedName("name")      val name: String,
    @SerializedName("print")     val print: String,
    @SerializedName("shape")     val shape: String,
    @SerializedName("color")     val color: String,
    @SerializedName("type")      val type: String,
    @SerializedName("line")      val line: String,
    @SerializedName("tokenized") val tokenized: String,
    @SerializedName("_id")       val id: String,
    @SerializedName("__v")       val version: Int
)


//RoomDB
@Entity(tableName = "AhyakTable" )
data class AhyakEntity(
    val searchsympotmName : String
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}

@Entity(tableName = "PrescriptionTable")
data class PrescriptionEntity(
    val PrescriptionId: String,
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

