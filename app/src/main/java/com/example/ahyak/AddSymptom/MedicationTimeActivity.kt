package com.example.ahyak.AddSymptom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ahyak.databinding.ActivityMedicationTimeBinding

class MedicationTimeActivity : AppCompatActivity() {
    lateinit var binding : ActivityMedicationTimeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.medicationTimeCancleIv.setOnClickListener {
            finish()
        }

        binding.medicationTimeSaveLl.setOnClickListener {
            finish()
        }
    }
}