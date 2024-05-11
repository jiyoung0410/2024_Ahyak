package com.example.ahyak.PillDetailGuide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahyak.R
import com.example.ahyak.databinding.FragmentPillDetailCautionBinding

class PillDetailCautionFragment : Fragment() {

    private lateinit var binding : FragmentPillDetailCautionBinding

    private var cautionData : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cautionData = it.getString("caution")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPillDetailCautionBinding.inflate(layoutInflater)
        binding.pillCautionInstructionTv.setText(cautionData.toString())
        return binding.root
    }
}