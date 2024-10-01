package com.example.ahyak.OCR

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.MedicineEntity
import com.example.ahyak.DB.PrescriptionEntity
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemOcrResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class OcrPillAdapter(
    private val ocrDrugLists: ArrayList<OcrDrugInfo>,
    private val context: Context,
    private val db: AhyakDataBase,
    private val prescriptionName: String// 약 정보 리스트를 DrugInfo로 수정
) : RecyclerView.Adapter<OcrPillAdapter.PillViewHolder>() {

    inner class PillViewHolder(val binding: ItemOcrResultBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(drugInfo: OcrDrugInfo) {
            // DrugInfo 데이터를 pill_info_tv에 반영
            binding.pillInfoTv.text =
                    "약 이름: ${drugInfo.name}\n" +
                    "1회 투여량: ${drugInfo.dosePerTime}\n" +
                    "1일 투여 횟수: ${drugInfo.dosesPerDay}\n" +
                    "총 투약 일수: ${drugInfo.totalDays}"

            // 삭제 버튼 클릭
            binding.ocrDeleteBtn.setOnClickListener {
                val position = adapterPosition // 삭제할 아이템의 위치를 가져옴
                if (position != RecyclerView.NO_POSITION) {
                    // 리스트에서 해당 아이템을 삭제
                    ocrDrugLists.removeAt(position)

                    // 어댑터에 변경 사항을 알림
                    notifyItemRemoved(position)
                }
            }

            // 완료 버튼 클릭
            binding.ocrCompleteBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val calendar = Calendar.getInstance()

                    val totalDays = drugInfo.totalDays.toInt() // 총 투약 일수
                    val dosesPerDay = drugInfo.dosesPerDay.toInt() // 1일 투여 횟수

                    // 투약 횟수에 따라 Morning, Afternoon, Evening을 설정
                    val slots = when (dosesPerDay) {
                        1 -> listOf("아침")
                        2 -> listOf("아침", "저녁")
                        3 -> listOf("아침", "점심", "저녁")
                        else -> emptyList()
                    }

                    // 총 투약 일수 동안 반복하여 날짜와 투약 슬롯별로 약 정보를 등록
                    for (day in 0 until totalDays) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1) // 하루씩 증가
                        val month = calendar.get(Calendar.MONTH) + 1
                        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) - 1

                        slots.forEach { slot ->

                            db!!.getMedicineDao()?.insertMedicine(
                                MedicineEntity(
                                    drugInfo.name,
                                    prescriptionName,
                                    month,
                                    dayOfMonth,
                                    slot,
                                    dosesPerDay.toFloat(),
                                    "정", // 정제 타입
                                    false,
                                    false
                                )
                            )

                            val existingMedicineList =
                                db!!.getMedicineDao()
                                    .getMedicine(month, dayOfMonth, slot, prescriptionName)
                            Log.d("register Medicine check", "$existingMedicineList")

                            // 하루씩 증가
                            calendar.add(Calendar.DAY_OF_MONTH, 1) // 다음 날로 이동
                        }
                    }
                    // 비동기 작업 완료 후 UI 업데이트는 메인 스레드에서 실행
                    withContext(Dispatchers.Main) {

                        binding.ocrDeleteBtn.visibility = View.GONE
                        binding.ocrCompleteBtn.visibility = View.GONE
                        binding.ocrDoneBtn.visibility = View.VISIBLE
                    }
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillViewHolder {
        val binding = ItemOcrResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PillViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PillViewHolder, position: Int) {
        holder.bind(ocrDrugLists[position])
    }

    override fun getItemCount(): Int {
        return ocrDrugLists.size
    }
}
