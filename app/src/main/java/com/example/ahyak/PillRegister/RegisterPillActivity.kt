package com.example.ahyak.PillRegister

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityRegisterPillBinding

class RegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterPillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterPillBinding.inflate(layoutInflater)



        //text에 밑줄 추가하는 코드
        binding.registerPillSearchShapeTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        //모양으로 검색하기 눌렀을 때
        binding.registerPillSearchShapeTv.setOnClickListener {
            val intent = Intent(this, SearchPillActivity::class.java)
            startActivity(intent)
        }

        //'X'버튼 눌렀을 때
        binding.registerPillCancleIv.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
    }


}