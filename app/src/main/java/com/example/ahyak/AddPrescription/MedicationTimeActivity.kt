package com.example.ahyak.AddPrescription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ahyak.Alarm.AlarmFunctions
import com.example.ahyak.databinding.ActivityMedicationTimeBinding

class MedicationTimeActivity : AppCompatActivity() {
    lateinit var binding : ActivityMedicationTimeBinding
    lateinit var alarmFunctions: AlarmFunctions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmFunctions = AlarmFunctions(this)

        binding.medicationTimeCancleIv.setOnClickListener {
            finish()
        }

        binding.medicationTimeSaveLl.setOnClickListener {
            val hour = binding.medicationTimeTp.hour
            val minute = binding.medicationTimeTp.minute

            val random = (1..100000)
            val alarmCode = random.random()
            alarmFunctions.callAlarm(hour,minute,alarmCode,"Ahyak")

            val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
            val editor = sharedPref.edit()
//
//            val selectedSlot = intent.getStringExtra("alarmTimeSlot")?:""
//
//            if(selectedSlot == "기상 직후"){
//                if(hour < 12) {
//                    editor.putString("alarmTime wakeup",String.format("오전 %02d:%02d", hour, minute))
//                } else {
//                    editor.putString("alarmTime wakeup",String.format("오후 %02d:%02d", hour-12, minute))
//                }
//                editor.apply()
//            }else if(selectedSlot == "아침"){
//                if(hour < 12) {
//                    editor.putString("alarmTime morning",String.format("오전 %02d:%02d", hour, minute))
//                } else {
//                    editor.putString("alarmTime morning",String.format("오후 %02d:%02d", hour-12, minute))
//                }
//                editor.apply()
//            }else if(selectedSlot == "점심"){
//                if(hour < 12) {
//                    editor.putString("alarmTime lunch",String.format("오전 %02d:%02d", hour, minute))
//                } else {
//                    editor.putString("alarmTime lunch",String.format("오후 %02d:%02d", hour-12, minute))
//                }
//                editor.apply()
//            }else if(selectedSlot == "저녁"){
//                if(hour < 12) {
//                    editor.putString("alarmTime dinner",String.format("오전 %02d:%02d", hour, minute))
//                } else {
//                    editor.putString("alarmTime dinner",String.format("오후 %02d:%02d", hour-12, minute))
//                }
//                editor.apply()
//            }else{
//                if(hour < 12) {
//                    editor.putString("alarmTime night",String.format("오전 %02d:%02d", hour, minute))
//                } else {
//                    editor.putString("alarmTime night",String.format("오후 %02d:%02d", hour-12, minute))
//                }
//                editor.apply()
//            }

            if(hour < 12) {
                editor.putString("alarmTime",String.format("오전 %02d:%02d", hour, minute))
            } else {
                editor.putString("alarmTime",String.format("오후 %02d:%02d", hour-12, minute))
            }
            editor.apply()

//            val alarmIntent = Intent(this, AlarmReceiver::class.java)
//            val pendingIntent = PendingIntent.getBroadcast(this,100,alarmIntent,PendingIntent.FLAG_IMMUTABLE)
//            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,5000,pendingIntent)
            finish()
        }
    }
}