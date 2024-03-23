package com.example.ahyak.PillRegister

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.ahyak.Calendar.DataItemExtraPill
import com.example.ahyak.Calendar.DataItemSymptom
import com.example.ahyak.MainActivity
import com.example.ahyak.R
import com.example.ahyak.RecordSymptoms.RecordSymptomsActivity
import com.example.ahyak.databinding.ActivityRegisterPillBinding
import com.google.gson.Gson

class RegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterPillBinding
    var registerpillDosage: String = "정"
    var registerpillDosageSize:String = ""
    var registerpillName:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterPillBinding.inflate(layoutInflater)

        //text에 밑줄 추가하는 코드
        binding.registerPillSearchShapeTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.registerPillNameInputEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.registerPillNameInputEt.setOnEditorActionListener { _, actionId1, _ ->
            if (actionId1 == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.registerPillNameInputEt.clearFocus() // 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.registerPillNameInputEt.windowToken, 0) // 키보드 숨김
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        binding.registerPillDosageInputEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.registerPillDosageInputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.registerPillDosageInputEt.clearFocus() // 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.registerPillDosageInputEt.windowToken, 0) // 키보드 숨김
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
        binding.root.setOnTouchListener { _, _ ->
            binding.registerPillNameInputEt.clearFocus() // 포커스 해제
            binding.registerPillDosageInputEt.clearFocus() // 포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.registerPillNameInputEt.windowToken, 0) // 키보드 숨김
            inputMethodManager.hideSoftInputFromWindow(binding.registerPillDosageInputEt.windowToken, 0)
            true
        }


        //돋보기 아이콘 누르면 결과 화면으로 이동
        binding.registerPillSearchIv.setOnClickListener {
            val intent = Intent(this, ResultPillActivity::class.java)
            startActivity(intent)
        }

        //mg 버튼 누르면
        binding.registerPillDosageMgCv.setOnClickListener {
            binding.registerPillDosageMgCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.registerPillDosageTabletCv.setBackgroundResource(R.drawable.gray2_radi_5dp)
            registerpillDosage = "mg"
        }

        //정 버튼 누르면
        binding.registerPillDosageTabletCv.setOnClickListener {
            binding.registerPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.registerPillDosageMgCv.setBackgroundResource(R.drawable.gray2_radi_5dp)
            registerpillDosage = "정"

        }

        //모양으로 검색하기 눌렀을 때
        binding.registerPillSearchShapeTv.setOnClickListener {
            val intent = Intent(this, SearchPillActivity::class.java)
            finish()
            startActivity(intent)
        }

        val resultPillName = intent.getStringExtra("resultPillInpoName") ?: ""
        if(resultPillName!!.isNotEmpty()){
            binding.registerPillNameInputEt.hint = resultPillName
        }

        val freeRecordPillName = intent.getStringExtra("freeRecordPillInpoName") ?: ""
        if(freeRecordPillName!!.isNotEmpty()){
            binding.registerPillNameInputEt.hint = freeRecordPillName
        }

        //'X'버튼 눌렀을 때
        binding.registerPillCancleIv.setOnClickListener {
            finish()
        }

        //저장 눌렀을 때
        binding.registerPillSaveLl.setOnClickListener {
            registerpillDosageSize = binding.registerPillDosageInputEt.text.toString()
            registerpillName = binding.registerPillNameInputEt.text.toString()

            // Intent를 통해 전달된 데이터를 받을 때 Extra 키 값을 "putsymptomName"으로 설정
            val symptomName = intent.getStringExtra("putsymptomName")

            // SharedPreferences에서 기존 데이터 불러오기
            val preferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = preferences.getString("symptomList", null)
            val symptomList: MutableList<DataItemSymptom> = if (json != null) {
                gson.fromJson(json, Array<DataItemSymptom>::class.java).toMutableList()
            } else {
                mutableListOf()
            }
            // 해당 증상을 찾아서 약을 추가할 수 있도록 함
            val selectedSymptom = symptomList.find { it.sympotmname == symptomName }

            // 만약 해당 증상이 없으면 새로운 증상을 생성하여 추가
            if (selectedSymptom == null) {
                val newSymptom = DataItemSymptom(
                    symptomName ?: "New Symptom",
                    "New Hospital",
                    "2024.03.08",
                    arrayListOf(DataItemSymptom.DataItemAddPill("$registerpillDosageSize$registerpillDosage", registerpillName))
                )
                symptomList.add(newSymptom)
            } else {
                // 새로운 약 생성
                val newPill = DataItemSymptom.DataItemAddPill("$registerpillDosageSize$registerpillDosage", registerpillName)

                // 선택한 증상에 새로운 약 추가
                selectedSymptom.ItemAddPill.add(newPill)
            }

            // SharedPreferences에 수정된 데이터 저장
            val editor = preferences.edit()
            editor.putString("symptomList", gson.toJson(symptomList))
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)

            finish()
            startActivity(intent)
        }

        setContentView(binding.root)

    }
}