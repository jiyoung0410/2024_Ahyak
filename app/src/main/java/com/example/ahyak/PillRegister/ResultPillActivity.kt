package com.example.ahyak.PillRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityResultPillBinding

class ResultPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultPillBinding

    private var resultpillList : ArrayList<DataItemResultPill> = arrayListOf()
    private var resultpilladapter : ResultPillAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultPillBinding.inflate(layoutInflater)

        resultpillListInit()
        initresultpilladapter()

        //'x'눌렀을 때
        binding.recordSymptomsCancleIv.setOnClickListener {
            finish()
        }

        //자유 기록하기 눌렀을 때
        binding.resultPillFreeRecordLl.setOnClickListener {
            val intent = Intent(this, FreeRegisterPillActivity::class.java)
            startActivity(intent)
        }
        setContentView(binding.root)
    }
    private fun initresultpilladapter() {
        resultpilladapter = ResultPillAdapter(resultpillList)
        binding.resultPillRv.adapter = resultpilladapter
        binding.resultPillRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun resultpillListInit() {
        resultpillList.addAll(
            arrayListOf(
                DataItemResultPill(R.drawable.pill_example_img, "가네듀오캡슐", "UDDB NTB")
            )
        )
    }
}