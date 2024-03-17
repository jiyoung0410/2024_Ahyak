package com.example.ahyak.RecordSymptoms

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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

        //더 기록하고 싶나요를 클릭하면
        binding.recordSymptomsTv.setOnLongClickListener {
            binding.recordSymptomsOkIc.visibility = View.VISIBLE
            binding.recordSymptomsTv.visibility = View.INVISIBLE
            binding.recordSymptomsEt.visibility = View.VISIBLE
            binding.recordSymptomsEt.requestFocus() // EditText에 포커스를 설정하여 입력 가능하도록 함
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.recordSymptomsEt, InputMethodManager.SHOW_IMPLICIT) // 키보드를 자동으로 표시
            true // Long Click 이벤트를 소비하여 다른 클릭 이벤트가 발생하지 않도록 함
        }

        binding.recordSymptomsOkIc.setOnClickListener {
            val texted = binding.recordSymptomsEt.text
            if (texted.isEmpty()) {
                binding.recordSymptomsTv.text = "증상에 관해 자유롭게 기록해보세요"
            } else {
                binding.recordSymptomsTv.text = texted
            }
            binding.recordSymptomsEt.clearFocus() // EditText의 포커스 제거
            binding.recordSymptomsOkIc.visibility = View.INVISIBLE
            binding.recordSymptomsTv.visibility = View.VISIBLE
            binding.recordSymptomsEt.visibility = View.INVISIBLE
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.recordSymptomsEt.windowToken, 0) // 키보드 숨김

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