package com.example.ahyak.MonthlyCalendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.databinding.FragmentCalenderBinding

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


        return binding.root
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