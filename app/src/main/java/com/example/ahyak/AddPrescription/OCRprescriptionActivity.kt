package com.example.ahyak.AddPrescription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ahyak.databinding.ActivityOcrprescriptionBinding

class OCRprescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOcrprescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOcrprescriptionBinding.inflate(layoutInflater)
    
        binding.cameraCancleIc.setOnClickListener {
            finish()
        }
    }
}