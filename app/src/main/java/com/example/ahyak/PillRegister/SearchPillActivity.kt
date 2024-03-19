package com.example.ahyak.PillRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivitySearchPillBinding

class SearchPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchPillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchPillBinding.inflate(layoutInflater)


        //'X'버튼 누르면
        binding.searchPillCancleIv.setOnClickListener {
            finish()
        }

        //검색하기 버튼 누르면
        binding.searchPillSearchLl.setOnClickListener {
            val intent = Intent(this, ResultPillActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }
}