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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.MedicineEntity
import com.example.ahyak.MainActivity
import com.example.ahyak.R
import com.example.ahyak.RecordSymptoms.frequency.FrequencyTermActivity
import com.example.ahyak.databinding.ActivityRegisterPillBinding
import com.example.ahyak.remote.AuthService
import com.example.ahyak.remote.AutoCompleteView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterPillActivity : AppCompatActivity(), AutoCompleteView {

    private lateinit var binding: ActivityRegisterPillBinding
    var registerpillType: String = "mg"
    var registerPillVolume: String = ""
    var registerPillName: String = ""
    var resultPillName: String = ""
    var registerPillFree: Boolean = false
    var searchPillName : String = ""

    //자유기록인지 확인하기 위한 변수
    var existingMedicineNames: String = ""

    //시간대 선택 관련
    private val selectedDays = mutableListOf<String>()

    //선택된 처방 이름 Sharedpreference로 저장받을 변수 선언
    var PrescriptionName: String = ""

    //데이터 베이스 객체
    var ahyakDatabase: AhyakDataBase? = null

    //약 자동완성 리스트 리사이클러뷰 관련
    private val registerPills: ArrayList<DataItemRegisterPill> = arrayListOf()
    private var registerPillAdapter: RegisterPillAdapter? = null

    //빈도 text 설정하기 위함
    private var frequenctType : Int = -1

    private val selectedTimes = mutableListOf<String>()

    override fun onResume() {
        super.onResume()
        //Sharedpreference 변수 선언
        val sharedPref = this.getSharedPreferences("myPref", Context.MODE_PRIVATE)

        //빈도 type 받아오기
        frequenctType = -1
        frequenctType = sharedPref.getInt("type",-1)

        //빈도에 따라 설정된 값 받아오기
        if(frequenctType == -1){
            binding.registerPillFrequencySelectTv.setText("선택")
        }else if(frequenctType == 0){
            val frequenct = sharedPref.getInt("term", 0)!!
            binding.registerPillFrequencySelectTv.setText("$frequenct 일 마다")
        }else if(frequenctType == 1){
            val frequenct = sharedPref.getString("selectDay", "")!!
            binding.registerPillFrequencySelectTv.setText("$frequenct 마다")
        }else{
            binding.registerPillFrequencySelectTv.setText("필요시 투여")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterPillBinding.inflate(layoutInflater)

        //Sharedpreference 변수 선언
        val sharedPref = this.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        //처방 이름 받아오기
        PrescriptionName = sharedPref.getString("prescriptionName", "")!!

        //약 자동완성 관련 초기화
        registerPillInit()
        initregisterPilladapter()

        val authService = AuthService(this@RegisterPillActivity)
        authService.setautoCompleteView(this)

        //text에 밑줄 추가하는 코드
        binding.registerPillSearchShapeTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        //약 이름 입력 Edit Text
        binding.registerPillNameInputEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.registerPillNameInputEt.setOnEditorActionListener { _, actionId1, _ ->
            if (actionId1 == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.registerPillNameInputEt.clearFocus() // 포커스 해제
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    binding.registerPillNameInputEt.windowToken,
                    0
                ) // 키보드 숨김
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        //약 이름 입력 자동완성(not API)
        binding.registerPillNameInputEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전에 실행할 작업
                binding.registerPillRv.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때마다 실행할 작업
                authService.autoComplete(s.toString())
                filterPillName(s.toString())
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
                updateSelectedDaysTextView()
            }
        }

        //용량 기록
        binding.registerPillVolumeInputEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.registerPillVolumeInputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.registerPillVolumeInputEt.clearFocus() // 포커스 해제
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    binding.registerPillVolumeInputEt.windowToken,
                    0
                ) // 키보드 숨김
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
        binding.root.setOnTouchListener { _, _ ->
            binding.registerPillNameInputEt.clearFocus() // 포커스 해제
            binding.registerPillVolumeInputEt.clearFocus() // 포커스 해제
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                binding.registerPillNameInputEt.windowToken,
                0
            ) // 키보드 숨김
            inputMethodManager.hideSoftInputFromWindow(
                binding.registerPillVolumeInputEt.windowToken,
                0
            )
            true
        }

        //mg 버튼 누르면
        binding.registerPillDosageMgCv.setOnClickListener {
            binding.registerPillDosageMgCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.registerPillDosageTabletCv.setBackgroundResource(R.drawable.bg_radi_5dp)
            registerpillType = "mg"
        }

        //정 버튼 누르면
        binding.registerPillDosageTabletCv.setOnClickListener {
            binding.registerPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.registerPillDosageMgCv.setBackgroundResource(R.drawable.bg_radi_5dp)
            registerpillType = "정"
        }

        //모양으로 검색하기 눌렀을 때
        binding.registerPillSearchShapeTv.setOnClickListener {
            val intent = Intent(this, SearchPillActivity::class.java)
            finish()
            startActivity(intent)
        }

        //빈도 눌렀을 때
        binding.registerPillFrequencySelectTv.setOnClickListener {
            val intent = Intent(this, FrequencyTermActivity::class.java)
            //finish()
            startActivity(intent)
        }

        resultPillName = intent.getStringExtra("FreeMedicineName") ?: ""
        if (resultPillName.isNotEmpty()) {
            binding.registerPillNameInputEt.visibility = View.GONE
            binding.registerPillNameInputTv.visibility = View.VISIBLE
            binding.registerPillNameInputTv.text = resultPillName
            binding.registerPillNameInputEt.setText(resultPillName)
            binding.registerPillSearchIv.visibility = View.GONE
            binding.registerPillDeleteIv.visibility = View.VISIBLE
        }

        searchPillName = intent.getStringExtra("resultPillInpoName")?:""
        Log.d("searchPill2", "$searchPillName")
        if(searchPillName.isNotEmpty()){
            binding.registerPillNameInputEt.visibility = View.GONE
            binding.registerPillNameInputTv.visibility = View.VISIBLE
            binding.registerPillNameInputTv.text = searchPillName
            binding.registerPillNameInputEt.setText(searchPillName)
            binding.registerPillSearchIv.visibility = View.GONE
            binding.registerPillDeleteIv.visibility = View.VISIBLE
        }

        //검색 취소 아이콘 눌렀을 때
        binding.registerPillDeleteIv.setOnClickListener {
            binding.registerPillNameInputEt.visibility = View.VISIBLE
            binding.registerPillNameInputTv.visibility = View.GONE
            binding.registerPillNameInputEt.hint = "약의 이름을 검색해주세요"
            binding.registerPillSearchIv.visibility = View.VISIBLE
            binding.registerPillDeleteIv.visibility = View.GONE

        }

        val freeRecordPillName = intent.getStringExtra("freeRecordPillInpoName") ?: ""
        if (freeRecordPillName.isNotEmpty()) {
            binding.registerPillNameInputEt.hint = freeRecordPillName
        }

        //'X'버튼 눌렀을 때
        binding.registerPillCancleIv.setOnClickListener {
            finish()
        }

        //저장 눌렀을 때
        binding.registerPillSaveLl.setOnClickListener {
            //처방 이름 - prescriptionName
            //약 이름 가져오기
            val registerPilltext = binding.registerPillNameInputEt.text.toString()

            //월, 일
            val selectedMonth = sharedPref.getInt("selectedMonth", 0)
            val selectedDay = sharedPref.getInt("selectedDay", 0)

            //시간대 - list
            Log.d("시간대", "$selectedDays")

            //빈도 가져오기
//            val dates = intent.getStringArrayListExtra("dates")
//            dates?.let {
//                // dates 리스트를 처리하는 코드
//                Log.d("RegisterPillActivity", "Received dates: $it")
//            }

            // 저장된 문자열을 dates 리스트로 변환하여 사용
            val datesString = sharedPref.getString("dates", "") ?: ""
            val dates = datesString.split(",").map { it.trim() }


            //용량 데이터 가져오기
            registerPillVolume = binding.registerPillVolumeInputEt.text.toString()
            val floatVolume = registerPillVolume.toFloat()

            //용량 타입 - registerPillType
            //Take - 0

            GlobalScope.launch(Dispatchers.IO) {
                //데이터베이스 초기화
                ahyakDatabase = AhyakDataBase.getInstance(this@RegisterPillActivity)

                // 데이터베이스에서 해당 이름을 가진 자유기록 약이 있는지 불러오기
                val existingMedicineList2 =
                    ahyakDatabase!!.getFreeMedicineDao().getFreeMedicine(registerPilltext)

                // 약 이름만 추출
                existingMedicineNames =
                    existingMedicineList2?.map { it.FreeMedicineName }.toString()

                registerPillFree = existingMedicineNames.contains(registerPilltext)

                //Free Medicine인지 확인

                if (dates != null) {
                    for (date in dates) {
                        val splitDate = date.split(".") // 날짜를 월과 일로 분리
                        val selectedMonth = splitDate[1].toInt()
                        val selectedDay = splitDate[2].toInt()

                        for (time in selectedDays) {
                            // 약 추가
                            ahyakDatabase!!.getMedicineDao().insertMedicine(
                                MedicineEntity(
                                    registerPilltext,
                                    PrescriptionName,
                                    selectedMonth,
                                    selectedDay,
                                    time,
                                    floatVolume,
                                    registerpillType,
                                    false,
                                    registerPillFree
                                )
                            )
                            //잘 저장되었는지 확인
                            val existingMedicineList =
                                ahyakDatabase!!.getMedicineDao()
                                    .getMedicine(selectedMonth, selectedDay, time, PrescriptionName)
                            Log.d("register Medicine check", "$existingMedicineList")
                        }
                    }
                }
                //빈도 text 설정 reset을 위한 코드
                editor.putInt("type",-1)
                editor.apply()
            }
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        setContentView(binding.root)
    }

    private fun initregisterPilladapter() {
        registerPillAdapter = RegisterPillAdapter(registerPills, this)
        binding.registerPillRv.adapter = registerPillAdapter
        binding.registerPillRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun registerPillInit() {
        registerPills.addAll(
            arrayListOf(
                DataItemRegisterPill("가나모티에스알정15밀리그램"),
                DataItemRegisterPill("가나모티정"),
                DataItemRegisterPill("가나텍정"),
                DataItemRegisterPill("가나톤정50밀리그램"),
                DataItemRegisterPill("가나티란정"),
                DataItemRegisterPill("가네탑골드연질캡슐"),
                DataItemRegisterPill("가네탑에스연질캡슐"),
                DataItemRegisterPill("가네탑플러스연질캡슐350밀리그램"),
                DataItemRegisterPill("가니토정"),
                DataItemRegisterPill("가드메트정100/1000밀리그램"),
                DataItemRegisterPill("가드메트정100/500밀리그램"),
                DataItemRegisterPill("가드메트정100/850밀리그램"),
                DataItemRegisterPill("가로틴캡슐100밀리그램"),
                DataItemRegisterPill("가르젠정<세라티오펩티다제>"),
                DataItemRegisterPill("가모텍에스알서방정15밀리그램"),
                DataItemRegisterPill("가모틴정"),
                DataItemRegisterPill("가바스탄캡슐"),
                DataItemRegisterPill("가바스탄캡슐100mg"),
                DataItemRegisterPill("가바스틸캡슐100mg"),
                DataItemRegisterPill("가바스틸캡슐300mg"),
                DataItemRegisterPill("가바액트정600밀리그람"),
                DataItemRegisterPill("가바텐캡슐100밀리그램"),
                DataItemRegisterPill("가바텐캡슐300밀리그램"),
                DataItemRegisterPill("가바토파정100밀리그람"),
                DataItemRegisterPill("가바티론캡슐100밀리그램"),
                DataItemRegisterPill("가바티론캡슐300밀리그램"),
                DataItemRegisterPill("가바틴정600밀리그램"),
                DataItemRegisterPill("가바틴정800밀리그램"),
                DataItemRegisterPill("가바틴캡슐100밀리그람"),
                DataItemRegisterPill("가바틴캡슐300밀리그람"),
                DataItemRegisterPill("가비스타캡슐100밀리그램"),
                DataItemRegisterPill("가비스타캡슐300밀리그램"),
                DataItemRegisterPill("가스타제연질캡슐"),
                DataItemRegisterPill("가스타제정"),
                DataItemRegisterPill("게스타렌정"),
                DataItemRegisterPill("게스타렌투엑스정"),
                DataItemRegisterPill("겔타제정"),
                DataItemRegisterPill("경남비타민씨정"),
                DataItemRegisterPill("고려은단비타민씨정"),
                DataItemRegisterPill("골드타민연질캡슐"),
                DataItemRegisterPill("광동비타민씨정1000밀리그램"),
                DataItemRegisterPill("광동비타씨큐정"),
                DataItemRegisterPill("광동아토르바스타틴정10밀리그램"),
                DataItemRegisterPill("광동아토르바스타틴정20밀리그램"),
                DataItemRegisterPill("광동타리풀정"),
                DataItemRegisterPill("광동타목시펜정"),
                DataItemRegisterPill("광동타목시펜정20밀리그램"),
                DataItemRegisterPill("구주로라타딘정10밀리그람"),
                DataItemRegisterPill("구주비타민C1000mg정"),
                DataItemRegisterPill("국제로라타딘정"),
                DataItemRegisterPill("국제피오글리타존정"),
                DataItemRegisterPill("국제피오글리타존정30밀리그램"),
                DataItemRegisterPill("그루타민정500밀리그램"),
                DataItemRegisterPill("그루타제정"),
                DataItemRegisterPill("그루타존정15밀리그램"),
                DataItemRegisterPill("그리타존정15밀리그램"),
                DataItemRegisterPill("그린비타정"),
                DataItemRegisterPill("그린비타포르테연질캡슐"),
                DataItemRegisterPill("그린비타플러스연질캡슐"),
                DataItemRegisterPill("그린비타플러스정"),
                DataItemRegisterPill("글리베타엠정"),
                DataItemRegisterPill("글리스타엠정"),
                DataItemRegisterPill("글리아스타연질캡슐"),
                DataItemRegisterPill("글립타이드정200밀리그람"),
                DataItemRegisterPill("네오비타연질캡슐"),
                DataItemRegisterPill("뉴란타에이정"),
                DataItemRegisterPill("뉴미네랄비타연질캡슐"),
                DataItemRegisterPill("뉴신타아이알정50밀리그램"),
                DataItemRegisterPill("뉴트리비타에프연질캡슐"),
                DataItemRegisterPill("다파시타엠서방정10/100/1000밀리그램"),
                DataItemRegisterPill("다파시타엠서방정5/50/1000밀리그램"),
                DataItemRegisterPill("다파시타엠서방정5/50/500밀리그램"),
                DataItemRegisterPill("다파시타엠서방정5/50/750밀리그램"),
                DataItemRegisterPill("덴타에이스캡슐"),
                DataItemRegisterPill("두타엠정0.5밀리그램"),
                DataItemRegisterPill("듀아타임코와정"),
                DataItemRegisterPill("로라타인정"),
                DataItemRegisterPill("로얄비타연질캡슐"),
                DataItemRegisterPill("로이비타연질캡슐"),
                DataItemRegisterPill("로제비타연질캡슐"),
                DataItemRegisterPill("로타인정"),
                DataItemRegisterPill("루마비타연질캡슐"),
                DataItemRegisterPill("리멘타연질캡슐"),
                DataItemRegisterPill("리버베타연질캡슐"),
                DataItemRegisterPill("리버비타연질캡슐"),
                DataItemRegisterPill("마그벨비타연질캡슐"),
                DataItemRegisterPill("마그비타연질캡슐"),
                DataItemRegisterPill("맥덴타에프캡슐"),
                DataItemRegisterPill("멀티비타에스정"),
                DataItemRegisterPill("베로타이엑스정"),
                DataItemRegisterPill("알리타이드정"),
                DataItemRegisterPill("어린이용타이레놀정80밀리그람"),
                DataItemRegisterPill("에피타이츄정"),
                DataItemRegisterPill("엔타이정0.5밀리그램"),
                DataItemRegisterPill("엔타이정1.0밀리그램"),
                DataItemRegisterPill("우먼스타이레놀정"),
                DataItemRegisterPill("지엘타이밍정"),
                DataItemRegisterPill("타이노즈연질캡슐"),
                DataItemRegisterPill("타이드정"),
                DataItemRegisterPill("타이레놀8시간이알서방정"),
                DataItemRegisterPill("타이레놀8시간이알서방정325밀리그람"),
                DataItemRegisterPill("타이레놀정160밀리그람"),
                DataItemRegisterPill("타이레놀정500밀리그람"),
                DataItemRegisterPill("타이렌연질캡슐"),
                DataItemRegisterPill("타이로정"),
                DataItemRegisterPill("타이록신캡슐"),
                DataItemRegisterPill("타이론정"),
                DataItemRegisterPill("타이리콜8시간이알서방정"),
                DataItemRegisterPill("타이맥스연질캡슐"),
                DataItemRegisterPill("타이몰8시간이알정"),
                DataItemRegisterPill("타이본위클리정"),
                DataItemRegisterPill("타이센정500밀리그램"),
                DataItemRegisterPill("타이젠정"),
                DataItemRegisterPill("타이커브정250밀리그램"),
                DataItemRegisterPill("타이코푸캡슐"),
                DataItemRegisterPill("타이쿨에프 연질캡슐"),
                DataItemRegisterPill("타이펜8시간이알서방정"),
                DataItemRegisterPill("타이펜정160밀리그람"),
                DataItemRegisterPill("타이푸푸골드연질캡슐"),
                DataItemRegisterPill("타이푸푸연질캡슐"),
                DataItemRegisterPill("태준팩타이트100밀리그람정")
                )
        )
    }

    private fun filterPillName(query: String) {
        val filteredList = ArrayList<DataItemRegisterPill>()

        for (item in registerPills) {
            // 증상 명칭에 검색어가 포함되어 있는지 확인
            if (item.RegisterPillName.contains(query, ignoreCase = true)) {
                filteredList.add(item)
            }
        }

        // 어댑터에 필터링된 목록 설정
        registerPillAdapter?.filterList(filteredList)
    }

    private fun toggletimeSelection(layout: LinearLayout) {
        val textView = layout.getChildAt(0) as TextView
        val day = textView.text.toString()

        if (selectedDays.contains(day)) {
            selectedDays.remove(day)
            textView.setTextColor(Color.GRAY)
            layout.setBackgroundResource(R.drawable.bg_radi_100dp)
        } else {
            selectedDays.add(day)
            textView.setTextColor(Color.WHITE)
            layout.setBackgroundResource(R.drawable.point_radi_100dp)
        }
    }

    fun onItemClick(dataItemRegisterPill: DataItemRegisterPill) {
        binding.registerPillNameInputEt.visibility = View.GONE
        binding.registerPillNameInputEt.text.clear()
        binding.registerPillNameInputTv.text = dataItemRegisterPill.RegisterPillName
        binding.registerPillNameInputEt.setText(dataItemRegisterPill.RegisterPillName)
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

        for (item in drug_list) {
            filteredList.add(DataItemRegisterPill(item))
        }

        // 어댑터에 필터링된 목록 설정
        registerPillAdapter?.filterList(filteredList)
    }

    override fun AutoCompleteFailure() {
    }

    //시간대 선택해서 리스트 저장
    private fun updateSelectedDaysTextView() {
        selectedDays.joinToString(", ")
    }
}