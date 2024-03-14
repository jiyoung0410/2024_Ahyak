package com.example.ahyak.RecordSymptoms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ahyak.Calendar.CalendarItemExtraPillAdapter
import com.example.ahyak.Calendar.DataItemExtraPill
import com.example.ahyak.Calendar.DataItemSymptom
import com.example.ahyak.MainActivity
import com.example.ahyak.R
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
            //원형 증상 추가하는 코드
//            val newRecordSymptom = DataItemRecordSymptom("두드러기", "#2222")
//            recordSymptoms.add(newRecordSymptom)
//            recordSymptomsadapter?.notifyItemInserted(recordSymptoms.size-1)
        }

        //저장 누르면
        binding.recordSymptomsSaveLl.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }

        //'x'누르면
        binding.recordSymptomsCancleIv.setOnClickListener {
            finish()
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