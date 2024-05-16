package com.example.ahyak.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//Dao를 담고 있는 DataBase 객체
@Database(entities = [AhyakEntity::class, PrescriptionEntity::class], version = 6)
abstract class AhyakDataBase : RoomDatabase(){
    //Entity에 접근할 수 있는 Dao들을 가짐
    abstract fun getAhyakDao() : AhyakDAO
    abstract fun getPrescriptionDao(): PrescriptionDAO
//    abstract fun getAddMedicineDao() : MedicineDao

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