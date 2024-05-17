package com.example.ahyak.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

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

    @Query("SELECT * FROM PrescriptionTable")
    fun getAllPrescriptions(): List<PrescriptionEntity>

    @Query("SELECT * FROM PrescriptionTable WHERE Month = :month AND Day = :day AND Slot = :slot")
    fun getPrescription(month: Int?, day: Int?, slot: String?): MutableList<PrescriptionEntity>

    @Query("DELETE FROM PrescriptionTable WHERE Prescription = :prescription")
    fun deletePrescription(prescription: String): Int

    @Query("DELETE FROM PrescriptionTable")
    fun deleteAllPrescriptions(): Int

}
@Dao
interface MedicineDao {

    @Insert
    fun insertMedicine(medicine: MedicineEntity)

    @Query("SELECT * FROM MedicineTable WHERE MedicineMonth = :month AND MedicineDay = :day AND MedicineSlot = :slot AND PrescriptionName = :prescription" )
    fun getMedicine(month: Int?, day: Int?, slot: String?, prescription: String?): List<MedicineEntity>
}

@Dao
interface ExtraPillDao{
    @Insert
    fun insertPill(pill: ExtraPillEntity)

    @Query("SELECT * FROM ExtraPillTable WHERE PillMonth = :month AND PillDay = :day AND PillSlot = :slot")
    fun getPill(month: Int?, day: Int?, slot: String?): List<ExtraPillEntity>


}



