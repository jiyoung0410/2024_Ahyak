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
    }
}