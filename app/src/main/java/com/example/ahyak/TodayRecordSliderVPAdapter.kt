package com.example.ahyak

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ahyak.HomeRecord.TodayRecordHomeFragment

class TodayRecordSliderVPAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> TodayRecordHomeFragment()
            1 -> TodayRecordHomeFragment()
            2 -> TodayRecordHomeFragment()
            3 -> TodayRecordHomeFragment()
            else -> TodayRecordHomeFragment()
        }
    }
}