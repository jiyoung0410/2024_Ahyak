package com.example.ahyak.PillDetailGuide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityDetailPillBinding

class DetailPillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPillBinding
    //private val adapter by lazy { DetailPillViewPagerAdapter(supportFragmentManager) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getpillName = intent.getStringExtra("sendpillName") ?: ""
        val effectData = intent.getStringExtra("effect") ?: ""
        val cautionData = intent.getStringExtra("caution") ?: ""
        val foodData = intent.getStringExtra("drugFood") ?: ""
        val sideeffectData = intent.getStringExtra("sideEffect") ?: ""

        binding.pillTitleTv.setText(getpillName)

        //Log.d("effectresult each print", "effect $effectData, caution $cautionData, drug_food $foodData, side_effect $sideeffectData")

        val adapter = DetailPillViewPagerAdapter(supportFragmentManager, effectData, cautionData, foodData, sideeffectData)

        //  initialize viewpager
        binding.viewPager.adapter = DetailPillActivity@adapter

        // 'X'버튼 클릭했을 때
        binding.cancleIv.setOnClickListener {
            finish()
        }

        //ViewPager와 TabLayout 연결
        //val viewPagerAdapter = DetailPillViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter

        binding.tabLayout.setupWithViewPager(binding.viewPager, true)

    }
}