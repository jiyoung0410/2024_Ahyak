package com.example.ahyak.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//Dao를 담고 있는 DataBase 객체
@Database(entities = [AhyakEntity::class, PrescriptionEntity::class, MedicineEntity::class, ExtraPillEntity::class,
    TodayRecordEntity::class, TodayRecordSymptomEntity::class, FreeMedicineEntity::class], version = 15)
abstract class AhyakDataBase : RoomDatabase(){
    //Entity에 접근할 수 있는 Dao들을 가짐
    abstract fun getAhyakDao() : AhyakDAO
    abstract fun getPrescriptionDao(): PrescriptionDAO
    abstract fun getMedicineDao() : MedicineDao
    abstract fun getExtraPillDao() : ExtraPillDao
    abstract fun getTodayRecordDao() : TodayRecordDao
    abstract fun getTodayRecordSymptomDao() : TodayRecordSymptomDao
    abstract fun getFreeMedicineDao() : FreeMedicineDao


    companion object{   //자바의 static이라고 생각하면됨
        var instance : AhyakDataBase? = null
        fun getInstance(context: Context) : AhyakDataBase{
            if(instance ==null){
                instance = Room.databaseBuilder(
                    context,
                    AhyakDataBase::class.java,
                    "Ahyak-database"
                ).fallbackToDestructiveMigration()
                    .build()
            }

         return instance!!
        }
    }
}