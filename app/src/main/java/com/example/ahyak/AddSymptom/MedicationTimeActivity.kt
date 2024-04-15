package com.example.ahyak.AddSymptom

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ahyak.Alarm.AlarmFunctions
import com.example.ahyak.Alarm.AlarmReceiver
import com.example.ahyak.Alarm.AlarmReceiver.Companion.CHANNEL_ID
import com.example.ahyak.FullScreenAlarmActivity
import com.example.ahyak.R
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
            alarmFunctions.callAlarm(hour,minute,alarmCode,"알람!")

//            val alarmIntent = Intent(this, AlarmReceiver::class.java)
//            val pendingIntent = PendingIntent.getBroadcast(this,100,alarmIntent,PendingIntent.FLAG_IMMUTABLE)
//            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,5000,pendingIntent)
            finish()
        }
    }
}