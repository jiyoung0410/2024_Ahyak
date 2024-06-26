package com.example.ahyak.Statistics

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.DB.TodayRecordSymptomEntity
import com.example.ahyak.R
import com.example.ahyak.Sympom
import com.example.ahyak.databinding.FragmentStatisticsHeatmapBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StatisticsHeatmapFragment : Fragment() {
    lateinit var binding : FragmentStatisticsHeatmapBinding
//    var statData = arrayOf(1,0,0,0,0,0,0,5,4,2,2,1,0,0,4,3,2,1,0,0,0,4,4,3,2,1,1,0,5,4,3,1,0,0,0,4,3,2,2,1,1,0,5,3,4,1,0,1,0)
    var statData = arrayListOf<Int>()
//    var sympomsStatData = arrayListOf("구역","속쓰림","두통","두근거림","불면","식욕감소","불안")
    var sympomsStatData = arrayListOf<String>()
    var dateStatData = arrayListOf<String>()
    val cal = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsHeatmapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sympomsStatData = getSympomList()
        binding.statisticsHeatmapStatSympomsRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.statisticsHeatmapStatSympomsRv.adapter = StatisticsSympomAdapter(sympomsStatData)

        dateStatData = getDate()
        binding.statisticsHeatmapStatDateRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.statisticsHeatmapStatDateRv.adapter = StatisticsDateAdapter(dateStatData)

//        val tableLayout = TableLayout(context)
//        tableLayout.layoutParams = TableLayout.LayoutParams(
//            TableLayout.LayoutParams.MATCH_PARENT,
//            TableLayout.LayoutParams.WRAP_CONTENT)
//
//
//        val itemPerRow = 7
//        var rowIndex = 0
//        var tableRow : TableRow?= null
//
//        for((index,item) in statData.withIndex()) {
//            val customView = View(context)
//            //customView.text = item.toString()
//            val viewParams = TableRow.LayoutParams(35.pxToDp(requireContext()),27.pxToDp(requireContext()))
//            customView.layoutParams = viewParams
//            customView.background = getBackgroundData(item)
//
//            if(index % itemPerRow == 0) {
//                tableRow = TableRow(context)
//                val rowParams = TableRow.LayoutParams(
//                    TableRow.LayoutParams.MATCH_PARENT,
//                    TableLayout.LayoutParams.WRAP_CONTENT)
//                tableRow.layoutParams = rowParams
//                tableLayout.addView(tableRow)
//                rowIndex++
//            }
//
//            tableRow?.addView(customView)
//        }
//
//        binding.statisticsHeatmapStatLl.addView(tableLayout)
    }

    fun getDate(): ArrayList<String> {
        val dateFormat1 = SimpleDateFormat("M/d", Locale.getDefault())
        val dateFormat2 = SimpleDateFormat("M월 d일", Locale.getDefault())
        val dateList = ArrayList<String>()

        val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val startDate = sharedPref.getString("sympomCheckStartDate",null)
        val endDate = sharedPref.getString("sympomCheckEndDate",null)
        val date1 = dateFormat2.parse(startDate)
        val date2 = dateFormat2.parse(endDate)

//        endDate = dateFormat1.format(cal.time)
        cal.time = date2
        for(i in 0 until 7) {
            dateList.add(dateFormat1.format(cal.time))
            cal.add(Calendar.DATE,-1)
        }
        cal.add(Calendar.DATE,1)
//        startDate = dateFormat1.format(cal.time)

        return dateList.reversed() as ArrayList<String>
    }

    fun getSympomList(): ArrayList<String> {
        var sympoms = arrayListOf<String>()
        val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val gson = Gson()
        var json = sharedPref.getString("sympomWeekList",null)
        var newSympomList = gson.fromJson(json,object : TypeToken<ArrayList<TodayRecordSymptomEntity>>() {}.type)
            ?: arrayListOf<TodayRecordSymptomEntity>()

        var sympomList = arrayListOf<Sympom>()
        for(item in newSympomList) {
            sympomList.add(
                Sympom(
                    item.SymptomName,
                    item.RecordSymptomMonth.toString() + "월 " + item.RecordSymptomDay.toString() + "일",
                    item.SymptomStrength)
            )
        }

        getSympomData(sympomList)

        for(item in sympomList) {
            if(!sympoms.contains(item.name)) {
                sympoms.add(item.name)
            }
        }

        if(sympoms.size == 0) {
            binding.statisticsHeatmapStatNullTv.visibility = View.VISIBLE
            binding.statisticsHeatmapStatDateRv.visibility = View.GONE
        } else {
            binding.statisticsHeatmapStatNullTv.visibility = View.GONE
            binding.statisticsHeatmapStatDateRv.visibility = View.VISIBLE
        }

        return sympoms
    }

    fun getSympomData(sympomList: ArrayList<Sympom>) {
//        val dateFormat = SimpleDateFormat("M월 d일", Locale.KOREA)

        sympomList.sortWith(compareBy({ it.name }, { it.date }))

        val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val startDate = sharedPref.getString("sympomCheckStartDate",null)
        val endDate = sharedPref.getString("sympomCheckEndDate",null)

        val dateFormat = SimpleDateFormat("M월 d일", Locale.KOREA)
        var dateList = arrayListOf<String>()
        val cal = Calendar.getInstance()
        val date1 = dateFormat.parse(startDate)
        val date2 = dateFormat.parse(endDate)
        cal.time = date1
        while(cal.time.before(date2) || cal.time == date2) {
            dateList.add(dateFormat.format(cal.time))
            cal.add(Calendar.DAY_OF_MONTH,1)
        }

        val groupedSympom = sympomList.groupBy { it.name }

        // 결과를 저장할 리스트
        val completeSympomList = ArrayList<Sympom>()

        // 모든 증상에 대해 날짜를 채워넣음
        for ((symptom, inSympomList) in groupedSympom) {
            val dateMap = inSympomList.associateBy { it.date }
            for (date in dateList) {
                completeSympomList.add(dateMap[date] ?: Sympom(symptom, date, 0))
            }
        }

        // 증상과 날짜 순으로 정렬
        completeSympomList.sortWith(compareBy({ it.name }, { it.date }))
        sympomList.clear()
        sympomList.addAll(completeSympomList)

        for(item in sympomList) {
            statData.add(item.strength)
        }

        val tableLayout = TableLayout(context)
        tableLayout.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT)

        val itemPerRow = 7
        var rowIndex = 0
        var tableRow : TableRow?= null

        for((index,item) in statData.withIndex()) {
            val customView = View(context)
            //customView.text = item.toString()
            val viewParams = TableRow.LayoutParams(35.pxToDp(requireContext()),27.pxToDp(requireContext()))
            customView.layoutParams = viewParams
            customView.background = getBackgroundData(item)

            if(index % itemPerRow == 0) {
                tableRow = TableRow(context)
                val rowParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT)
                tableRow.layoutParams = rowParams
                tableLayout.addView(tableRow)
                rowIndex++
            }

            tableRow?.addView(customView)
        }

        binding.statisticsHeatmapStatLl.addView(tableLayout)
    }

    fun Int.pxToDp(context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
    }

    fun getBackgroundData(data : Int) : Drawable {
        return when(data) {
//            0 -> ColorDrawable(ContextCompat.getColor(requireContext(),R.color.white))
            0 -> ContextCompat.getDrawable(requireContext(), R.drawable.stat_white_gray2_stroke)!!
            1 -> ContextCompat.getDrawable(requireContext(),
                R.drawable.stat_light_point_gray2_stroke
            )!!
            2 -> ContextCompat.getDrawable(requireContext(), R.drawable.stat_point_gray2_stroke)!!
            3 -> ContextCompat.getDrawable(requireContext(),
                R.drawable.stat_light_deep_point_gray2_stroke
            )!!
            4 -> ContextCompat.getDrawable(requireContext(),
                R.drawable.stat_regular_deep_point_gray2_stroke
            )!!
            else -> ContextCompat.getDrawable(requireContext(),
                R.drawable.stat_deep_green_gray2_stroke
            )!!
        }
    }
}