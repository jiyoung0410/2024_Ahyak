package com.example.ahyak.PillDetailGuide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahyak.R
import com.example.ahyak.databinding.FragmentPillDetailPillFoodBinding
class PillDetailPillFoodFragment : Fragment() {

    private lateinit var binding : FragmentPillDetailPillFoodBinding
    private var drug_food:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drug_food = it.getString("drugFood")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPillDetailPillFoodBinding.inflate(layoutInflater)
        binding.pillFoodInstructionTv.setText(drug_food.toString())
        // Inflate the layout for this fragment
        return binding.root
    }

}