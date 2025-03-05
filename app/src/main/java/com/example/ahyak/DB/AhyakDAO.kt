package com.example.ahyak.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

//Entity에 접근할 수 있는 DAO
@Dao
interface AhyakDAO {
    @Insert
    fun add(searchsympotmName:AhyakEntity)

    @Query("SELECT * FROM AhyakTable")
    fun getAllMyStrings() : List<AhyakEntity>
}


@Dao
interface PrescriptionDAO {
    @Insert
    fun insertPrescription(prescription: PrescriptionEntity)

    // 더미 데이터를 한 번에 여러 개 삽입하는 메서드
    @Insert
    fun insertAll(prescriptions: List<PrescriptionEntity>)

    //증상 이름만 가져오는 메서드
    @Query("SELECT Prescription FROM PrescriptionTable")
    fun getAllSymptomNames(): List<String>


    @Query("SELECT * FROM PrescriptionTable")
    fun getAllPrescriptions(): List<PrescriptionEntity>

    @Query("SELECT * FROM PrescriptionTable WHERE Month = :month AND Day = :day AND Slot = :slot")
    fun getPrescription(month: Int?, day: Int?, slot: String?): MutableList<PrescriptionEntity>

    @Query("DELETE FROM PrescriptionTable WHERE Prescription = :prescription")
    fun deletePrescription(prescription: String): Int

    @Query("DELETE FROM PrescriptionTable")
    fun deleteAllPrescriptions(): Int

    //종료일 받아오기
    @Query("SELECT End_Date FROM PrescriptionTable WHERE Prescription = :name")
    fun getPrescriptionEnd_Date(name: String?):String


}
@Dao
interface MedicineDao {

    @Insert
    fun insertMedicine(medicine: MedicineEntity)

    @Query("DELETE FROM  MedicineTable WHERE id = :id")
    fun deleteMedicine(id: Int)

    @Query("DELETE FROM  MedicineTable WHERE PrescriptionName = :prescription")
    fun deletePrescriptionMedicine(prescription: String)

    @Query("SELECT * FROM MedicineTable WHERE MedicineMonth = :month AND MedicineDay = :day AND MedicineSlot = :slot AND PrescriptionName = :prescription" )
    fun getMedicine(month: Int?, day: Int?, slot: String?, prescription: String?): List<MedicineEntity>

    //복용 여부 확인
    @Query("SELECT MedicineTake FROM MedicineTable WHERE id = :id" )
    fun getMedicineTake(id: Int?): Boolean

    //일별 복용 여부 확인
    @Query("SELECT * FROM MedicineTable WHERE MedicineMonth =:month AND MedicineDay = :day")
    fun getMedicineTakeOfDay(month: Int, day: Int): List<MedicineEntity>

    // 복용 여부 업데이트
    @Query("UPDATE MedicineTable SET MedicineTake = :take WHERE id = :id")
    suspend fun updateMedicineTakeStatus(id: Int, take: Boolean)

    //자유기록 여부 확인
    @Query("SELECT FreeRegister FROM MedicineTable WHERE id = :id" )
    fun getMedicineFree(id: Int?): Boolean

}

@Dao
interface ExtraPillDao{
    @Insert
    fun insertPill(pill: ExtraPillEntity)

    @Query("SELECT * FROM ExtraPillTable WHERE PillMonth = :month AND PillDay = :day AND PillSlot = :slot")
    fun getPill(month: Int?, day: Int?, slot: String?): List<ExtraPillEntity>

    @Query("DELETE FROM  ExtraPillTable WHERE PillName = :pillName AND PillDay = :day AND PillSlot = :slot")
    fun deletePill(pillName: String, day: Int, slot: String): Int

}

@Dao
interface TodayRecordDao {
    @Insert
    fun insertTodayRecordContent(todayRecord: TodayRecordEntity)

    @Query("SELECT * FROM todayrecordcontenttable WHERE RecordMonth =:month AND RecordDay = :day")
    fun getTodayRecordContent(month: Int, day: Int): List<TodayRecordEntity>

    @Query("DELETE FROM todayrecordcontenttable")
    fun deleteTodayrecordcontent(): Int

}

@Dao
interface TodayRecordSymptomDao {
    @Insert
    fun insertTodayRecordSymptom(todayRecordSymptom: TodayRecordSymptomEntity)

    @Query("SELECT * FROM todayrecordsymptomtable WHERE RecordSymptomMonth =:month AND RecordSymptomDay = :day")
    fun getTodayRecordSymptom(month: Int, day: Int): List<TodayRecordSymptomEntity>

    @Query("DELETE FROM todayrecordsymptomtable WHERE SymptomName = :name")
    fun deleteTodayRecordSymptom(name: String)
}

@Dao
interface FreeMedicineDao{
    @Insert
    fun insertFreeMedicine(freeMedicine: FreeMedicineEntity)

    @Query("SELECT * FROM freemedicinetable WHERE FreeMedicineName =:name")
    fun getFreeMedicine(name: String):  List<FreeMedicineEntity>
}



