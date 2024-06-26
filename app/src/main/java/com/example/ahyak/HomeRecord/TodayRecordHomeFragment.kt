package com.example.ahyak.HomeRecord

import android.content.BroadcastReceiver
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

class TodayRecordHomeFragment : Fragment() {

    private lateinit var binding: FragmentCalendarAfterwakeBinding
    private var extrapillList : ArrayList<ExtraPillEntity> = arrayListOf()
    private var extrapilladapter : ExtraPillAdapter?= null
    private var symptomList: MutableList<PrescriptionEntity> = mutableListOf()

    var selectedSlot : String? = ""
    var selectedDay : Int? = null
    var selectedMonth : Int? = null

    //데이터 베이스 객체
    var ahyakDatabase : AhyakDataBase? = null
    var alarmTime : String = ""



    override fun onResume() {
        super.onResume()

        //알람 관련
        val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        selectedMonth = sharedPref.getInt("selectedMonth", 0)
        selectedDay = sharedPref.getInt("selectedDay", 0)
        selectedSlot = sharedPref.getString("selectSlot", "기상 직후")

        if(selectedSlot == "기상 직후"){
            alarmTime = sharedPref.getString("alarmTime wakeup","정해진 시간 없음").toString()
        }else if(selectedSlot == "아침"){
            alarmTime = sharedPref.getString("alarmTime morning","정해진 시간 없음").toString()
        }else if(selectedSlot == "점심"){
            alarmTime = sharedPref.getString("alarmTime lunch","정해진 시간 없음").toString()
        }else if(selectedSlot == "저녁"){
            alarmTime = sharedPref.getString("alarmTime dinner","정해진 시간 없음").toString()
        }else{
            alarmTime = sharedPref.getString("alarmTime night","정해진 시간 없음").toString()
        }

        binding.calendarAfterwakeTimeTv.text = alarmTime

        GlobalScope.launch(Dispatchers.IO) {

            // 데이터베이스 초기화
            ahyakDatabase = AhyakDataBase.getInstance(requireContext())

            symptomList.clear()
            extrapillList.clear()

            // 데이터베이스에서 데이터 가져오기 - 월/일/시간대 정보 전송(Prescription)
            val NewsymptomList = ahyakDatabase!!.getPrescriptionDao().getPrescription(selectedMonth, selectedDay, selectedSlot).toMutableList()

            // 데이터베이스에서 데이터 가져오기 - 월/일/시간대 정보 전송(추가 약 기록)
            val NewPillList = ahyakDatabase!!.getExtraPillDao().getPill(selectedMonth, selectedDay, selectedSlot)

            // 리사이클러뷰 아이템 구성
            symptomList.addAll(NewsymptomList)
            extrapillList.addAll(NewPillList)

            binding.calendarAfterwakeChangeSymptomRv.adapter?.notifyDataSetChanged()
            binding.calendarAfterwakeChangeExtraPillRv.adapter?.notifyDataSetChanged()

            //특정 항목 삭제
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트1")
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트2")
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트3")
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트4")
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트5")
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트6")
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트7")
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트8")
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트9")
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트11")
//            ahyakDatabase!!.getPrescriptionDao()?.deletePrescription("테스트10")

            //테이블 전체 삭제
            //ahyakDatabase!!.getPrescriptionDao().deleteAllPrescriptions()
            //symptomList.addAll(ahyakDatabase!!.getPrescriptionDao().getAllPrescriptions())

            //화면에 적용할 내용
            withContext(Dispatchers.Main) {
                symptomList.clear()
                extrapillList.clear()

                // 리사이클러뷰 아이템 구성
                symptomList.addAll(NewsymptomList)
                extrapillList.addAll(NewPillList)

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

        //sharedPref 변수 선언
        val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        //추가 약 기록 어댑터 초기화
        initextrapilladapter()

        //오늘의 증상 기록하기 누르면
        binding.todayRecordLl.setOnClickListener {
            val intent = Intent(getActivity(), RecordSymptomsActivity::class.java)
            startActivity(intent)
        }

        binding.calendarAfterwakeChangeSymptomRv.apply {
            // 어댑터를 미리 초기화하고 리사이클러뷰에 설정
            val adapter = PrescriptionAdapter(
                onClick = { ->
                    // 선택된 아이템 클릭 이벤트
                }
            ) { symptom ->
                //약 추가하기 버튼 누르면 -> 새로운 약 추가 이벤트
                val intent = Intent(requireContext(), RegisterPillActivity::class.java)
                editor.putString("prescriptionName", symptom.Prescription)
                editor.putString("endDate", symptom.End_Date)
                editor.apply()
                startActivity(intent)
            }.build(symptomList)

            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }

        //알림 시간 설정하기
        binding.calendarAfterwakeChangeTimeLl.setOnClickListener {
            val intent = Intent(requireContext(), MedicationTimeActivity::class.java)
            intent.putExtra("alarmTimeSlot", selectedSlot) // 예시로 증상의 이름을 넘김
            startActivity(intent)
        }

        //증상 추가하기 누르면
        binding.todayRecordPrescriptionLl.setOnClickListener {
            val intent = Intent(requireContext(), AddPrescriptionActivity::class.java)
            parentFragmentManager.popBackStack()
            startActivity(intent)
        }

        //추가 약 기록에서 약 추가하기 눌렀을 때
        binding.calendarAfterwakeChangeAddPillLl.setOnClickListener {
            val intent = Intent(requireContext(), ExtraRegisterPillActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }


    //추가 약 기록 어댑터 등록
    private fun initextrapilladapter() {
        extrapilladapter = ExtraPillAdapter(extrapillList)
        binding.calendarAfterwakeChangeExtraPillRv.adapter = extrapilladapter
        binding.calendarAfterwakeChangeExtraPillRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
    }
}
