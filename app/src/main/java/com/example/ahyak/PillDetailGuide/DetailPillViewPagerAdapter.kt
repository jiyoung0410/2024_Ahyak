package com.example.ahyak.PillDetailGuide

import android.os.Bundle
import android.provider.Settings.Global.putString
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class DetailPillViewPagerAdapter(fm:FragmentManager, private val effectData: String, private val cautionData: String, private val foodData: String, private val sideeffectData: String) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount() = 4

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PillDetailEfficientFragment().apply {
                arguments = Bundle().apply {
                    putString("effect", effectData)
                }
            }
            1 -> PillDetailCautionFragment().apply {
                arguments = Bundle().apply {
                    putString("caution", cautionData)
                }
            }
            2 -> PillDetailPillFoodFragment().apply {
                arguments = Bundle().apply {
                    putString("drugFood", foodData)
                }
            }
            3 -> PillDetailSideEffectFragment().apply {
                arguments = Bundle().apply {
                    putString("sideEffect", sideeffectData)
                }
            }
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

}