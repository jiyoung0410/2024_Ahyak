package com.example.ahyak

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ahyak.databinding.ActivityFullScreenAlarmBinding
import com.example.ahyak.databinding.ActivityMedicationTimeBinding

class FullScreenAlarmActivity : AppCompatActivity() {
    lateinit var binding : ActivityFullScreenAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val alarmTime = sharedPref.getString("alarmTime","정해진 시간 없음")
        binding.fullScreenAlarmTimeTv.text = alarmTime

        binding.fullScreenAlarmTakenowBtnCl.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.fullScreenAlarmTakelaterBtnCl.setOnClickListener {
            finish()
        }
    }
}