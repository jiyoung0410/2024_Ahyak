package com.example.ahyak.Calendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.AddSymptomsActivity
import com.example.ahyak.MedicationTimeActivity
import com.example.ahyak.R
import com.example.ahyak.RecordSymptoms.RecordSymptomsActivity
import com.example.ahyak.databinding.FragmentCalendarAfterwakeBinding

class CalendarAfterwakeFragment : Fragment() {

    private lateinit var binding: FragmentCalendarAfterwakeBinding
    private var extrapillList : ArrayList<DataItemExtraPill> = arrayListOf()
    private var extrapilladapter : CalendarItemExtraPillAdapter?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarAfterwakeBinding.inflate(layoutInflater)

        extrapillListInit()
        initextrapilladapter()

        //오늘의 증상 기록하기 누르면
        binding.calendarAfterwakeRecordLl.setOnClickListener {
            val intent = Intent(getActivity(), RecordSymptomsActivity::class.java)
            startActivity(intent)
        }

        binding.calendarAfterwakeChangeSymptomRv.apply {
            // 어댑터를 미리 초기화하고 리사이클러뷰에 설정
            val adapter = CalendarItemSympotmAdapter(
                onClick = { ->
                    // 선택된 아이템 클릭 이벤트
                },
                onAddPillClick = { symptom ->
                    // 새로운 약 추가 이벤트
                    val newPillItem = DataItemSymptom.DataItemAddPill("10mg", "새로운 약")
                    symptom.ItemAddPill.add(newPillItem)
                    adapter?.notifyDataSetChanged()
                }
            ).build(sympotmList)

            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }

        binding.calendarAfterwakeChangeTimeLl.setOnClickListener {
            val intent = Intent(requireContext(), MedicationTimeActivity::class.java)
            startActivity(intent)
        }

        binding.calendarAfterwakeAddSymptomLl.setOnClickListener {
            val intent = Intent(requireContext(), AddSymptomsActivity::class.java)
            startActivity(intent)

            // 새로운 아이템을 생성하거나 기존 데이터를 수정
            val newSymptomItem = DataItemSymptom("새로운 증상", "새로운 병원", "2024.03.09",
                ItemAddPill = arrayListOf(DataItemSymptom.DataItemAddPill("10mg", "새로운 약")))

            // 기존 데이터에 새로운 아이템을 추가
            sympotmList.add(newSymptomItem)

            // 추가된 아이템을 리사이클러뷰에 반영
            binding.calendarAfterwakeChangeSymptomRv.adapter?.notifyItemInserted(sympotmList.size - 1)
        }

        binding.calendarAfterwakeChangeAddPillLl.setOnClickListener {
            val newExtraPillItem = DataItemExtraPill("새로운약", "1정", "오전 12:00")

            // 기존 데이터에 새로운 아이템을 추가
            extrapillList.add(newExtraPillItem)

            // 추가된 아이템을 리사이클러뷰에 반영
            extrapilladapter?.notifyItemInserted(extrapillList.size - 1)
        }

        return binding.root
    }

    private fun initextrapilladapter() {
        extrapilladapter = CalendarItemExtraPillAdapter(extrapillList)
        binding.calendarAfterwakeChangeExtraPillRv.adapter = extrapilladapter
        binding.calendarAfterwakeChangeExtraPillRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
    }

    val sympotmList = arrayListOf<DataItemSymptom>(
        DataItemSymptom("환절기 피부 질환", "연세대학교 원주 세브란스 기독병원", "2024.03.08",
            ItemAddPill = arrayListOf(DataItemSymptom.DataItemAddPill("18mg", "콘서타"), DataItemSymptom.DataItemAddPill("10mg", "인테놀정"))),
        DataItemSymptom("특발성 돌풍", "건국대학교 병원", "2024.05.08",
            ItemAddPill = arrayListOf(DataItemSymptom.DataItemAddPill("20mg", "경동파니틴정"), DataItemSymptom.DataItemAddPill("10mg", "트리부틴서방정"), DataItemSymptom.DataItemAddPill("20mg", "포타겔현탁액")))
    )

    private fun extrapillListInit() {
        extrapillList.addAll(
            arrayListOf(
                DataItemExtraPill("타이레놀", "1정", "오전 11:00")
            )
        )
    }
}