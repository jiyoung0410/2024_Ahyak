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
import com.example.ahyak.PillRegister.ExtraRegisterPillActivity
import com.example.ahyak.PillRegister.RegisterPillActivity
import com.example.ahyak.R
import com.example.ahyak.RecordSymptoms.RecordSymptomsActivity
import com.example.ahyak.databinding.FragmentCalendarAfterwakeBinding

class CalendarAfterwakeFragment : Fragment() {

    private lateinit var binding: FragmentCalendarAfterwakeBinding
    private var extrapillList : ArrayList<DataItemExtraPill> = arrayListOf()
    private var extrapilladapter : CalendarItemExtraPillAdapter?= null

    var extraPillInpoName: String? = null
    var extraPillInpoDosageSize:String? = null
    var extraPillInpoDosage:String? = null
    var extraPillformattedTime:String? = null

    companion object {
        const val ADD_PILL_REQUEST_CODE = 1001
    }

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
                    //약 추가하기 버튼 누르면
                    // 새로운 약 추가 이벤트
                    val intent = Intent(requireContext(), RegisterPillActivity::class.java)
                    startActivity(intent)
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

        //추가 약 기록에서 약 추가하기 눌렀을 때
        binding.calendarAfterwakeChangeAddPillLl.setOnClickListener {
            val intent = Intent(requireContext(), ExtraRegisterPillActivity::class.java)
            startActivity(intent)
        }

        //Intent를 통해 전달된 데이터를 받음
        val intent = activity?.intent
        if (intent != null) {
            extraPillInpoName = intent.getStringExtra("extraPillInpoName") ?: ""
            extraPillInpoDosageSize = intent.getStringExtra("extraPillInpoDosageSize") ?: ""
            extraPillInpoDosage = intent.getStringExtra("extraPillInpoDosage") ?: ""
            extraPillformattedTime = intent.getStringExtra("formattedTime") ?: ""

            // 모든 값이 정상적으로 전달되었는지 확인
            if (extraPillInpoName!!.isNotEmpty() && extraPillInpoDosageSize!!.isNotEmpty() &&
                extraPillInpoDosage!!.isNotEmpty() && extraPillformattedTime!!.isNotEmpty()) {

                // 전달된 데이터를 사용하여 새로운 아이템 생성
                val newExtraPillItem = DataItemExtraPill(extraPillInpoName!!, "$extraPillInpoDosageSize$extraPillInpoDosage", extraPillformattedTime!!)
                // 기존 데이터에 새로운 아이템을 추가
                extrapillList.add(newExtraPillItem)
                // 추가된 아이템을 리사이클러뷰에 반영
                extrapilladapter?.notifyItemInserted(extrapillList.size - 1)
            } else {
                // 전달된 데이터가 비어있을 경우 처리할 내용 추가
            }
        }

        val registerPillInpoName = intent!!.getStringExtra("registerPillInpoName") ?: ""
        val registerPillInpoDosageSize = intent.getStringExtra("registerPillInpoDosageSize") ?: ""
        val registerPillInpoDosage = intent.getStringExtra("registerPillInpoDosage") ?: ""


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
