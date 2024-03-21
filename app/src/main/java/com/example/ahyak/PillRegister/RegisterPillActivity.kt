package com.example.ahyak.PillRegister

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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

        binding.registerPillNameInputEt.imeOptions = EditorInfo.IME_ACTION_DONE

        //돋보기 아이콘 누르면 결과 화면으로 이동
        binding.registerPillSearchIv.setOnClickListener {
            val intent = Intent(this, ResultPillActivity::class.java)
            startActivity(intent)
        }

        //mg 버튼 누르면
        binding.registerPillDosageMgCv.setOnClickListener {
            binding.registerPillDosageMgCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.registerPillDosageTabletCv.setBackgroundResource(R.drawable.gray2_radi_5dp)
        }

        //정 버튼 누르면
        binding.registerPillDosageTabletCv.setOnClickListener {
            binding.registerPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.registerPillDosageMgCv.setBackgroundResource(R.drawable.gray2_radi_5dp)

        }

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