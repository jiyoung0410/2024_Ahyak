package com.example.ahyak

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ahyak.HomeRecord.CalendarAdapter
import com.example.ahyak.HomeRecord.CalendarVO
import com.example.ahyak.databinding.FragmentTodayRecordBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class TodayRecordFragment : Fragment() {
    lateinit var binding: FragmentTodayRecordBinding
    lateinit var calendarAdapter: CalendarAdapter
    private var calendarList = ArrayList<CalendarVO>()
    private val tabItems = arrayOf<String>("기상 직후", "아침", "점심", "저녁", "취침 전")

    //음성 인식 관련
    private lateinit var speechRecognizer: SpeechRecognizer

    // 전역 변수로 Month와 Day를 저장할 변수 선언
    private var selectedMonth: Int = 0
    private var selectedDay: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodayRecordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var week_day: ArrayList<String> = arrayListOf("월", "화", "수", "목", "금", "토", "일")
        var nowMonday: LocalDateTime? = null

        val dateFormat = DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko"))
        val monthFormat =
            DateTimeFormatter.ofPattern("yyyy년 M월").withLocale(Locale.forLanguageTag("ko"))
        val localDate = LocalDateTime.now()
        var calSelectedDay: LocalDateTime = localDate
        var thisMonday: LocalDateTime =
            localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        var existSelectedItem = false

        //sharedpreference 선언
        val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // 오늘 날짜의 연도와 월을 가져와서 변수에 할당합니다.
        selectedMonth = localDate.monthValue
        selectedDay = localDate.dayOfMonth


        binding.todayRecordYearmonthTv.text = localDate.format(monthFormat)
        nowMonday = thisMonday
        for (i in 0..6) {
            calendarList.apply {
                add(CalendarVO(thisMonday.plusDays(i.toLong()).format(dateFormat), week_day[i]))
            }
        }

        calendarAdapter = CalendarAdapter(calendarList, calSelectedDay, true) { item ->
            //캘린더 click event 내용
            for (i in 0..6) {
                if (nowMonday!!.plusDays(i.toLong()).dayOfMonth == item.cl_date.toInt()) {
                    calSelectedDay = nowMonday!!.plusDays(i.toLong())
//                    binding.todayRecordYearmonthTv.text = calSelectedDay.format(monthFormat)

                    // 선택된 날짜의 월과 일을 구합니다.
                    selectedMonth = calSelectedDay.monthValue
                    selectedDay = calSelectedDay.dayOfMonth

                    editor.putInt("selectedDay", selectedDay)
                    editor.putInt("selectedMonth", selectedMonth)
                    editor.putString("selectSlot", "기상 직후")
                    editor.apply()

                    Log.d("set click", "selectedDay : $selectedDay, selectedMonth : $selectedMonth")

                    binding.todayRecordVp.adapter = TodayRecordSliderVPAdapter(requireActivity())
                    TabLayoutMediator(
                        binding.todayRecordTab,
                        binding.todayRecordVp
                    ) { tab, position ->
                        tab.text = tabItems[position]
                    }.attach()

                    editor.apply()
                }
            }
        }

        // ViewPager2 초기화 및 어댑터 설정
        val viewPager = binding.todayRecordVp
        viewPager.isUserInputEnabled = false  // 스와이프 기능 비활성화
        // ViewPager2에 어댑터를 설정
        viewPager.adapter = TodayRecordSliderVPAdapter(requireActivity())

        // TabLayoutMediator를 설정합니다.
        TabLayoutMediator(binding.todayRecordTab, binding.todayRecordVp) { tab, position ->
            tab.text = tabItems[position]
        }.attach()

        // TabLayout의 탭 클릭 이벤트를 처리합니다.
        binding.todayRecordTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 선택된 탭의 위치를 가져옵니다.
                val selectedSlot = tabItems[tab?.position ?: 0]

                // 선택된 시간대에 따라 해당하는 Fragment로 데이터를 전달
                fun saveSelectedData(selectedMonth: Int, selectedDay: Int, selectSlot: String) {
                    with(sharedPref.edit()) {
                        putInt("selectedDay", selectedDay)
                        putInt("selectedMonth", selectedMonth)
                        putString("selectSlot", selectSlot)
                        commit() // 변경 사항 저장
                    }
                }

                // TabLayout에서 선택될 때 호출되는 함수
                when (tab?.position) {
                    0 -> {
                        saveSelectedData(selectedMonth, selectedDay, "기상 직후")
                    }

                    1 -> {
                        saveSelectedData(selectedMonth, selectedDay, "아침")
                    }

                    2 -> {
                        saveSelectedData(selectedMonth, selectedDay, "점심")
                    }

                    3 -> {
                        saveSelectedData(selectedMonth, selectedDay, "저녁")
                    }

                    else -> {
                        saveSelectedData(selectedMonth, selectedDay, "취침 전")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        binding.todayRecordCalendarRv.adapter = calendarAdapter
        binding.todayRecordCalendarRv.layoutManager = GridLayoutManager(context, 7)
        binding.todayRecordCalendarRv.isNestedScrollingEnabled = false

        //이전 주 버튼
        binding.todayRecordPrevWeekBtnCl.setOnClickListener {
            val dateFormat =
                DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko"))

            nowMonday = nowMonday?.minusDays(7)
//            binding.todayRecordYearmonthTv.text = nowMonday?.format(monthFormat)
            calendarList = ArrayList<CalendarVO>()
            for (i in 0..6) {
                calendarList.apply {
                    add(
                        CalendarVO(
                            nowMonday!!.plusDays(i.toLong()).format(dateFormat),
                            week_day[i]
                        )
                    )
                }
                if (nowMonday!!.plusDays(i.toLong()) == calSelectedDay) {
                    existSelectedItem = true
                }
            }

            calendarAdapter =
                CalendarAdapter(calendarList, calSelectedDay, existSelectedItem) { item ->
                    //캘린더 click event 내용
                    for (i in 0..6) {
                        if (nowMonday!!.plusDays(i.toLong()).dayOfMonth == item.cl_date.toInt()) {
                            calSelectedDay = nowMonday!!.plusDays(i.toLong())
                            binding.todayRecordYearmonthTv.text = calSelectedDay.format(monthFormat)

                            // 선택된 날짜의 월과 일을 구합니다.
                            selectedMonth = calSelectedDay.monthValue
                            selectedDay = calSelectedDay.dayOfMonth

                            editor.putInt("selectedDay", selectedDay)
                            editor.putInt("selectedMonth", selectedMonth)
                            editor.putString("selectSlot", "기상 직후")
                            editor.apply()

                            binding.todayRecordVp.adapter =
                                TodayRecordSliderVPAdapter(requireActivity())
                            TabLayoutMediator(
                                binding.todayRecordTab,
                                binding.todayRecordVp
                            ) { tab, position ->
                                tab.text = tabItems[position]
                            }.attach()

                            editor.apply()
                        }
                    }
                }
            existSelectedItem = false
            binding.todayRecordCalendarRv.adapter = calendarAdapter
        }

        //다음 주 버튼
        binding.todayRecordNextWeekBtnCl.setOnClickListener {
            val dateFormat =
                DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko"))

            nowMonday = nowMonday?.plusDays(7)
//            binding.todayRecordYearmonthTv.text = nowMonday?.format(monthFormat)
            calendarList = ArrayList<CalendarVO>()
            for (i in 0..6) {
                calendarList.apply {
                    add(
                        CalendarVO(
                            nowMonday!!.plusDays(i.toLong()).format(dateFormat),
                            week_day[i]
                        )
                    )
                }
                if (nowMonday!!.plusDays(i.toLong()) == calSelectedDay) {
                    existSelectedItem = true
                }
            }

            calendarAdapter =
                CalendarAdapter(calendarList, calSelectedDay, existSelectedItem) { item ->
                    //캘린더 click event 내용
                    for (i in 0..6) {
                        if (nowMonday!!.plusDays(i.toLong()).dayOfMonth == item.cl_date.toInt()) {
                            calSelectedDay = nowMonday!!.plusDays(i.toLong())
                            binding.todayRecordYearmonthTv.text = calSelectedDay.format(monthFormat)

                            // 선택된 날짜의 월과 일을 구합니다.
                            selectedMonth = calSelectedDay.monthValue
                            selectedDay = calSelectedDay.dayOfMonth

                            editor.putInt("selectedDay", selectedDay)
                            editor.putInt("selectedMonth", selectedMonth)
                            editor.putString("selectSlot", "기상 직후")
                            editor.apply()

                            binding.todayRecordVp.adapter =
                                TodayRecordSliderVPAdapter(requireActivity())
                            TabLayoutMediator(
                                binding.todayRecordTab,
                                binding.todayRecordVp
                            ) { tab, position ->
                                tab.text = tabItems[position]
                            }.attach()

                            editor.apply()

                        }
                    }
                }
            existSelectedItem = false
            binding.todayRecordCalendarRv.adapter = calendarAdapter
        }

        binding.todayRecordVp.adapter = TodayRecordSliderVPAdapter(requireActivity())
        TabLayoutMediator(binding.todayRecordTab, binding.todayRecordVp) { tab, position ->
            tab.text = tabItems[position]
        }.attach()

        //음성인식 관련 코드
        requestPermission()

        // RecognizerIntent 생성
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.example.ahyak")    // 여분의 키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)// 언어 설정

        // <말하기> 버튼 눌러서 음성인식 시작
        binding.speechBtn.setOnClickListener {
            // 새 SpeechRecognizer 를 만드는 팩토리 메서드
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireActivity())
            speechRecognizer.setRecognitionListener(recognitionListener)    // 리스너 설정
            speechRecognizer.startListening(intent)                         // 듣기 시작

        }
    }

//     권한 설정 메소드
    private fun requestPermission() {
        // 버전 체크, 권한 허용했는지 체크
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO), 0
            )
        }
    }

    // 리스너 설정
    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        // 말하기 시작할 준비가 되면 호출
        override fun onReadyForSpeech(params: Bundle) {
            Toast.makeText(requireActivity(), "음성인식 시작", Toast.LENGTH_SHORT).show()
//            binding.tvState.text = "이제 말씀하세요!"
        }

        // 말하기 시작했을 때 호출
        override fun onBeginningOfSpeech() {
            binding.speechBtn.visibility = View.GONE
            binding.speechIngBtn.visibility = View.VISIBLE
//            binding.tvState.text = "잘 듣고 있어요."
        }

        // 입력받는 소리의 크기를 알려줌
        override fun onRmsChanged(rmsdB: Float) {}

        // 말을 시작하고 인식이 된 단어를 buffer에 담음
        override fun onBufferReceived(buffer: ByteArray) {}

        // 말하기를 중지하면 호출
        override fun onEndOfSpeech() {
//            binding.tvState.text = "끝!"
            binding.speechBtn.visibility = View.VISIBLE
            binding.speechIngBtn.visibility = View.GONE
        }

        // 오류 발생했을 때 호출
        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
            Toast.makeText(requireActivity(), "에러 발생 $message", Toast.LENGTH_SHORT).show()
//
        }

        // 인식 결과가 준비되면 호출
        override fun onResults(results: Bundle) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            binding.speechBubbleLl.visibility = View.VISIBLE
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//            for (i in matches!!.indices){
//                binding.speechBubbleTv.text = matches[i]
//                //val result = matches[i]
//                //Toast.makeText(requireActivity(), "$result", Toast.LENGTH_LONG).show()
//                //Log.d("speech result", "$result")
//                //binding.textView.text = matches[i]
//            }
            for (i in matches!!.indices) {
                binding.speechBubbleTv.setText(matches[i])
            }
        }

        // 부분 인식 결과를 사용할 수 있을 때 호출
        override fun onPartialResults(partialResults: Bundle) {}

        // 향후 이벤트를 추가하기 위해 예약
        override fun onEvent(eventType: Int, params: Bundle) {}
    }
}