package com.example.ahyak

import OnSwipeTouchListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ahyak.Calendar.CalendarAdapter
import com.example.ahyak.Calendar.CalendarVO
import com.example.ahyak.databinding.FragmentTodayRecordBinding
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

        calendarAdapter = CalendarAdapter(calendarList) { item ->
            //캘린더 click event 내용
        }

        calendarList.apply {
            val dateFormat = DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko"))
            val monthFormat = DateTimeFormatter.ofPattern("yyyy년 M월").withLocale(Locale.forLanguageTag("ko"))

            val localDate = LocalDateTime.now().format(monthFormat)
            binding.todayRecordYearmonthTv.text = localDate

            var thisMonday : LocalDateTime = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            nowMonday = thisMonday

            for(i in 0..6) {
                calendarList.apply {
                    add(CalendarVO(thisMonday.plusDays(i.toLong()).format(dateFormat),week_day[i]))
                }
            }
        }
        binding.todayRecordCalendarRv.adapter = calendarAdapter
        binding.todayRecordCalendarRv.layoutManager = GridLayoutManager(context,7)
        binding.todayRecordCalendarRv.isNestedScrollingEnabled = false

//        binding.todayRecordCalendarRv.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
//            val dateFormat = DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko"))
//
//            override fun onSwipeLeft() {
//                super.onSwipeLeft()
//                nowMonday = nowMonday?.minusDays(7)
//                calendarList = ArrayList<CalendarVO>()
//                for(i in 0..6) {
//                    calendarList.apply {
//                        add(CalendarVO(nowMonday!!.plusDays(i.toLong()).format(dateFormat),week_day[i]))
//                    }
//                }
//                binding.todayRecordCalendarRv.adapter = calendarAdapter
//            }
//
//            override fun onSwipeRight() {
//                super.onSwipeRight()
//                nowMonday = nowMonday?.plusDays(7)
//                calendarList = ArrayList<CalendarVO>()
//                for(i in 0..6) {
//                    calendarList.apply {
//                        add(CalendarVO(nowMonday!!.plusDays(i.toLong()).format(dateFormat),week_day[i]))
//                    }
//                }
//                binding.todayRecordCalendarRv.adapter = calendarAdapter
//            }
//        })

        binding.todayRecordVp.adapter = TodayRecordSliderVPAdapter(requireActivity())
        TabLayoutMediator(binding.todayRecordTab,binding.todayRecordVp) { tab, position ->
            tab.text = tabItems[position]
        }.attach()
    }
}