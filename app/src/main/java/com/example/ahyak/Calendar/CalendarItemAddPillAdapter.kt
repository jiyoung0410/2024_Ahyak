package com.example.ahyak.Calendar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.MedicineEntity
import com.example.ahyak.PillDetailGuide.DetailPillActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemCalendarAddPillBinding
import com.example.ahyak.remote.AuthService
import com.example.ahyak.remote.EffectInfoResponseResult
import com.example.ahyak.remote.EffectInfoView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarItemAddPillAdapter() : RecyclerView.Adapter<CalendarItemAddPillAdapter.ViewHolder>() {

    private var addpillList: ArrayList<MedicineEntity> = arrayListOf()

    var takepill : Boolean = false
    var SendpillName : String? = ""

    var selectedDay : Int? = null
    var selectedMonth : Int? = null
    var selectedSlot : String? = "기상 직후"
    var prescriptionName : String? = ""

    //복용 여부
    var MedicineTAKE : Boolean? = true
    var MedicineId : Int? = 0

    //데이터 베이스 객체
    var ahyakDatabase : AhyakDataBase? = null

    fun build(i: ArrayList<MedicineEntity>):CalendarItemAddPillAdapter{
        addpillList = i
        return this
    }

    inner class ViewHolder(val binding: ItemCalendarAddPillBinding, val context: Context) : RecyclerView.ViewHolder(binding.root), EffectInfoView{

        val authService = AuthService(context)


    init {
            //약을 longClick하면 약 상세 정보 화면으로 intent
            // 아이템 뷰에 LongClickListener 추가
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val pillName = addpillList[position].MedicineName // 현재 아이템의 약 이름 가져오기
                    Log.d("pillName", "$pillName")
                    SendpillName = pillName
                    authService.seteffectInfoView(this@ViewHolder)
                    authService.effectInfo(pillName)
                }
                true // LongClickListener가 이벤트를 소비했음을 나타냄
            }

            //자유기록한 약 click했을 때 상세 정보 화면으로 intent (임시)
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                    // BottomSheetDialog 표시
                    val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.activity_free_detail_pill, null)
                    val bottomSheetDialog = BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme)

                    bottomSheetDialog.setContentView(bottomSheetView)
                    bottomSheetDialog.show()

                }
                true // LongClickListener가 이벤트를 소비했음을 나타냄
            }
        }

        fun bind(addpill: MedicineEntity){
            binding.itemCalendarPillVolumeTv.text = addpill.MedicineVolume.toString() + addpill.MedicineType
            binding.itemCalendarPillNameTv.text = addpill.MedicineName

            // 복용 상태에 따라 layout 스타일 변경
            updateTakeStatus(addpill.MedicineTake)

            // 복용 상태 토글 리스너
            binding.itemCalendarPillTakeStateLl.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    //데이터베이스 초기화
                    ahyakDatabase = AhyakDataBase.getInstance(context)

                    val newTakeState = !ahyakDatabase!!.getMedicineDao()?.getMedicineTake(addpill.id)!!
                    ahyakDatabase?.getMedicineDao()?.updateMedicineTakeStatus(addpill.id, newTakeState)

                    withContext(Dispatchers.Main) {
                        addpill.MedicineTake = newTakeState
                        updateTakeStatus(newTakeState)
                    }
                }
            }
        }

        private fun updateTakeStatus(isTaken: Boolean) {
            if (isTaken) {
                binding.itemCalendarPillTakeStateLl.setBackgroundResource(R.drawable.white_radi_50dp_gray1_stroke)
                binding.itemCalendarPillEatStateTv.setTextColor(Color.GRAY)
                binding.itemCalendarPillEatStateTv.text = "완료"
            } else {
                binding.itemCalendarPillTakeStateLl.setBackgroundResource(R.drawable.point_radi_50dp)
                binding.itemCalendarPillEatStateTv.setTextColor(Color.WHITE)
                binding.itemCalendarPillEatStateTv.text = "복용"
            }
        }

        override fun EffectInfoLoading() {
        }

        override fun EffectInfoSuccess(effectresult: List<EffectInfoResponseResult>) {
            Log.d("EffectInfo activity success", effectresult.toString())
            val context = binding.root.context
            val intent = Intent(context, DetailPillActivity::class.java)

            var effectList = ""
            var cautionList = ""
            var drugFoodList = ""
            var sideEffectList = ""

            for (result in effectresult) {
                effectList += "${result.effect}\n"
                cautionList += "${result.caution}\n"
                drugFoodList += "${result.drug_food}\n"
                sideEffectList += "${result.side_effect}\n"
            }

            intent.putExtra("sendpillName", SendpillName) // 데이터를 Intent에 추가
            intent.putExtra("effect", effectList) // 데이터를 Intent에 추가
            intent.putExtra("caution", cautionList) // 데이터를 Intent에 추가
            intent.putExtra("drugFood", drugFoodList) // 데이터를 Intent에 추가
            intent.putExtra("sideEffect", sideEffectList) // 데이터를 Intent에 추가
            //Log.d("effectresult print", "$effectresult")
            // 여기서 아이템에 대한 정보를 인텐트에 추가할 수 있음
            context.startActivity(intent)

            Log.d("effectresult each print", "effect $effectList, caution $cautionList, drug_food $drugFoodList, side_effect $sideEffectList")
        }
        override fun EffectInfoFailure() {
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarItemAddPillAdapter.ViewHolder =
        ViewHolder(ItemCalendarAddPillBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)

    override fun onBindViewHolder(holder: CalendarItemAddPillAdapter.ViewHolder, position: Int) {
        holder.bind(addpillList[position])
    }

    override fun getItemCount(): Int = addpillList.size

}