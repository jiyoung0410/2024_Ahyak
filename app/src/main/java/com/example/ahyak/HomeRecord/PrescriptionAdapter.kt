package com.example.ahyak.HomeRecord

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.AddPrescription.AddPrescriptionActivity
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.AuthService
import com.example.ahyak.DB.HomeStatusView
import com.example.ahyak.DB.MedicineEntity
import com.example.ahyak.DB.PrescriptionEntity
import com.example.ahyak.DB.PrescriptionItemView
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemCalendarSymptomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrescriptionAdapter(
    private val context: Context,
    val onClick: () -> Unit,
    val onAddPillClick: (PrescriptionEntity) -> Unit) : RecyclerView.Adapter<PrescriptionAdapter.ViewHolder>(), PrescriptionItemView {

    lateinit var sympotms: MutableList<PrescriptionEntity>
    private var selectedPosition = -1 // 선택된 아이템의 위치를 저장하는 변수 추가
    private var optionPosition = -1 // 수정 혹은 삭제할 아이템의 위치를 저장하는 변수 추가
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
        fun bind(sympotm: PrescriptionEntity) {

            binding.itemCalendarAddSymptomPillLl.setOnClickListener {
                // 아이템 추가 이벤트 발생
                onAddPillClick(sympotm)
            }

            var sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)
            var selectedMonth = sharedPref.getInt("selectedMonth", 0)
            var selectedDay = sharedPref.getInt("selectedDay", 0)
            var selectedSlot = sharedPref.getString("selectSlot", "")

            binding.itemCalendarSymptomName.text = sympotm.Prescription
            binding.itemCalendarSymptomDate.text = sympotm.Start_Date
            binding.itemCalendarSymptomHospitalName.text = sympotm.Hospital
            binding.itemCalendarSymptomPillRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                loadMedicinesForPrescription(sympotm.Prescription, selectedMonth, selectedDay, selectedSlot!!)

                binding.root.setOnClickListener {
                    // 해당 증상에 대한 약 데이터 가져오기
                    //loadMedicinesForPrescription(sympotm.Prescription, selectedMonth, selectedDay, selectedSlot!!)

                    selectedPosition = adapterPosition // 현재 선택된 아이템의 위치 저장
                    if (selectedBoolean) {
                        binding.itemCalendarAddSymptomPillLl.visibility = View.VISIBLE
                        binding.itemCalendarSymptomDate.visibility = View.GONE
                        binding.itemCalendarSymptomPillRv.visibility = View.VISIBLE
                        binding.itemCalendarSymptomHospitalName.visibility = View.INVISIBLE
                        binding.itemCalendarSymptomMoreCl.visibility = View.GONE
                        selectedBoolean = false
                    } else {
                        binding.itemCalendarAddSymptomPillLl.visibility = View.GONE
                        binding.itemCalendarSymptomDate.visibility = View.VISIBLE
                        binding.itemCalendarSymptomPillRv.visibility = View.GONE
                        binding.itemCalendarSymptomHospitalName.visibility = View.VISIBLE
                        binding.itemCalendarSymptomMoreCl.visibility = View.VISIBLE
                        selectedBoolean = true
                    }

                    onClick() // 생성자 파라미터로 받은 람다함수 onClick 실행
                }
            }

            binding.itemCalendarSymptomMoreCl.setOnClickListener {
                val popupmenu = PopupMenu(context, binding.itemCalendarSymptomMoreCl)
                popupmenu.menuInflater.inflate(R.menu.menu_item_more, popupmenu.menu)

                popupmenu.setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.item_more_menu1 -> {
                            //수정 시 행동
                            val intent = Intent(binding.root.context, AddPrescriptionActivity::class.java).apply {
                                putExtra("presmodify_prescription", sympotm.Prescription)
                                putExtra("presmodify_hospital", sympotm.Hospital)
                                putExtra("presmodify_startdate", sympotm.Start_Date)
                                putExtra("presmodify_enddate",sympotm.End_Date)
                                putExtra("presmodify_id",sympotm.PrescriptionId)
                            }
                            //parentFragmentManager.popBackStack()
                            binding.root.context.startActivity(intent)
                            true
                        }
                        R.id.item_more_menu2 -> {
                            optionPosition = adapterPosition
                            if(optionPosition != RecyclerView.NO_POSITION) {
                                sympotms.removeAt(optionPosition)
                                GlobalScope.launch(Dispatchers.IO) {
//                                    ahyakDatabase = AhyakDataBase.getInstance(context)
//                                    ahyakDatabase!!.getMedicineDao().deletePrescriptionMedicine(sympotm.Prescription)
//                                    ahyakDatabase!!.getPrescriptionDao().deletePrescription(sympotm.Prescription)
//                                    withContext(Dispatchers.Main) {
//                                        notifyItemRemoved(position)
//                                        notifyDataSetChanged()
//                                    }
                                    val authService = AuthService(context)
                                    authService.setPrescriptionItemView(this@PrescriptionAdapter)
                                    authService.deletePrescription(sympotm)
                                }
                            }
                            true
                        }
                        else -> false
                    }
                }
                popupmenu.show()
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
    override fun DelPrescriptionLoading() {
    }

    override fun DelPrescriptionSuccess(prescription: PrescriptionEntity) {
        Log.d("Prescription Delete","Prescription Delete Success")
        GlobalScope.launch(Dispatchers.IO) {
            ahyakDatabase = AhyakDataBase.getInstance(context)
            ahyakDatabase!!.getMedicineDao().deletePrescriptionMedicine(prescription.Prescription)
            ahyakDatabase!!.getPrescriptionDao().deletePrescription(prescription.PrescriptionId)
            withContext(Dispatchers.Main) {
                notifyItemRemoved(optionPosition)
                notifyDataSetChanged()
            }
        }
    }

    override fun DelPrescriptionFailure() {
        Log.d("Prescription Delete", "Prescription Delete Failure")
    }
}
