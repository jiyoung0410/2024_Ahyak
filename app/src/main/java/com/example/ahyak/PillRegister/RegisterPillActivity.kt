package com.example.ahyak.PillRegister

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.Calendar.DataItemSymptom
import com.example.ahyak.MainActivity
import com.example.ahyak.R
import com.example.ahyak.RecordSymptoms.DataItemSearchSymptom
import com.example.ahyak.RecordSymptoms.DegreeSymptomsActivity
import com.example.ahyak.RecordSymptoms.frequency.FrequencyTermActivity
import com.example.ahyak.databinding.ActivityRegisterPillBinding
import com.example.ahyak.remote.AuthService
import com.example.ahyak.remote.AutoCompleteView
import com.google.gson.Gson

class RegisterPillActivity : AppCompatActivity(), AutoCompleteView {

    private lateinit var binding : ActivityRegisterPillBinding
    var registerpillDosage: String = "mg"
    var registerpillDosageSize:String = ""
    var registerpillName:String = ""

    //약 자동완성 관련
    private val registerPills : ArrayList<DataItemRegisterPill> = arrayListOf()
    private var registerPillAdapter : RegisterPillAdapter? = null

    private val selectedTimes = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterPillBinding.inflate(layoutInflater)

        //약 자동완성 관련 초기화
        registerPillInit()
        initregisterPilladapter()

        val authService = AuthService(this@RegisterPillActivity)
        authService.setautoCompleteView(this)

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

        binding.registerPillNameInputEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전에 실행할 작업
                binding.registerPillRv.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때마다 실행할 작업
                authService.autoComplete(s.toString())
//                filterPillName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력 후에 실행할 작업
            }
        })

        //시간 선택에 사용할 리스트 설정
        val layouts = listOf(
            binding.registerPillTimeWakeLl,
            binding.registerPillTimeMorningLl,
            binding.registerPillTimeAfternoonLl,
            binding.registerPillTimeDinnerLl,
            binding.registerPillTimeNightLl
        )

        layouts.forEach { layout ->
            layout.setOnClickListener {
                toggletimeSelection(layout)
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
            binding.registerPillDosageTabletCv.setBackgroundResource(R.drawable.bg_radi_5dp)
            registerpillDosage = "mg"
        }

        //정 버튼 누르면
        binding.registerPillDosageTabletCv.setOnClickListener {
            binding.registerPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.registerPillDosageMgCv.setBackgroundResource(R.drawable.bg_radi_5dp)
            registerpillDosage = "정"

        }

        //모양으로 검색하기 눌렀을 때
        binding.registerPillSearchShapeTv.setOnClickListener {
            val symptomName = intent.getStringExtra("putsymptomName")
            val intent = Intent(this, SearchPillActivity::class.java)
            intent.putExtra("putsymptomName", symptomName) //증상의 이름을 넘김
            finish()
            startActivity(intent)
        }

        //빈도 눌렀을 때
        binding.registerPillFrequencySelectTv.setOnClickListener{
            val intent = Intent(this, FrequencyTermActivity::class.java)
            finish()
            startActivity(intent)
        }

        val resultPillName = intent.getStringExtra("resultPillInpoName") ?: ""
        if(resultPillName!!.isNotEmpty()){
            binding.registerPillNameInputEt.visibility = View.GONE
            binding.registerPillNameInputTv.visibility = View.VISIBLE
            binding.registerPillNameInputTv.text = resultPillName
            binding.registerPillSearchIv.visibility = View.GONE
            binding.registerPillDeleteIv.visibility = View.VISIBLE
        }

        binding.registerPillDeleteIv.setOnClickListener {
            binding.registerPillNameInputEt.visibility = View.VISIBLE
            binding.registerPillNameInputTv.visibility = View.GONE
            binding.registerPillNameInputEt.hint = "약의 이름을 검색해주세요"
            binding.registerPillSearchIv.visibility = View.VISIBLE
            binding.registerPillDeleteIv.visibility = View.GONE

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

            if(binding.registerPillNameInputEt.text.isEmpty()){
                registerpillName = binding.registerPillNameInputTv.text.toString()
            }else{
                registerpillName = binding.registerPillNameInputEt.text.toString()
            }

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
            intent.putExtra("putsymptomName", symptomName) //증상의 이름을 넘김

//            editor.clear() // 모든 데이터를 지우는 메서드
//            editor.apply()

            finish()
            startActivity(intent)
        }
        setContentView(binding.root)
    }

    private fun initregisterPilladapter() {
        registerPillAdapter = RegisterPillAdapter(registerPills, this)
        binding.registerPillRv.adapter = registerPillAdapter
        binding.registerPillRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun registerPillInit() {
        registerPills.addAll(
            arrayListOf(
                DataItemRegisterPill("감기약"),
                DataItemRegisterPill("나는 감기"),
                DataItemRegisterPill("타이레놀"),
                DataItemRegisterPill("타이레놀2"),
                DataItemRegisterPill("타이레놀3"),
                DataItemRegisterPill("타이레놀4"),
                DataItemRegisterPill("타이레놀5")
            )
        )
    }

    private fun filterPillName(query: String) {
        val filteredList = ArrayList<DataItemRegisterPill>()

        for (item in registerPills) {
            // 증상 명칭에 검색어가 포함되어 있는지 확인현
            if (item.RegisterPillName.contains(query, ignoreCase = true)) {
                filteredList.add(item)
            }
        }

        // 어댑터에 필터링된 목록 설정
        registerPillAdapter?.filterList(filteredList)
    }

    private fun toggletimeSelection(layout: LinearLayout) {
        val background = layout.background
        val textView = layout.getChildAt(0) as TextView
        val isPointRadiSelected = background != null && background.constantState == ContextCompat.getDrawable(this, R.drawable.point_radi_100dp)?.constantState

        val bgColor = if (isPointRadiSelected) {
            // 선택되지 않은 상태의 배경 이미지를 적용합니다.
            textView.setTextColor(Color.GRAY)
            R.drawable.bg_radi_100dp
        } else {
            // 선택된 상태의 배경 이미지를 다른 리소스로 변경합니다.
            textView.setTextColor(Color.WHITE)
            R.drawable.point_radi_100dp

        }
        layout.setBackgroundResource(bgColor) // 배경을 적용합니다.
    }

    fun onItemClick(dataItemRegisterPill: DataItemRegisterPill) {
        binding.registerPillNameInputEt.visibility = View.GONE
        binding.registerPillNameInputEt.text.clear()
        binding.registerPillNameInputTv.setText(dataItemRegisterPill.RegisterPillName)
        binding.registerPillNameInputTv.visibility = View.VISIBLE
        binding.registerPillRv.visibility = View.GONE
        binding.registerPillSearchIv.visibility = View.GONE
        binding.registerPillDeleteIv.visibility = View.VISIBLE

    }

    override fun AutoCompleteLoading() {
    }

    override fun AutoCompleteSuccess(drug_list: List<String>) {
        Log.d("success", drug_list.toString())
        val filteredList = ArrayList<DataItemRegisterPill>()

        for(item in drug_list) {
            filteredList.add(DataItemRegisterPill(item))
        }

        // 어댑터에 필터링된 목록 설정
        registerPillAdapter?.filterList(filteredList)
    }

    override fun AutoCompleteFailure() {
    }


}