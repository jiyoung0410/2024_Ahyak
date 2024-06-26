package com.example.ahyak.MonthlyCalendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.MedicineEntity
import com.example.ahyak.DB.TodayRecordEntity
import com.example.ahyak.DB.TodayRecordSymptomEntity
import com.example.ahyak.databinding.FragmentCalenderBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

//        var newSympomList = arrayListOf<TodayRecordSymptomEntity>()
//        var newMedicineList = arrayListOf<MedicineEntity>()
//        var newSympomContent = arrayListOf<TodayRecordEntity>()
        binding = FragmentCalenderBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        setDataOfDay(month,day)
//        CoroutineScope(Dispatchers.IO).launch {
//            ahyakDataBase = AhyakDataBase.getInstance(requireContext())
//
//            val month = cal.get(Calendar.MONTH) + 1
//            val day = cal.get(Calendar.DAY_OF_MONTH)
//            var checkCount = 0
//
//            newSympomList += ahyakDataBase!!.getTodayRecordSymptomDao().getTodayRecordSymptom(month,day)
//            for(item in newSympomList) {
//                calendarsymptomsList.add(DataItemCalenderSymptoms(item.SymptomName,item.SymptomStrength))
//            }
//
//            newMedicineList += ahyakDataBase!!.getMedicineDao().getMedicineTakeOfDay(month,day)
//            for(item in newMedicineList) {
//                takepillList.add(DataItemTakePill(item.MedicineName,item.MedicineTake))
//                if(item.MedicineTake == true) {
//                    checkCount++
//                }
//            }
//
//            newSympomContent += ahyakDataBase!!.getTodayRecordDao().getTodayRecordContent(month,day)
//            if(newSympomContent.size == 0) {
//                binding.calendarRecordTextTv.text = ""
//            } else {
//                binding.calendarRecordTextTv.text = newSympomContent[0].RecordContent
//            }
//
//            //프로그레스바 설정
//            val progress = checkCount * 100 / takepillList.size
//            binding.calendarProgressbarPb.progress = progress
//            binding.calenderProgressPercentTv.text = progress.toString() + "%"
//        }
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

        lifecycleScope.launch(Dispatchers.IO) {
            var newMedicineList : ArrayList<MedicineEntity>
            var checkCount : Int
            var dayTakePillList : ArrayList<DataItemTakePill>
            ahyakDataBase = AhyakDataBase.getInstance(requireContext())

            for(i in startWeekday-2 downTo 0) {
                dayList.add(CalDaysInfo(year.toString(),month.toString(),(-1).toString(),0))
            }
            for(i in 1..lastDay) {
                newMedicineList = arrayListOf()
                checkCount = 0
                dayTakePillList = arrayListOf()
                newMedicineList += ahyakDataBase!!.getMedicineDao().getMedicineTakeOfDay(month,i)
                for(item in newMedicineList) {
                    dayTakePillList.add(DataItemTakePill(item.MedicineName,item.MedicineTake))
                    if(item.MedicineTake == true) {
                        checkCount++
                    }
                }

                //프로그레스바 설정
                var isToday : Int
                if(year == todayCal.get(Calendar.YEAR) && month == todayCal.get(Calendar.MONTH) + 1 && i == todayCal.get(Calendar.DAY_OF_MONTH)) {
                    isToday = 3
                } else {
                    isToday = 0
                }
                if(dayTakePillList.size != 0) {
                    Log.d("calLog",i.toString()+" "+(checkCount * 100 / dayTakePillList.size).toString())
                    if(checkCount * 100 / dayTakePillList.size == 100) {
                        dayList.add(CalDaysInfo(year.toString(),month.toString(),i.toString(),1 + isToday))
                    } else {
                        dayList.add(CalDaysInfo(year.toString(),month.toString(),i.toString(),2 + isToday))
                    }
                } else {
                    Log.d("calLog",i.toString()+" x")
                    dayList.add(CalDaysInfo(year.toString(),month.toString(),i.toString(),0 + isToday))
                }
            }
            withContext(Dispatchers.Main) {
                binding.calendarDaysRv.adapter = CalendarDaysAdapter(dayList) { item ->
                    setDataOfDay(item.month.toInt(),item.day.toInt())
                }
                binding.calendarDaysRv.layoutManager = GridLayoutManager(requireContext(),7)
            }
        }
    }

    private fun setDataOfDay(month: Int,day: Int) {
        var newSympomList = arrayListOf<TodayRecordSymptomEntity>()
        var newMedicineList = arrayListOf<MedicineEntity>()
        var newSympomContent = arrayListOf<TodayRecordEntity>()
        takepillList = arrayListOf()
        calendarsymptomsList = arrayListOf()

        lifecycleScope.launch(Dispatchers.IO) {
            ahyakDataBase = AhyakDataBase.getInstance(requireContext())
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

            //프로그레스바 설정
            var progress = 100
            if(takepillList.size != 0) {
                progress = checkCount * 100 / takepillList.size
            }

            newSympomContent += ahyakDataBase!!.getTodayRecordDao().getTodayRecordContent(month,day)

            withContext(Dispatchers.Main) {

                initcalendarsymptomsadapter()
                inittakepilladapter()

                if(newSympomContent.size == 0) {
                    binding.calendarRecordTextTv.text = ""
                } else {
                    binding.calendarRecordTextTv.text = newSympomContent[0].RecordContent
                }

                binding.calendarProgressbarPb.progress = progress
                binding.calenderProgressPercentTv.text = progress.toString() + "%"
            }
        }
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