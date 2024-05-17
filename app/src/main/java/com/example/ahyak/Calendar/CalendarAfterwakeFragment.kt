package com.example.ahyak.Calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.AddPrescription.AddPrescriptionActivity
import com.example.ahyak.AddPrescription.MedicationTimeActivity
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.ExtraPillEntity
import com.example.ahyak.DB.PrescriptionEntity
import com.example.ahyak.PillRegister.ExtraRegisterPillActivity
import com.example.ahyak.PillRegister.RegisterPillActivity
import com.example.ahyak.RecordSymptoms.RecordSymptomsActivity
import com.example.ahyak.databinding.FragmentCalendarAfterwakeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarAfterwakeFragment : Fragment() {

    private lateinit var binding: FragmentCalendarAfterwakeBinding
    private var extrapillList : ArrayList<ExtraPillEntity> = arrayListOf()
    private var extrapilladapter : CalendarItemExtraPillAdapter?= null
    private var symptomList: MutableList<PrescriptionEntity> = mutableListOf()

    var extraPillInpoName: String? = null
    var extraPillInpoDosageSize:String? = null
    var extraPillInpoDosage:String? = null
    var extraPillformattedTime:String? = null

    var selectedSlot : String? = "기상 직후"
    var selectedDay : Int? = null
    var selectedMonth : Int? = null

    //데이터 베이스 객체
    var ahyakDatabase : AhyakDataBase? = null

    override fun onResume() {
        super.onResume()

        //알람 관련
        val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val alarmTime = sharedPref.getString("alarmTime","정해진 시간 없음")
        binding.calendarAfterwakeTimeTv.text = alarmTime

        selectedMonth = sharedPref.getInt("selectedMonth", 0)
        selectedDay = sharedPref.getInt("selectedDay", 0)
        sharedPref.edit().putString("selectedSlot", selectedSlot)
        Log.d("select day", "selectedMonth : $selectedMonth, dat : $selectedDay")

        // 코루틴을 사용하여 백그라운드 스레드에서 데이터베이스 작업 실행
        GlobalScope.launch(Dispatchers.IO) {

            // 데이터베이스 초기화
            ahyakDatabase = AhyakDataBase.getInstance(requireContext())
            symptomList.clear()
            extrapillList.clear()

            // 데이터베이스에서 데이터 가져오기 - 월/일/시간대 정보 전송
            val NewsymptomList = ahyakDatabase!!.getPrescriptionDao().getPrescription(selectedMonth, selectedDay, selectedSlot).toMutableList()
            symptomList.addAll(NewsymptomList)

            // 데이터베이스에서 데이터 가져오기 - 월/일/시간대 정보 전송
            val NewPillList = ahyakDatabase!!.getExtraPillDao().getPill(selectedMonth, selectedDay, selectedSlot)
            extrapillList.addAll(NewPillList)


            //특정 항목 삭제
            //ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("처방5")

            //테이블 전체 삭제
            //ahyakDatabase!!.getPrescriptionDao().deleteAllPrescriptions()
            //symptomList.addAll(ahyakDatabase!!.getPrescriptionDao().getAllPrescriptions())

            withContext(Dispatchers.Main) {
                // 리사이클러뷰 아이템 구성
                binding.calendarAfterwakeChangeSymptomRv.adapter?.notifyDataSetChanged()
                binding.calendarAfterwakeChangeExtraPillRv.adapter?.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarAfterwakeBinding.inflate(layoutInflater)

        // 코루틴을 사용하여 백그라운드 스레드에서 데이터베이스 작업 실행
        GlobalScope.launch(Dispatchers.IO) {

            //데이터베이스 초기화
            ahyakDatabase = AhyakDataBase.getInstance(requireContext())

            //추가약 기록
//            ahyakDatabase!!.getExtraPillDao()?.insertPill(
//                ExtraPillEntity("타이레놀", 5, 17, "기상 직후", 1.0f, "정", "오전11:00")
//            )

            //증상 기록
//            ahyakDatabase!!.getPrescriptionDao()?.insertPrescription(
//                PrescriptionEntity("처방1", 5, 17, "기상 직후", "병원3", "2024.05.17", "2025.06.30"))
//            ahyakDatabase!!.getPrescriptionDao()?.insertPrescription(
//                PrescriptionEntity("처방2", 5, 17, "기상 직후", "병원3", "2024.05.17", "2025.06.30"))
        }

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
                }
            ) { symptom ->
                //약 추가하기 버튼 누르면
                // 새로운 약 추가 이벤트
                val intent = Intent(requireContext(), RegisterPillActivity::class.java)
//                    intent.putExtra("putsymptomName", symptom.sympotmname) // 예시로 증상의 이름을 넘김
//                    val symptomName = symptom.sympotmname
                startActivity(intent)
            }.build(symptomList)

            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }

        binding.calendarAfterwakeChangeTimeLl.setOnClickListener {
            val intent = Intent(requireContext(), MedicationTimeActivity::class.java)
            startActivity(intent)
        }

        binding.calendarAfterwakeAddSymptomLl.setOnClickListener {
            val intent = Intent(requireContext(), AddPrescriptionActivity::class.java)
            startActivity(intent)
        }

        //추가 약 기록에서 약 추가하기 눌렀을 때
        binding.calendarAfterwakeChangeAddPillLl.setOnClickListener {
            val intent = Intent(requireContext(), ExtraRegisterPillActivity::class.java)
            startActivity(intent)
        }

//        //Intent를 통해 전달된 데이터를 받음
//        val intent = activity?.intent
//        if (intent != null) {
//            extraPillInpoName = intent.getStringExtra("extraPillInpoName") ?: ""
//            extraPillInpoDosageSize = intent.getStringExtra("extraPillInpoDosageSize") ?: ""
//            extraPillInpoDosage = intent.getStringExtra("extraPillInpoDosage") ?: ""
//            extraPillformattedTime = intent.getStringExtra("formattedTime") ?: ""
//
//            // 모든 값이 정상적으로 전달되었는지 확인
//            if (extraPillInpoName!!.isNotEmpty() && extraPillInpoDosageSize!!.isNotEmpty() &&
//                extraPillInpoDosage!!.isNotEmpty() && extraPillformattedTime!!.isNotEmpty()) {
//
//                // 전달된 데이터를 사용하여 새로운 아이템 생성
//                val newExtraPillItem = DataItemExtraPill(extraPillInpoName!!, "$extraPillInpoDosageSize$extraPillInpoDosage", extraPillformattedTime!!)
//                // 기존 데이터에 새로운 아이템을 추가
//                extrapillList.add(newExtraPillItem)
//                // 추가된 아이템을 리사이클러뷰에 반영
//                extrapilladapter?.notifyItemInserted(extrapillList.size - 1)
//            } else {
//                // 전달된 데이터가 비어있을 경우 처리할 내용 추가
//            }
//        }
        return binding.root

    }
    private fun initextrapilladapter() {
        extrapilladapter = CalendarItemExtraPillAdapter(extrapillList)
        binding.calendarAfterwakeChangeExtraPillRv.adapter = extrapilladapter
        binding.calendarAfterwakeChangeExtraPillRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
    }

}
