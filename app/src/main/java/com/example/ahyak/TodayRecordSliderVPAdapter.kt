package com.example.ahyak

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ahyak.Calendar.CalendarAfterwakeFragment
import com.example.ahyak.Calendar.CalendarBeforesleepFragment
import com.example.ahyak.Calendar.CalendarDinnerFragment
import com.example.ahyak.Calendar.CalendarLunchFragment
import com.example.ahyak.Calendar.CalendarMorningFragment

class TodayRecordSliderVPAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CalendarAfterwakeFragment()
            1 -> CalendarMorningFragment()
            2 -> CalendarLunchFragment()
            3 -> CalendarDinnerFragment()
            else -> CalendarBeforesleepFragment()
        }
    }
}