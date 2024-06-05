package com.example.ahyak.Statistics

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.TodayRecordSymptomEntity
import com.example.ahyak.Sympom
import com.example.ahyak.databinding.FragmentStatisticsBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StatisticsFragment : Fragment() {
    lateinit var binding : FragmentStatisticsBinding
    private val items = arrayOf<String>("그리드 그래프","꺾은 선 그래프")
    val cal = Calendar.getInstance()
    var startDate : String = ""
    var endDate : String = ""
    var ahyakDataBase : AhyakDataBase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStatisticsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.statisticsAreaVp.adapter = StatisticsSliderVPAdapter(requireActivity())
        TabLayoutMediator(binding.statisticsTab,binding.statisticsAreaVp) { tab, position ->
            tab.text = items[position]
        }.attach()

        getDate(0)
        getSympomData()

        binding.statisticsTitlePrevIv.setOnClickListener {
            getDate(-7)
        }
        binding.statisticsTitleNextIv.setOnClickListener {
            getDate(7)
        }
    }

    fun getDate(moveDay: Int) {
        val dateFormat = SimpleDateFormat("M월 d일", Locale.getDefault())

        cal.add(Calendar.DATE,moveDay)

        endDate = dateFormat.format(cal.time)
        for(i in 0 until 7) {
            cal.add(Calendar.DATE,-1)
        }
        cal.add(Calendar.DATE,1)
        startDate = dateFormat.format(cal.time)
        cal.add(Calendar.DATE,6)
        binding.statisticsTitleContent1Tv.text = startDate + " ~ " + endDate

        val sharedPref = requireActivity().getSharedPreferences("myPref",MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("sympomCheckStartDate",startDate)
        editor.putString("sympomCheckEndDate",endDate)
        editor.apply()
    }

    fun getSympomData() {
//        val dummyDate = arrayListOf("4월 24일","4월 25일","4월 26일","4월 27일","4월 28일","4월 29일","4월 30일")
//        val sympomList : ArrayList<Sympom> = arrayListOf<Sympom>(
//            Sympom("구역",dummyDate[0],1),
//            Sympom("구역",dummyDate[1],0),
//            Sympom("구역",dummyDate[2],0),
//            Sympom("구역",dummyDate[3],0),
//            Sympom("구역",dummyDate[4],0),
//            Sympom("구역",dummyDate[5],0),
//            Sympom("구역",dummyDate[6],0),
//            Sympom("속쓰림",dummyDate[0],5),
//            Sympom("속쓰림",dummyDate[1],4),
//            Sympom("속쓰림",dummyDate[2],2),
//            Sympom("속쓰림",dummyDate[3],2),
//            Sympom("속쓰림",dummyDate[4],1),
//            Sympom("속쓰림",dummyDate[5],0),
//            Sympom("속쓰림",dummyDate[6],0),
//            Sympom("두통",dummyDate[0],4),
//            Sympom("두통",dummyDate[1],3),
//            Sympom("두통",dummyDate[2],2),
//            Sympom("두통",dummyDate[3],1),
//            Sympom("두통",dummyDate[4],0),
//            Sympom("두통",dummyDate[5],0),
//            Sympom("두통",dummyDate[6],0),
//            Sympom("두근거림",dummyDate[0],4),
//            Sympom("두근거림",dummyDate[1],4),
//            Sympom("두근거림",dummyDate[2],3),
//            Sympom("두근거림",dummyDate[3],2),
//            Sympom("두근거림",dummyDate[4],1),
//            Sympom("두근거림",dummyDate[5],1),
//            Sympom("두근거림",dummyDate[6],0),
//            Sympom("불면",dummyDate[0],5),
//            Sympom("불면",dummyDate[1],4),
//            Sympom("불면",dummyDate[2],3),
//            Sympom("불면",dummyDate[3],1),
//            Sympom("불면",dummyDate[4],0),
//            Sympom("불면",dummyDate[5],0),
//            Sympom("불면",dummyDate[6],0),
//            Sympom("식욕감소",dummyDate[0],4),
//            Sympom("식욕감소",dummyDate[1],3),
//            Sympom("식욕감소",dummyDate[2],2),
//            Sympom("식욕감소",dummyDate[3],2),
//            Sympom("식욕감소",dummyDate[4],1),
//            Sympom("식욕감소",dummyDate[5],1),
//            Sympom("식욕감소",dummyDate[6],0),
//            Sympom("불안",dummyDate[0],5),
//            Sympom("불안",dummyDate[1],3),
//            Sympom("불안",dummyDate[2],4),
//            Sympom("불안",dummyDate[3],1),
//            Sympom("불안",dummyDate[4],0),
//            Sympom("불안",dummyDate[5],1),
//            Sympom("불안",dummyDate[6],0),
//        )

        var dateList : ArrayList<String> = arrayListOf()
        var newSympomList = arrayListOf<TodayRecordSymptomEntity>()

        GlobalScope.launch(Dispatchers.IO) {
            ahyakDataBase = AhyakDataBase.getInstance(requireContext())

            getWeekDate(startDate,endDate,dateList)
            for(item in dateList) {
                val parts = item.split(" ")
                val month = parts[0].filter { it.isDigit() }.toInt()
                val day = parts[1].filter { it.isDigit() }.toInt()

                newSympomList += ahyakDataBase!!.getTodayRecordSymptomDao().getTodayRecordSymptom(month,day)
            }
            val sharedPref = requireActivity().getSharedPreferences("myPref",MODE_PRIVATE)
            val gson = Gson()
            val editor = sharedPref.edit()
//        val newJson = gson.toJson(sympomList)
            Log.d("logcat2",newSympomList.toString())
            val newJson = gson.toJson(newSympomList)
            editor.putString("sympomWeekList",newJson)
            editor.apply()
        }
    }

    fun getWeekDate(startDate: String, endDate: String, dateList: ArrayList<String>): Unit {
        val dateFormat = SimpleDateFormat("M월 d일", Locale.KOREA)
        val cal = Calendar.getInstance()
        val date1 = dateFormat.parse(startDate)
        val date2 = dateFormat.parse(endDate)
        cal.time = date1
        while(cal.time.before(date2) || cal.time == date2) {
            dateList.add(dateFormat.format(cal.time))
            cal.add(Calendar.DAY_OF_MONTH,1)
        }
    }
}