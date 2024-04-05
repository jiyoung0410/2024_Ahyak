package com.example.ahyak.Statistics

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.ahyak.R
import com.example.ahyak.Sympom
import com.example.ahyak.databinding.FragmentStatisticsCurvedBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Strength(val data : Int, val sympom : String)
data class ChartCommitData(val sympom : String, val commitNum: Int)

class StatisticsCurvedFragment : Fragment() {
    lateinit var binding : FragmentStatisticsCurvedBinding
    var chartEntry = arrayListOf<Entry>()
    var dataList : MutableList<ChartCommitData> = mutableListOf()
    lateinit var spinner1 : Spinner
    lateinit var spinner2 : Spinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsCurvedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val StrengthList = arrayListOf<Strength>(
            Strength(1,"구역"),
            Strength(5,"속쓰림"),
            Strength(4,"두통"),
            Strength(4,"두근거림"),
            Strength(5,"불면"),
            Strength(4,"식욕감소"),
            Strength(5,"불안")
        )
//        var StrengthList = arrayListOf<Strength>()
        val minData = 0
        val maxData = 5

//        val dates = arrayOf("일별로 보기","1월 4일","1월 5일","1월 6일","1월 7일","1월 8일","1월 9일","1월 10일")

        var dates = arrayListOf("일별로 보기")
        var sympoms = arrayListOf("증상별로 보기")

        val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val startDate = sharedPref.getString("sympomCheckStartDate",null)
        val endDate = sharedPref.getString("sympomCheckEndDate",null)

        val gson = Gson()
        var json = sharedPref.getString("sympomWeekList",null)
        var sympomList = gson.fromJson(json,object : TypeToken<ArrayList<Sympom>>() {}.type) ?: arrayListOf<Sympom>()

        getWeekDate(startDate!!,endDate!!,dates)
        getSympomList(sympomList,sympoms)

        spinner1 = binding.statisticsCurvedSpinner1
        val spinnerAdapter1 = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, dates) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f) // 텍스트 크기를 13sp로 설정
                textView.gravity = Gravity.CENTER
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
                textView.gravity = Gravity.CENTER
                view.setPadding(0,5,0,5)
                return view
            }
        }
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner1.adapter = spinnerAdapter1

        spinner2 = binding.statisticsCurvedSpinner2
        val spinnerAdapter2 = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, sympoms) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f) // 텍스트 크기를 13sp로 설정
                textView.gravity = Gravity.CENTER
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
                textView.gravity = Gravity.CENTER
                view.setPadding(0,5,0,5)
                return view
            }
        }
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner2.adapter = spinnerAdapter2

        for(i in StrengthList.indices) { //indices : dataList의 최소 index ~ 최대 index
            chartEntry.add(Entry(i.toFloat(),StrengthList[i].data.toFloat()))
            dataList.add(ChartCommitData(StrengthList[i].sympom,StrengthList[i].data))
        }

        val lineDataSet = LineDataSet(chartEntry,"chartEntry")
        lineDataSet.apply {
            color = resources.getColor(R.color.point,null) //선의 색깔
            circleRadius = 5f //데이터 포인트에 표시되는 원의 반지름
            lineWidth = 2f //선의 두께
            setDrawCircles(true) //데이터 포인트에 표시되는 원 존재 여부
            setCircleColor(resources.getColor(R.color.point,null)) //데이터 포인트에 표시되는 원의 색깔
            setDrawHighlightIndicators(false) // 하이라이트 표시기(터치 이벤트에 사용) 사용 여부
            setDrawValues(false) // 데이터 포인트 값 표시 여부
            setDrawFilled(false) // 선 아래 영역을 채울지에 대한 여부
        }
        binding.statisticsCurvedLinechart.apply {
            axisLeft.isEnabled = true
            axisRight.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false
            isDragXEnabled = false
            isDragYEnabled = false
            isScaleXEnabled = false
            isScaleYEnabled = false
        }
        binding.statisticsCurvedLinechart.xAxis.apply {
            setDrawGridLines(true) // x축 격자 line 표시 여부
            setDrawAxisLine(true) // x축 축선 표시 여부
            setDrawLabels(true) // x축 label 표시 여부
            position = XAxis.XAxisPosition.BOTTOM // x축 label 위치
            valueFormatter = XAxisCustomFormatter(changeXAxisText(dataList)) // x축 label 데이터 형식
            textColor = resources.getColor(R.color.black,null) //label의 text 색상
            textSize = 8f
            labelRotationAngle = 0f // x축 label 회전 각도
            setLabelCount(dataList.size,true) // x축 label 개수
        }
        binding.statisticsCurvedLinechart.axisLeft.apply {
            setDrawGridLines(true)
            setDrawAxisLine(true)
            axisMaximum = maxData.toFloat()
            axisMinimum = minData.toFloat()
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            labelCount = 5
            textColor = resources.getColor(R.color.black,null)
            textSize = 8f
        }
        binding.statisticsCurvedLinechart.apply {
            data = LineData(lineDataSet)
            notifyDataSetChanged()
            invalidate() // 차트를 다시 그림
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
        Log.d("logcat",startDate+endDate)
    }

    fun getSympomList(sympomsList: ArrayList<Sympom>, sympoms: ArrayList<String>): Unit {
        for(item in sympomsList) {
            if(!sympoms.contains(item.name)) {
                sympoms.add(item.name)
            }
        }
    }

    fun changeXAxisText(dataList : List<ChartCommitData>): List<String> {
        val dataTextList = ArrayList<String>()
        for(i in dataList.indices) {
            dataTextList.add(dataList[i].sympom)
        }
        return dataTextList
    }

    class XAxisCustomFormatter(val xAxisData : List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return xAxisData[(value).toInt()]
        }
    }
}