package com.example.ahyak.PillDetailGuide

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class DetailPillViewPagerAdapter(fm:FragmentManager): FragmentStatePagerAdapter(fm) {
    override fun getCount() = 4

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0       ->  PillDetailEfficientFragment()
            1       ->  PillDetailCautionFragment()
            2       ->  PillDetailPillFoodFragment()
            else    -> PillDetailSideEffectFragment()
        }
    }

}