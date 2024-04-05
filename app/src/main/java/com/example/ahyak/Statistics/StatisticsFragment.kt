package com.example.ahyak.Statistics

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahyak.Sympom
import com.example.ahyak.databinding.FragmentStatisticsBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StatisticsFragment : Fragment() {
    lateinit var binding : FragmentStatisticsBinding
    private val items = arrayOf<String>("그리드 그래프","꺾은 선 그래프")
    val cal = Calendar.getInstance()
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

        getDate()
        getSympomData()
    }

    fun getDate() {
        val dateFormat = SimpleDateFormat("M월 d일", Locale.getDefault())
        var startDate : String
        var endDate : String

        endDate = dateFormat.format(cal.time)
        for(i in 0 until 7) {
            cal.add(Calendar.DATE,-1)
        }
        cal.add(Calendar.DATE,1)
        startDate = dateFormat.format(cal.time)
        binding.statisticsTitleContent1Tv.text = startDate + " ~ " + endDate

        val sharedPref = requireActivity().getSharedPreferences("myPref",MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("sympomCheckStartDate",startDate)
        editor.putString("sympomCheckEndDate",endDate)
        editor.apply()
    }

    fun getSympomData() {
        val sympomList : ArrayList<Sympom> = arrayListOf<Sympom>(
            Sympom("구역","1월 4일",1),
            Sympom("구역","1월 5일",0),
            Sympom("구역","1월 6일",0),
            Sympom("구역","1월 7일",0),
            Sympom("구역","1월 8일",0),
            Sympom("구역","1월 9일",0),
            Sympom("구역","1월 10일",0),
            Sympom("속쓰림","1월 4일",5),
            Sympom("속쓰림","1월 5일",4),
            Sympom("속쓰림","1월 6일",2),
            Sympom("속쓰림","1월 7일",2),
            Sympom("속쓰림","1월 8일",1),
            Sympom("속쓰림","1월 9일",0),
            Sympom("속쓰림","1월 10일",0),
            Sympom("두통","1월 4일",4),
            Sympom("두통","1월 5일",3),
            Sympom("두통","1월 6일",2),
            Sympom("두통","1월 7일",1),
            Sympom("두통","1월 8일",0),
            Sympom("두통","1월 9일",0),
            Sympom("두통","1월 10일",0),
            Sympom("두근거림","1월 4일",4),
            Sympom("두근거림","1월 5일",4),
            Sympom("두근거림","1월 6일",3),
            Sympom("두근거림","1월 7일",2),
            Sympom("두근거림","1월 8일",1),
            Sympom("두근거림","1월 9일",1),
            Sympom("두근거림","1월 10일",0),
            Sympom("불면","1월 4일",5),
            Sympom("불면","1월 5일",4),
            Sympom("불면","1월 6일",3),
            Sympom("불면","1월 7일",1),
            Sympom("불면","1월 8일",0),
            Sympom("불면","1월 9일",0),
            Sympom("불면","1월 10일",0),
            Sympom("식욕감소","1월 4일",4),
            Sympom("식욕감소","1월 5일",3),
            Sympom("식욕감소","1월 6일",2),
            Sympom("식욕감소","1월 7일",2),
            Sympom("식욕감소","1월 8일",1),
            Sympom("식욕감소","1월 9일",1),
            Sympom("식욕감소","1월 10일",0),
            Sympom("불안","1월 4일",5),
            Sympom("불안","1월 5일",3),
            Sympom("불안","1월 6일",4),
            Sympom("불안","1월 7일",1),
            Sympom("불안","1월 8일",0),
            Sympom("불안","1월 9일",1),
            Sympom("불안","1월 10일",0),
        )

        val sharedPref = requireActivity().getSharedPreferences("myPref",MODE_PRIVATE)
        val gson = Gson()
        val editor = sharedPref.edit()
        val newJson = gson.toJson(sympomList)
        editor.putString("sympomWeekList",newJson)
        editor.apply()
    }
}