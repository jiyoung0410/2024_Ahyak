package com.example.ahyak.PillDetailGuide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahyak.R
import com.example.ahyak.databinding.FragmentPillDetailEfficientBinding


class PillDetailEfficientFragment : Fragment() {

    private lateinit var binding : FragmentPillDetailEfficientBinding

    private var effectData: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            effectData = it.getString("effect")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPillDetailEfficientBinding.inflate(layoutInflater)

        binding.pillEfficientInstructionTv.setText(effectData.toString())

        return binding.root

    }

}