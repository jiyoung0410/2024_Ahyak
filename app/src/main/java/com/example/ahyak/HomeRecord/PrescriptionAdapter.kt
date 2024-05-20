package com.example.ahyak.HomeRecord

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.MedicineEntity
import com.example.ahyak.DB.PrescriptionEntity
import com.example.ahyak.databinding.ItemCalendarSymptomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrescriptionAdapter(val onClick: () -> Unit, val onAddPillClick: (PrescriptionEntity) -> Unit) : RecyclerView.Adapter<PrescriptionAdapter.ViewHolder>() {

    lateinit var sympotms: MutableList<PrescriptionEntity>
    private var selectedPosition = -1 // 선택된 아이템의 위치를 저장하는 변수 추가
    private var medicines : ArrayList<MedicineEntity> = arrayListOf() // Medicine List

    //데이터 베이스 객체
    var ahyakDatabase : AhyakDataBase? = null

    var selectedBoolean = true

    fun build(i: MutableList<PrescriptionEntity>) : PrescriptionAdapter{
        sympotms = i
        return this
    }

    inner class ViewHolder(val binding: ItemCalendarSymptomBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){

        // 아래의 코드를 추가하여 item_addpill의 어댑터에 접근할 수 있도록 합니다.
        private var addPillAdapter: MedicineAdapter = MedicineAdapter()
        init {
            binding.itemCalendarSymptomPillRv.adapter = addPillAdapter
        }
        fun bind(sympotm:PrescriptionEntity) {

            binding.itemCalendarAddSymptomPillLl.setOnClickListener {
                // 아이템 추가 이벤트 발생
                onAddPillClick(sympotm)
            }

            binding.itemCalendarSymptomName.text = sympotm.Prescription
            binding.itemCalendarSymptomDate.text = sympotm.Start_Date
            binding.itemCalendarSymptomHospitalName.text = sympotm.Hospital
            binding.itemCalendarSymptomPillRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                binding.root.setOnClickListener {
                    // 해당 증상에 대한 약 데이터 가져오기
                    var sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)
                    var selectedMonth = sharedPref.getInt("selectedMonth", 0)
                    var selectedDay = sharedPref.getInt("selectedDay", 0)
                    var selectedSlot = sharedPref.getString("selectSlot", "")

                    loadMedicinesForPrescription(sympotm.Prescription, selectedMonth, selectedDay, selectedSlot!!)

                    selectedPosition = adapterPosition // 현재 선택된 아이템의 위치 저장
                    if (selectedBoolean) {
                        binding.itemCalendarAddSymptomPillLl.visibility = View.VISIBLE
                        binding.itemCalendarSymptomDate.visibility = View.GONE
                        binding.itemCalendarSymptomPillRv.visibility = View.VISIBLE
                        binding.itemCalendarSymptomHospitalName.visibility = View.INVISIBLE
                        selectedBoolean = false
                    } else {
                        binding.itemCalendarAddSymptomPillLl.visibility = View.GONE
                        binding.itemCalendarSymptomDate.visibility = View.VISIBLE
                        binding.itemCalendarSymptomPillRv.visibility = View.GONE
                        binding.itemCalendarSymptomHospitalName.visibility = View.VISIBLE
                        selectedBoolean = true
                    }

                    onClick() // 생성자 파라미터로 받은 람다함수 onClick 실행
                }
            }
        }

        private fun loadMedicinesForPrescription(prescription: String, month:Int,day : Int, slot : String ) {
            // 증상에 해당하는 약 데이터를 데이터베이스에서 가져오기
            GlobalScope.launch(Dispatchers.IO) {

                //데이터베이스 초기화
                ahyakDatabase = AhyakDataBase.getInstance(context)
                medicines.clear()

                // 데이터베이스에서 데이터 가져오기 - 월/일/시간대 정보 전송
                val NewmedicineList = ahyakDatabase!!.getMedicineDao().getMedicine(month, day, slot, prescription)
                Log.d("Slot Check", "Slot : $slot, Data : $NewmedicineList")
                medicines.addAll(NewmedicineList)

                withContext(Dispatchers.Main) {
                    // 가져온 약 데이터를 리사이클러뷰에 표시
                    addPillAdapter.build(medicines)
                    addPillAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionAdapter.ViewHolder =
        ViewHolder(
            ItemCalendarSymptomBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context
        )

    override fun onBindViewHolder(holder: PrescriptionAdapter.ViewHolder, position: Int) {
        holder.bind(sympotms[position])
    }

    override fun getItemCount(): Int = sympotms.size
}
