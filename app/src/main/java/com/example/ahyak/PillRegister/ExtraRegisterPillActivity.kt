package com.example.ahyak.PillRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.example.ahyak.MainActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityExtraRegisterPillBinding

class ExtraRegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityExtraRegisterPillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExtraRegisterPillBinding.inflate(layoutInflater)

        binding.extraRegisterPillNameInputEt.imeOptions = EditorInfo.IME_ACTION_DONE

        //돋보기 아이콘 누르면 결과 화면으로 이동
        binding.extraRegisterPillSearchIv.setOnClickListener {
            val intent = Intent(this, ResultPillActivity::class.java)
            startActivity(intent)
        }

        //mg 버튼 누르면
        binding.extraRegisterPillDosageMgCv.setOnClickListener {
            binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.gray2_radi_5dp)
        }

        //정 버튼 누르면
        binding.extraRegisterPillDosageTabletCv.setOnClickListener {
            binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.gray2_radi_5dp)

        }

        //저장하기 버튼 누르면
        binding.extraRegisterPillSaveLl.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            //데이터 함께 보내기
            //intent.putExtra("searchText", searchSymptom.searchsympotmName)
            startActivity(intent)
        }

        //'X'버튼 눌렀을 때
        binding.extraRegisterPillCancleIv.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}