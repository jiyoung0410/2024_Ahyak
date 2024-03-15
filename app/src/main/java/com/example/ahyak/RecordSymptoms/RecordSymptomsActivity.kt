package com.example.ahyak.RecordSymptoms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ahyak.MainActivity
import com.example.ahyak.databinding.ActivityRecordSymptomsBinding

class RecordSymptomsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordSymptomsBinding

    private var recordSymptoms : ArrayList<DataItemRecordSymptom> = arrayListOf()
    private var recordSymptomsadapter : RecordSymptomsAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecordSymptomsBinding.inflate(layoutInflater)

        recordSymptomsInit()
        initrecordSymptomsadapter()

        //증상 추가하기 누르면
        binding.recordSymptomsAddLl.setOnClickListener {
            val intent = Intent(this, SearchSymptomsActivity::class.java)
            startActivity(intent)
        }

        val degreeColorData = intent.getSerializableExtra("degreeColor") as? DataItemRecordSymptom
        if (degreeColorData != null) {
            val searchText = degreeColorData.recordsympotmName
            val degreeColorNum = degreeColorData.recordsympotmNum

            // 받은 데이터를 사용하여 작업 수행
            val newRecordSymptom = DataItemRecordSymptom("$searchText", degreeColorNum)
            recordSymptoms.add(newRecordSymptom)
            recordSymptomsadapter?.notifyItemInserted(recordSymptoms.size-1)
        }

        //저장 누르면
        binding.recordSymptomsSaveLl.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //'x'누르면
        binding.recordSymptomsCancleIv.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }

    private fun initrecordSymptomsadapter() {
        recordSymptomsadapter = RecordSymptomsAdapter(recordSymptoms)
        binding.recordSymptomsRv.adapter = recordSymptomsadapter
        binding.recordSymptomsRv.layoutManager = GridLayoutManager(this,3)
    }

    private fun recordSymptomsInit() {
        recordSymptoms.addAll(
            arrayListOf(
                //DataItemRecordSymptom("땀 과다증", "#222222")
            )
        )
    }
}