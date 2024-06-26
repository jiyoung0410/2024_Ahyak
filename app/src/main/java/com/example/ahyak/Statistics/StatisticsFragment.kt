package com.example.ahyak.Statistics

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.MedicineEntity
import com.example.ahyak.DB.TodayRecordSymptomEntity
import com.example.ahyak.Sympom
import com.example.ahyak.databinding.FragmentStatisticsBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    var medicationList = arrayListOf<StatMedication>()
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
            getSympomData()

            binding.statisticsAreaVp.adapter = StatisticsSliderVPAdapter(requireActivity())
            TabLayoutMediator(binding.statisticsTab,binding.statisticsAreaVp) { tab, position ->
                tab.text = items[position]
            }.attach()
        }
        binding.statisticsTitleNextIv.setOnClickListener {
            getDate(7)
            getSympomData()

            binding.statisticsAreaVp.adapter = StatisticsSliderVPAdapter(requireActivity())
            TabLayoutMediator(binding.statisticsTab,binding.statisticsAreaVp) { tab, position ->
                tab.text = items[position]
            }.attach()
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
        var dateList : ArrayList<String> = arrayListOf()
        var newSympomList = arrayListOf<TodayRecordSymptomEntity>()
        var newMedicineList = arrayListOf<MedicineEntity>()

        GlobalScope.launch(Dispatchers.IO) {
            ahyakDataBase = AhyakDataBase.getInstance(requireContext())

            getWeekDate(startDate,endDate,dateList)
            for(item in dateList) {
                val parts = item.split(" ")
                val month = parts[0].filter { it.isDigit() }.toInt()
                val day = parts[1].filter { it.isDigit() }.toInt()

                newSympomList += ahyakDataBase!!.getTodayRecordSymptomDao().getTodayRecordSymptom(month,day)

                //복약율 계산
                newMedicineList += ahyakDataBase!!.getMedicineDao().getMedicineTakeOfDay(month,day)
            }
            val sharedPref = requireActivity().getSharedPreferences("myPref",MODE_PRIVATE)
            val gson = Gson()
            val editor = sharedPref.edit()
//        val newJson = gson.toJson(sympomList)
            Log.d("logcat2",newSympomList.toString())
            val newJson = gson.toJson(newSympomList)
            editor.putString("sympomWeekList",newJson)
            editor.apply()

            //진단받은 증상 리스트 주간별로 추출
            var medications = arrayListOf<String>()
            for(item in newMedicineList) {
                if(!medications.contains(item.PrescriptionName)) {
                    medications.add(item.PrescriptionName)
                }
            }

            medicationList = arrayListOf()
            for(medi in medications) {
                var cnt1 = 0
                var cnt2 = 0
                for(item in newMedicineList) {
                    if(item.PrescriptionName == medi) {
                        cnt2++
                        if(item.MedicineTake == true) {
                            cnt1++
                        }
                    }
                }
                medicationList += StatMedication(medi, cnt1 * 100 / cnt2)
            }
            withContext(Dispatchers.Main) {
                binding.statisticsMedicationRv.adapter = StatisticsMedicationAdapter(medicationList)
                binding.statisticsMedicationRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                Log.d("medicationList",medicationList.toString())
            }
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