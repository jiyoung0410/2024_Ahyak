package com.example.ahyak.PillDetailGuide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityDetailPillBinding

class DetailPillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPillBinding
    private val adapter by lazy { DetailPillViewPagerAdapter(supportFragmentManager) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  initialize viewpager
        binding.viewPager.adapter = DetailPillActivity@adapter

        // 'X'버튼 클릭했을 때
        binding.cancleIv.setOnClickListener {
            finish()
        }

        //ViewPager와 TabLayout 연결
        val viewPagerAdapter = DetailPillViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = viewPagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager, true)

    }
}