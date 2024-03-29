package com.example.ahyak.MonthlyCalendar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.databinding.FragmentCalenderBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class CalenderFragment : Fragment() {

    private lateinit var binding : FragmentCalenderBinding
    private var takepillList : ArrayList<DataItemTakePill> = arrayListOf()
    private var takepilladapter : CalenderTakePillAdapter?=null
    private var calendarsymptomsList : ArrayList<DataItemCalenderSymptoms> = arrayListOf()
    private var calendarsymptomsadapter : CalenderInconvenienceSymptomsAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalenderBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        takepillListInit()
        inittakepilladapter()

        calendarsymptomsListInit()
        initcalendarsymptomsadapter()

        //프로그레스바 설정(임시)
        binding.calendarProgressbarPb.progress = 85

        var calendarWeekAdapter = CalendarWeekAdapter(arrayListOf("월","화","수","목","금","토","일"))
        binding.calendarWeekRv.adapter = calendarWeekAdapter
        binding.calendarWeekRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        var cal = Calendar.getInstance()

        val monthFormat = SimpleDateFormat("yyyy년 M월",Locale.KOREA)
        var localDate = monthFormat.format(cal.time)
        binding.calendarTitleDateTv.text = localDate

        setDate(cal)

        cal = Calendar.getInstance()
        Log.d("logcat",cal.time.toString())

        binding.calendarDatePrevIv.setOnClickListener {
            cal.add(Calendar.MONTH,-1)
            localDate = monthFormat.format(cal.time)
            binding.calendarTitleDateTv.text = localDate

            Log.d("logcat",cal.time.toString())
            setDate(cal)
        }

        binding.calendarDateNextIv.setOnClickListener {
            cal.add(Calendar.MONTH,1)
            localDate = monthFormat.format(cal.time)
            binding.calendarTitleDateTv.text = localDate

            Log.d("logcat",cal.time.toString())
            setDate(cal)
        }

        return binding.root
    }

    private fun setDate(cal : Calendar) {
        var newCal = Calendar.getInstance()
        newCal.timeInMillis = cal.timeInMillis
        newCal.set(Calendar.DATE,1)
        var startWeekday = newCal.get(Calendar.DAY_OF_WEEK)
        var lastDay = newCal.getActualMaximum(Calendar.DATE)

        newCal.add(Calendar.MONTH,-1)

        var prevMonthLastDay = newCal.getActualMaximum(Calendar.DATE)
        var dayList = ArrayList<String>()

        for(i in startWeekday-2 downTo 0) {
//            dayList.add((prevMonthLastDay-i).toString())
            dayList.add((-1).toString())
        }
        for(i in 1..lastDay) {
            dayList.add(i.toString())
        }
//        var dayCount = 1
//        while(dayList.size < 42) {
//            dayList.add(dayCount.toString())
//            dayCount++
//        }

        val parts = binding.calendarTitleDateTv.text.toString().split(" ")
        val year = parts.first().toIntOrNull() ?: 0
        val month = parts.last().dropLast(1).toIntOrNull() ?: 0
        binding.calendarDaysRv.adapter = CalendarDaysAdapter(dayList,year,month)
        binding.calendarDaysRv.layoutManager = GridLayoutManager(requireContext(),7)
    }

    private fun initcalendarsymptomsadapter() {
        calendarsymptomsadapter = CalenderInconvenienceSymptomsAdapter(calendarsymptomsList)
        binding.calendarInconvenienceRv.adapter = calendarsymptomsadapter
        binding.calendarInconvenienceRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun calendarsymptomsListInit() {
        calendarsymptomsList.addAll(
            arrayListOf(
                DataItemCalenderSymptoms("구역", 1),
                DataItemCalenderSymptoms("속쓰림", 5),
                DataItemCalenderSymptoms("두통", 3),
                DataItemCalenderSymptoms("두근거림", 3),
                DataItemCalenderSymptoms("불면", 5),
                DataItemCalenderSymptoms("식욕감소", 4)
            )
        )
    }

    private fun inittakepilladapter() {
        takepilladapter = CalenderTakePillAdapter(takepillList)
        binding.calendarWhetherTakePillRv.adapter = takepilladapter
        binding.calendarWhetherTakePillRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
    }

    private fun takepillListInit() {
        takepillList.addAll(
            arrayListOf(
                DataItemTakePill("인테놀정", true),
                DataItemTakePill("콘서타", false)
            )
        )
    }
}