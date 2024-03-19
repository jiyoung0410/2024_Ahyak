package com.example.ahyak.PillRegister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityExtraRegisterPillBinding

class ExtraRegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityExtraRegisterPillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExtraRegisterPillBinding.inflate(layoutInflater)

        //'X'버튼 눌렀을 때
        binding.extraRegisterPillCancleIv.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}