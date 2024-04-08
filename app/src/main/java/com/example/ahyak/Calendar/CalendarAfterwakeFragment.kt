package com.example.ahyak.Calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.AddSymptom.AddSymptomsActivity
import com.example.ahyak.AddSymptom.MedicationTimeActivity
import com.example.ahyak.PillRegister.ExtraRegisterPillActivity
import com.example.ahyak.PillRegister.RegisterPillActivity
import com.example.ahyak.RecordSymptoms.RecordSymptomsActivity
import com.example.ahyak.databinding.FragmentCalendarAfterwakeBinding
import com.google.gson.Gson

class CalendarAfterwakeFragment : Fragment() {

    private lateinit var binding: FragmentCalendarAfterwakeBinding
    private var extrapillList : ArrayList<DataItemExtraPill> = arrayListOf()
    private var extrapilladapter : CalendarItemExtraPillAdapter?= null
    private var symptomList: MutableList<DataItemSymptom> = mutableListOf()

    var extraPillInpoName: String? = null
    var extraPillInpoDosageSize:String? = null
    var extraPillInpoDosage:String? = null
    var extraPillformattedTime:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //나중에 증상 추가하기 제대로 구현되면 삭제하기

        // 최초 실행 여부를 확인하기 위해 SharedPreferences에서 isFirstRun 값을 가져옴
        val preferences = requireActivity().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val isFirstRun = preferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            // 최초 실행 시에만 증상 추가 작업 수행

            // SharedPreferences에서 기존 데이터 불러오기
            val gson = Gson()
            val json = preferences.getString("symptomList", null)
            symptomList = if (json != null) {
                gson.fromJson(json, Array<DataItemSymptom>::class.java).toMutableList()
            } else {
                mutableListOf()
            }

            // 새로운 증상 생성
            val newSymptom = DataItemSymptom("환절기 피부 질환", "연세대학교 원주 세브란스 기독병원", "2024.03.08", arrayListOf(
                DataItemSymptom.DataItemAddPill("18mg", "콘서타"),
                DataItemSymptom.DataItemAddPill("10mg", "인테놀정")
            ))

            // 새로운 증상 추가
            symptomList.add(newSymptom)

            // SharedPreferences에 새로운 데이터 저장
            val editor = preferences.edit()
            editor.putString("symptomList", gson.toJson(symptomList))

            // 최초 실행 여부를 false로 설정하여 다음 실행에서는 이 작업을 수행하지 않도록 함
            editor.putBoolean("isFirstRun", false)
            editor.apply()

        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarAfterwakeBinding.inflate(layoutInflater)

        extrapillListInit()
        initextrapilladapter()

        loadSymptomList()

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
                    intent.putExtra("putsymptomName", symptom.sympotmname) // 예시로 증상의 이름을 넘김
                    val symptomName = symptom.sympotmname
                    startActivity(intent)
                }
            ).build(symptomList)

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

//            // 기존 데이터에 새로운 아이템을 추가
//            sympotmList.add(newSymptomItem)

            // 추가된 아이템을 리사이클러뷰에 반영
//            binding.calendarAfterwakeChangeSymptomRv.adapter?.notifyItemInserted(sympotmList.size - 1)
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
        return binding.root

    }
    private fun initextrapilladapter() {
        extrapilladapter = CalendarItemExtraPillAdapter(extrapillList)
        binding.calendarAfterwakeChangeExtraPillRv.adapter = extrapilladapter
        binding.calendarAfterwakeChangeExtraPillRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
    }

    private fun extrapillListInit() {
        extrapillList.addAll(
            arrayListOf(
                DataItemExtraPill("타이레놀", "1정", "오전 11:00")
            )
        )
    }

    private fun loadSymptomList() {
        val preferences = requireActivity().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = preferences.getString("symptomList", null)
        symptomList = if (json != null) {
            gson.fromJson(json, Array<DataItemSymptom>::class.java).toMutableList()
        } else {
            mutableListOf()
        }
    }

}
