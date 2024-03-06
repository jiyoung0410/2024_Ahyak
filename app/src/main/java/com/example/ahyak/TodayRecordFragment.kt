package com.example.ahyak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ahyak.databinding.FragmentTodayRecordBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class TodayRecordFragment : Fragment() {
    lateinit var binding : FragmentTodayRecordBinding
    lateinit var calendarAdapter : CalendarAdapter
    private var calendarList = ArrayList<CalendarVO>()
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

        var week_day: ArrayList<String> = arrayListOf("월","화","수","목","금","토","일")

        calendarAdapter = CalendarAdapter(calendarList)

        calendarList.apply {
            val dateFormat = DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko"))
            val monthFormat = DateTimeFormatter.ofPattern("yyyy년 M월").withLocale(Locale.forLanguageTag("ko"))

            val localDate = LocalDateTime.now().format(monthFormat)
            binding.todayRecordYearmonthTv.text = localDate

            var thisMonday : LocalDateTime = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            for(i in 0..6) {
                calendarList.apply {
                    add(CalendarVO(thisMonday.plusDays(i.toLong()).format(dateFormat),week_day[i]))
                }
            }
        }
        binding.todayRecordCalendarRv.adapter = calendarAdapter
        binding.todayRecordCalendarRv.layoutManager = GridLayoutManager(context,7)
    }
}