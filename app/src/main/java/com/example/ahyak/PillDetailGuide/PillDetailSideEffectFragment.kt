package com.example.ahyak.PillDetailGuide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahyak.R
import com.example.ahyak.databinding.FragmentPillDetailSideEffectBinding

class PillDetailSideEffectFragment : Fragment() {

    private lateinit var binding : FragmentPillDetailSideEffectBinding

    private var sideeffectdata : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sideeffectdata = it.getString("sideEffect")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPillDetailSideEffectBinding.inflate(layoutInflater)

        binding.pillSideeffectInstructionTv.setText(sideeffectdata.toString())
        // Inflate the layout for this fragment
        return binding.root
    }
}