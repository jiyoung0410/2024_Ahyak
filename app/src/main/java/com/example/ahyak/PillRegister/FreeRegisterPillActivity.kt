package com.example.ahyak.PillRegister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityFreeRegisterPillBinding

class FreeRegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFreeRegisterPillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFreeRegisterPillBinding.inflate(layoutInflater)

        //'x'버튼 눌렀을때
        binding.searchPillCancleIv.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
    }
}