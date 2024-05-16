package com.example.ahyak

import OnSwipeTouchListener
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ahyak.Calendar.CalendarAdapter
import com.example.ahyak.Calendar.CalendarAfterwakeFragment
import com.example.ahyak.Calendar.CalendarBeforesleepFragment
import com.example.ahyak.Calendar.CalendarDinnerFragment
import com.example.ahyak.Calendar.CalendarLunchFragment
import com.example.ahyak.Calendar.CalendarMorningFragment
import com.example.ahyak.Calendar.CalendarVO
import com.example.ahyak.databinding.FragmentTodayRecordBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Locale

class TodayRecordFragment : Fragment() {
    lateinit var binding : FragmentTodayRecordBinding
    lateinit var calendarAdapter : CalendarAdapter
    private var calendarList = ArrayList<CalendarVO>()
    private val tabItems = arrayOf<String>("기상 직후","아침","점심","저녁","취침 전")
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

        var week_day : ArrayList<String> = arrayListOf("월","화","수","목","금","토","일")
        var nowMonday : LocalDateTime? = null

        val dateFormat = DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko"))
        val monthFormat = DateTimeFormatter.ofPattern("yyyy년 M월").withLocale(Locale.forLanguageTag("ko"))
        val localDate = LocalDateTime.now()
        var calSelectedDay : LocalDateTime = localDate
        var thisMonday : LocalDateTime = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        var existSelectedItem = false

        // 오늘 날짜의 연도와 월을 가져와서 변수에 할당합니다.
        selectedMonth = localDate.monthValue
        selectedDay = localDate.dayOfMonth


        binding.todayRecordYearmonthTv.text = localDate.format(monthFormat)
        nowMonday = thisMonday
        for(i in 0..6) {
            calendarList.apply {
                add(CalendarVO(thisMonday.plusDays(i.toLong()).format(dateFormat),week_day[i]))
            }
        }

        calendarAdapter = CalendarAdapter(calendarList,calSelectedDay,true) { item ->
            //캘린더 click event 내용
            for(i in 0..6) {
                if(nowMonday!!.plusDays(i.toLong()).dayOfMonth == item.cl_date.toInt()) {
                    calSelectedDay = nowMonday!!.plusDays(i.toLong())
//                    binding.todayRecordYearmonthTv.text = calSelectedDay.format(monthFormat)

                    // 선택된 날짜의 월과 일을 구합니다.
                    selectedMonth = calSelectedDay.monthValue
                    selectedDay = calSelectedDay.dayOfMonth
                }
            }
        }

        // ViewPager2에 어댑터를 설정합니다.
        binding.todayRecordVp.adapter = TodayRecordSliderVPAdapter(requireActivity())

        // TabLayoutMediator를 설정합니다.
        TabLayoutMediator(binding.todayRecordTab, binding.todayRecordVp) { tab, position ->
            tab.text = tabItems[position]
        }.attach()

        // TabLayout의 탭 클릭 이벤트를 처리합니다.
        binding.todayRecordTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 선택된 탭의 위치를 가져옵니다.
                val selectedSlot = tabItems[tab?.position ?: 0]

                // 선택된 시간대를 Toast 메시지로 표시합니다.
                val message = "선택된 시간대: $selectedSlot, 선택된 월: $selectedMonth, 선택된 일 : $selectedDay "
                //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                // 선택된 시간대에 따라 해당하는 Fragment로 데이터를 전달
                // SharedPreferences 초기화
                val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)

                fun saveSelectedData(selectedMonth: Int, selectedDay: Int) {
                    with(sharedPref.edit()) {
                        putInt("selectedMonth", selectedMonth)
                        putInt("selectedDay", selectedDay)
                        Log.d("save date", "month : $selectedMonth, day : $selectedDay")
                        commit() // 변경 사항 저장
                    }
                }

                // TabLayout에서 선택될 때 호출되는 함수
                when (tab?.position) {
                    0 -> { saveSelectedData(selectedMonth, selectedDay) }
                    1 -> { saveSelectedData(selectedMonth, selectedDay) }
                    2 -> { saveSelectedData(selectedMonth, selectedDay) }
                    3 -> { saveSelectedData(selectedMonth, selectedDay) }
                    else -> { saveSelectedData(selectedMonth, selectedDay) }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        binding.todayRecordCalendarRv.adapter = calendarAdapter
        binding.todayRecordCalendarRv.layoutManager = GridLayoutManager(context,7)
        binding.todayRecordCalendarRv.isNestedScrollingEnabled = false

        binding.todayRecordPrevWeekBtnCl.setOnClickListener {
            val dateFormat = DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko"))

            nowMonday = nowMonday?.minusDays(7)
//            binding.todayRecordYearmonthTv.text = nowMonday?.format(monthFormat)
            calendarList = ArrayList<CalendarVO>()
            for(i in 0..6) {
                calendarList.apply {
                    add(CalendarVO(nowMonday!!.plusDays(i.toLong()).format(dateFormat),week_day[i]))
                }
                if(nowMonday!!.plusDays(i.toLong()) == calSelectedDay) {
                    existSelectedItem = true
                }
            }

            calendarAdapter = CalendarAdapter(calendarList,calSelectedDay, existSelectedItem) { item ->
                //캘린더 click event 내용
                for(i in 0..6) {
                    if(nowMonday!!.plusDays(i.toLong()).dayOfMonth == item.cl_date.toInt()) {
                        calSelectedDay = nowMonday!!.plusDays(i.toLong())
                        binding.todayRecordYearmonthTv.text = calSelectedDay.format(monthFormat)
                    }
                }
            }
            existSelectedItem = false
            binding.todayRecordCalendarRv.adapter = calendarAdapter
        }

        binding.todayRecordNextWeekBtnCl.setOnClickListener {
            val dateFormat = DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko"))

            nowMonday = nowMonday?.plusDays(7)
//            binding.todayRecordYearmonthTv.text = nowMonday?.format(monthFormat)
            calendarList = ArrayList<CalendarVO>()
            for(i in 0..6) {
                calendarList.apply {
                    add(CalendarVO(nowMonday!!.plusDays(i.toLong()).format(dateFormat),week_day[i]))
                }
                if(nowMonday!!.plusDays(i.toLong()) == calSelectedDay) {
                    existSelectedItem = true
                }
            }

            calendarAdapter = CalendarAdapter(calendarList,calSelectedDay,existSelectedItem) { item ->
                //캘린더 click event 내용
                for(i in 0..6) {
                    if(nowMonday!!.plusDays(i.toLong()).dayOfMonth == item.cl_date.toInt()) {
                        calSelectedDay = nowMonday!!.plusDays(i.toLong())
                        binding.todayRecordYearmonthTv.text = calSelectedDay.format(monthFormat)
                    }
                }
            }
            existSelectedItem = false
            binding.todayRecordCalendarRv.adapter = calendarAdapter
        }

        binding.todayRecordVp.adapter = TodayRecordSliderVPAdapter(requireActivity())
        TabLayoutMediator(binding.todayRecordTab,binding.todayRecordVp) { tab, position ->
            tab.text = tabItems[position]
        }.attach()
    }
}