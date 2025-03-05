package com.example.ahyak.PillRegister

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.ExtraPillEntity
import com.example.ahyak.MainActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityExtraRegisterPillBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class ExtraRegisterPillActivity : AppCompatActivity(), OnItemRegisterClickListener {

    private lateinit var binding : ActivityExtraRegisterPillBinding

    //캘린더 관련
    private var cal : Calendar = Calendar.getInstance()

    //시작월, 시작일 변수
    private var TakeMonth : Int = 0
    private var TakeDay : Int = 0
    private var Takedate : String = ""

    // formattedTime을 클래스 멤버 변수로 선언
    private var formattedTime: String = ""

    //약 자동완성 리스트 리사이클러뷰 관련
    private val listPill: ArrayList<DataItemRegisterPill> = arrayListOf()
    private var listPillAdapter: RegisterPillAdapter? = null

    var pillType: String = "mg"
    var pillName: String = ""
    var pillVolume: String = ""

    //데이터 베이스 객체
    var ahyakDatabase : AhyakDataBase? = null

    val localDate = LocalDateTime.now()
    val todayYear = localDate.year

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExtraRegisterPillBinding.inflate(layoutInflater)

        //월, 일 정보 받아오기
        //추가 약 저장하기 위함
        val sharedPref = this.getSharedPreferences("myPref", Context.MODE_PRIVATE)

        var name_modify = intent.getStringExtra("extrapillmodify_name")
        var vol_modify = intent.getStringExtra("extrapillmodify_volume")
        var type_modify = intent.getStringExtra("extrapillmodify_type")
        var selectedMonth = intent.getIntExtra("extrapillmodify_month",0)
        var selectedDay = intent.getIntExtra("extrapillmodify_day",0)
        var time_modify = intent.getStringExtra("extrapillmodify_time")

        if (name_modify != null) {
            Log.d("msg",selectedMonth.toString() + " " + selectedDay.toString())
            binding.extraRegisterPillNameInputEt.setText(name_modify)
            binding.extraRegisterPillDosageInputEt.setText(vol_modify)
            if (type_modify == "mg") {
                binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.bg_radi_5dp)
                binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
                pillType = "mg"
            } else {
                binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.bg_radi_5dp)
                binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.white_radi_5dp)
                pillType = "정"
            }
            binding.takeDaySelectTv.text = LocalDateTime.now().year.toString() + "." +
                    (selectedMonth).toString() + "." + (selectedDay).toString() + "."
            binding.takeTimeSelectTv.text = time_modify
        } else {
            selectedMonth = sharedPref.getInt("selectedMonth", 0)
            selectedDay = sharedPref.getInt("selectedDay", 0)

            //복약일 기본 설정은 [추가 약]화면으로 진입한 날짜로 선택
            // 오늘 날짜의 "연도"를 가져와서 변수에 할당(연도만 가져옴)
            Takedate = todayYear.toString() + "." + (selectedMonth).toString() + "." + (selectedDay).toString() + "."
            binding.takeDaySelectTv.text = Takedate

            //복약 시간 설정
            //복약일 디폴트 설정은 [추가 약]화면으로 진입한 시간
            //현재 시간 가져오기
            val now = LocalDateTime.now()
            // 시간 형식 지정 (a: 오전/오후, h:mm -> 12시간제)
            val formatter = DateTimeFormatter.ofPattern("a hh:mm")
            //포맷 적용
            var formattedTime = now.format(formatter)
            binding.takeTimeSelectTv.text = formattedTime
        }

        //약 자동완성 관련 초기화
        listPillInit()
        initlistPillAdapter()

        binding.extraRegisterPillNameInputEt.imeOptions = EditorInfo.IME_ACTION_DONE

        //mg 버튼 누르면
        binding.extraRegisterPillDosageMgCv.setOnClickListener {
            binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.point_radi_5dp)
            binding.extraRegisterPillDosageMgTv.setTextColor(this.getColor(R.color.white))
            binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.extraRegisterPillDosageTabletTv.setTextColor(this.getColor(R.color.black))
            pillType = "mg"
        }

        //정 버튼 누르면
        binding.extraRegisterPillDosageTabletCv.setOnClickListener {
            binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.point_radi_5dp)
            binding.extraRegisterPillDosageTabletTv.setTextColor(this.getColor(R.color.white))
            binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.extraRegisterPillDosageMgTv.setTextColor(this.getColor(R.color.black))
            pillType = "정"
        }

        //추가 약 이름
        binding.extraRegisterPillNameInputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.extraRegisterPillNameInputEt.clearFocus() // EditTextView1의 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.extraRegisterPillDosageInputEt.windowToken, 0) // 키보드 숨김
                binding.extraRegisterPillDosageInputEt.requestFocus() // EditTextView2로 포커스 이동
                return@setOnEditorActionListener true
            }
            false
        }

        //약 이름 입력 자동완성(not API)
        binding.extraRegisterPillNameInputEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전에 실행할 작업
                binding.listPillRv.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때마다 실행할 작업
                filterPillName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력 후에 실행할 작업
            }
        })

        //복용 용량
        binding.extraRegisterPillDosageInputEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.extraRegisterPillDosageInputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.extraRegisterPillDosageInputEt.clearFocus() // 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.extraRegisterPillDosageInputEt.windowToken, 0) // 키보드 숨김
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        //날짜 선택
        var TakeMonth = selectedMonth
        var TakeDay = selectedDay

        //복약일 설정
        binding.takeDaySelectTv.setOnClickListener {

            //데이터피커로 날짜 선택
            val datePickerDialog = DatePickerDialog(this, {_, year, month, day ->
                Takedate = todayYear.toString() + "." + (month + 1).toString() + "." + day.toString()+ "."
                binding.takeDaySelectTv.text = Takedate
                TakeMonth = month+1
                TakeDay = day
                Log.d("Takedate", "$Takedate")
            }, todayYear, TakeMonth-1, TakeDay)

            val textColor : Int = ContextCompat.getColor(this, R.color.point)

            datePickerDialog.show()
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(textColor)
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(textColor)
        }

        //타임 피커 사용해서 복약 시간 설정하기 - 스피너 형식
        // 타임 피커 사용해서 복약 시간 설정하기 - 스피너 형식
        binding.takeTimeSelectTv.setOnClickListener {
            // 현재 시간 가져오기
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)  // 24시간제 기준
            val minute = cal.get(Calendar.MINUTE)

            // TimePickerDialog 생성 (12시간제 AM/PM 사용)
            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.THEME_HOLO_LIGHT, // 기본 스피너 스타일 적용
                { _, selectedHour, selectedMinute ->
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    val hour12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12

                    // 선택한 시간을 formattedTime 변수에 저장
                    formattedTime = "$amPm $hour12Format:${String.format("%02d", selectedMinute)}"

                    // TextView 업데이트
                    binding.takeTimeSelectTv.text = formattedTime

                    // 로그 출력
                    Log.d("SelectedTime", "타임 피커에서 선택한 시간: $formattedTime")
                },
                hour,
                minute,
                false // 12시간제 설정
            )

            timePickerDialog.show()
        }


        // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
        binding.root.setOnTouchListener { _, _ ->
            binding.extraRegisterPillNameInputEt.clearFocus() // 포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.extraRegisterPillNameInputEt.windowToken, 0) // 키보드 숨김
            true
        }

        // EditText 내용 변경 시 버튼 상태 업데이트
        binding.extraRegisterPillNameInputEt.addTextChangedListener {
            updateSaveButtonState()
        }

        binding.extraRegisterPillDosageInputEt.addTextChangedListener {
            updateSaveButtonState()
        }

        //저장하기 버튼 누르면
        binding.extraRegisterPillSaveLl.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            //Edit Text에서 약 이름과 용량 정보 받아오기
            pillVolume = binding.extraRegisterPillDosageInputEt.getText().toString()
            pillName = binding.extraRegisterPillNameInputEt.getText().toString()

//            //현재 시간 계산하기
//            val currentTimeMillis = System.currentTimeMillis()
//            val dateFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
//            val formattedTime = dateFormat.format(currentTimeMillis)


            GlobalScope.launch(Dispatchers.IO) {

                //데이터베이스 초기화
                ahyakDatabase = AhyakDataBase.getInstance(this@ExtraRegisterPillActivity)

                Log.d("Prescription Saving", "$pillName, $TakeMonth, $TakeDay, $pillVolume, $pillType, $formattedTime")
                //추가 약 등록
                ahyakDatabase!!.getExtraPillDao()?.insertPill(
                    ExtraPillEntity(pillName, TakeMonth, TakeDay, "기상 직후", pillVolume, pillType, formattedTime)
                )
            }

            finish()
            startActivity(intent)
        }

        //'X'버튼 눌렀을 때
        binding.extraRegisterPillCancleIv.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

    private fun filterPillName(query: String) {
        val filteredList = ArrayList<DataItemRegisterPill>()

        for (item in listPill) {
            // 증상 명칭에 검색어가 포함되어 있는지 확인
            if (item.RegisterPillName.contains(query, ignoreCase = true)) {
                filteredList.add(item)
            }
        }

        // 어댑터에 필터링된 목록 설정
        listPillAdapter?.filterList(filteredList)
    }

    private fun initlistPillAdapter() {
        listPillAdapter = RegisterPillAdapter(listPill, this)
        binding.listPillRv.adapter = listPillAdapter
        binding.listPillRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun listPillInit() {
        listPill.addAll(
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

    private fun updateSaveButtonState() {
        pillVolume = binding.extraRegisterPillDosageInputEt.text.toString()
        pillName = binding.extraRegisterPillNameInputEt.text.toString()

        if (pillVolume.isNullOrEmpty() || pillName.isNullOrEmpty()) {
            binding.extraRegisterPillGraySaveLl.visibility = View.VISIBLE
            binding.extraRegisterPillSaveLl.visibility = View.GONE
        } else {
            binding.extraRegisterPillGraySaveLl.visibility = View.GONE
            binding.extraRegisterPillSaveLl.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(dataItemRegisterPill: DataItemRegisterPill) {
        binding.extraRegisterPillNameInputEt.setText(dataItemRegisterPill.RegisterPillName)
        binding.extraRegisterPillNameInputEt.clearFocus()
        binding.listPillRv.visibility = View.GONE
    }

}