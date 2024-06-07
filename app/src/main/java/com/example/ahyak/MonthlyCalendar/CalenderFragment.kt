package com.example.ahyak.MonthlyCalendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.MedicineEntity
import com.example.ahyak.DB.TodayRecordEntity
import com.example.ahyak.DB.TodayRecordSymptomEntity
import com.example.ahyak.databinding.FragmentCalenderBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    var ahyakDataBase : AhyakDataBase? = null
    val cal = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var newSympomList = arrayListOf<TodayRecordSymptomEntity>()
        var newMedicineList = arrayListOf<MedicineEntity>()
        var newSympomContent = arrayListOf<TodayRecordEntity>()
        binding = FragmentCalenderBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        GlobalScope.launch(Dispatchers.IO) {
            ahyakDataBase = AhyakDataBase.getInstance(requireContext())

            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            var checkCount = 0

            newSympomList += ahyakDataBase!!.getTodayRecordSymptomDao().getTodayRecordSymptom(month,day)
            for(item in newSympomList) {
                calendarsymptomsList.add(DataItemCalenderSymptoms(item.SymptomName,item.SymptomStrength))
            }

            newMedicineList += ahyakDataBase!!.getMedicineDao().getMedicineTakeOfDay(month,day)
            for(item in newMedicineList) {
                takepillList.add(DataItemTakePill(item.MedicineName,item.MedicineTake))
                if(item.MedicineTake == true) {
                    checkCount++
                }
            }

            newSympomContent += ahyakDataBase!!.getTodayRecordDao().getTodayRecordContent(month,day)
            if(newSympomContent.size == 0) {
                binding.calendarRecordTextTv.text = ""
            } else {
                binding.calendarRecordTextTv.text = newSympomContent[0].RecordContent
            }

            //프로그레스바 설정
            val progress = checkCount * 100 / takepillList.size
            binding.calendarProgressbarPb.progress = progress
            binding.calenderProgressPercentTv.text = progress.toString() + "%"
        }
//        takepillListInit()
        inittakepilladapter()

//        calendarsymptomsListInit()
        initcalendarsymptomsadapter()

        var calendarWeekAdapter = CalendarWeekAdapter(arrayListOf("월","화","수","목","금","토","일"))
        binding.calendarWeekRv.adapter = calendarWeekAdapter
        binding.calendarWeekRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        var cal = Calendar.getInstance()

        val monthFormat = SimpleDateFormat("yyyy년 M월",Locale.KOREA)
        var localDate = monthFormat.format(cal.time)
        binding.calendarTitleDateTv.text = localDate

        setDate(cal)

        cal = Calendar.getInstance()

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
        var todayCal = Calendar.getInstance()
        newCal.timeInMillis = cal.timeInMillis
        newCal.set(Calendar.DATE,1)
        var startWeekday = newCal.get(Calendar.DAY_OF_WEEK)
        var lastDay = newCal.getActualMaximum(Calendar.DATE)

        newCal.add(Calendar.MONTH,-1)

        var prevMonthLastDay = newCal.getActualMaximum(Calendar.DATE)
        var dayList = ArrayList<CalDaysInfo>()

        val parts = binding.calendarTitleDateTv.text.toString().split(" ")
        val year = parts.firstOrNull()?.substringBefore("년")?.toIntOrNull() ?: 0
        val month = parts.last().dropLast(1).toIntOrNull() ?: 0

        for(i in startWeekday-2 downTo 0) {
//            dayList.add((prevMonthLastDay-i).toString())
            dayList.add(CalDaysInfo(year.toString(),month.toString(),(-1).toString(),0))
        }
        for(i in 1..lastDay) {
            if(year == todayCal.get(Calendar.YEAR) && month == todayCal.get(Calendar.MONTH) + 1 && i == todayCal.get(Calendar.DAY_OF_MONTH)) { // 오늘 날짜
                if(i == 4 || i == 9 || i == 16 || i == 17 || i == 18) { //약 복용한 날 조건 수정 필요
                    dayList.add(CalDaysInfo(year.toString(),month.toString(),i.toString(),4))
                } else if(i == 10 || i == 11 || i == 13) { //약 미복용한 날 조건 수정 필요
                    dayList.add(CalDaysInfo(year.toString(),month.toString(),i.toString(),5))
                } else { // 약을 안 복용하는 날
                    dayList.add(CalDaysInfo(year.toString(),month.toString(),i.toString(),3))
                }
            } else {
                if(i == 4 || i == 9 || i == 16 || i == 17 || i == 18) { //약 복용한 날 조건 수정 필요
                    dayList.add(CalDaysInfo(year.toString(),month.toString(),i.toString(),1))
                } else if(i == 10 || i == 11 || i == 13) { //약 미복용한 날 조건 수정 필요
                    dayList.add(CalDaysInfo(year.toString(),month.toString(),i.toString(),2))
                } else { // 약을 안 복용하는 날
                    dayList.add(CalDaysInfo(year.toString(), month.toString(), i.toString(), 0))
                }
            }
        }
//        var dayCount = 1
//        while(dayList.size < 42) {
//            dayList.add(dayCount.toString())
//            dayCount++
//        }

        binding.calendarDaysRv.adapter = CalendarDaysAdapter(dayList)
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