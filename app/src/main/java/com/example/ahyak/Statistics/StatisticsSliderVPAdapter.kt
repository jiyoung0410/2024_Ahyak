package com.example.ahyak.Statistics

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ahyak.Statistics.StatisticsCurvedFragment
import com.example.ahyak.Statistics.StatisticsHeatmapFragment

class StatisticsSliderVPAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> StatisticsHeatmapFragment()
            else -> StatisticsCurvedFragment()
        }
    }
}