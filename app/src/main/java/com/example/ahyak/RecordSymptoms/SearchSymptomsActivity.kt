package com.example.ahyak.RecordSymptoms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivitySearchSymptomsBinding

class SearchSymptomsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchSymptomsBinding

    private var searchSymptoms : ArrayList<DataItemSearchSymptom> = arrayListOf()
    private var searchSymptomsadapter : SearchSymptomsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchSymptomsBinding.inflate(layoutInflater)

        searchSympromsInit()
        initsearchSymptomsadapter()

        //'x'누르면
        binding.recordSymptomsCancleIv.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

    private fun initsearchSymptomsadapter() {
        searchSymptomsadapter = SearchSymptomsAdapter(searchSymptoms)
        binding.searchSymptomsRv.adapter = searchSymptomsadapter
        binding.searchSymptomsRv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false )
    }

    private fun searchSympromsInit() {
        searchSymptoms.addAll(
            arrayListOf(
                DataItemSearchSymptom("가래"),
                DataItemSearchSymptom("가려움"),
                DataItemSearchSymptom("가스참"),
                DataItemSearchSymptom("가슴 답답함"),
                DataItemSearchSymptom("가슴 두근거림"),
                DataItemSearchSymptom("가슴 통증"),
                DataItemSearchSymptom("감각이상"),
                DataItemSearchSymptom("감정기복"),
                DataItemSearchSymptom("고혈당"),
                DataItemSearchSymptom("과다 수면"),
                DataItemSearchSymptom("과호흡"),
                DataItemSearchSymptom("관절 부종"),
                DataItemSearchSymptom("관절통"),
                DataItemSearchSymptom("괴사"),
                DataItemSearchSymptom("구내 마비감"),
                DataItemSearchSymptom("구내염"),
                DataItemSearchSymptom("구순염"),
                DataItemSearchSymptom("구역감"),
                DataItemSearchSymptom("구토"),
                DataItemSearchSymptom("귀 통증"),
                DataItemSearchSymptom("균형이상"),
                DataItemSearchSymptom("근무력증"),
                DataItemSearchSymptom("근육경련"),
                DataItemSearchSymptom("근육경직"),
                DataItemSearchSymptom("근육통"),
                DataItemSearchSymptom("기관지경련"),
                DataItemSearchSymptom("기관지수축"),
                DataItemSearchSymptom("기억 장애"),
                DataItemSearchSymptom("기침"),
                DataItemSearchSymptom("나른함"),
                DataItemSearchSymptom("난청"),
                DataItemSearchSymptom("눈 가려움"),
                DataItemSearchSymptom("눈 건조감"),
                DataItemSearchSymptom("눈 부종"),
                DataItemSearchSymptom("눈 충혈"),
                DataItemSearchSymptom("눈 피로감"),
                DataItemSearchSymptom("눈물흘림"),
                DataItemSearchSymptom("눈부심")
            )
        )
    }
}