package com.example.ahyak.HomeRecord

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.FreeMedicineEntity
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

class MedicineAdapter() : RecyclerView.Adapter<MedicineAdapter.ViewHolder>() {

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

    var freeMedicineData : List<FreeMedicineEntity> = listOf()

    fun build(i: ArrayList<MedicineEntity>):MedicineAdapter{
        addpillList = i
        return this
    }

    inner class ViewHolder(val binding: ItemCalendarAddPillBinding, val context: Context) : RecyclerView.ViewHolder(binding.root), EffectInfoView{

        val authService = AuthService(context)

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

            //상세 약 조회
            binding.root.setOnClickListener {
                val position = adapterPosition
                var freeCheck = false
                if (position != RecyclerView.NO_POSITION) {
                    // 코루틴을 사용하여 백그라운드 스레드에서 데이터베이스 작업 실행
                    GlobalScope.launch(Dispatchers.IO) {
                        // 데이터베이스 초기화
                        ahyakDatabase = AhyakDataBase.getInstance(context)

                        //클릭한 약이 자유기록인지 확인
                        freeCheck = ahyakDatabase!!.getMedicineDao()?.getMedicineFree(addpill.id)!!

                        if (freeCheck == true){
                            freeMedicineData = ahyakDatabase!!.getFreeMedicineDao().getFreeMedicine(addpill.MedicineName)
                        }

                        withContext(Dispatchers.Main) {
                            if (freeCheck == true){
                                // BottomSheetDialog 표시
                                val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.activity_free_detail_pill, null)
                                val bottomSheetDialog = BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme)

                                // 데이터 바인딩
                                bottomSheetView.findViewById<TextView>(R.id.pill_name_tv).text = addpill.MedicineName
                                bottomSheetView.findViewById<TextView>(R.id.pill_shape_name_tv).text = freeMedicineData[0].FreeMedicineShape
                                bottomSheetView.findViewById<TextView>(R.id.pill_color_name_tv).text = freeMedicineData[0].FreeMedicineColor
                                bottomSheetView.findViewById<TextView>(R.id.pill_line_name_tv).text = freeMedicineData[0].FreeMedicineLine
                                bottomSheetView.findViewById<TextView>(R.id.pill_formulation_name_tv).text = freeMedicineData[0].FreeMedicineType

                                //shape 이미지 설정
                                val shapeDrawableId = getShapeDrawableId(freeMedicineData[0].FreeMedicineShape)
                                bottomSheetView.findViewById<ImageView>(R.id.pill_shape_iv).setImageResource(shapeDrawableId)

                                //color 이미지 설정
                                val ColorDrawableId = getColorDrawableId(freeMedicineData[0].FreeMedicineColor)
                                bottomSheetView.findViewById<ImageView>(R.id.pill_color_iv).setImageResource(ColorDrawableId)

                                //type 이미지 설정
                                val TypeDrawableId = getTypeDrawableId(freeMedicineData[0].FreeMedicineType)
                                bottomSheetView.findViewById<ImageView>(R.id.pill_formulation_iv).setImageResource(TypeDrawableId)

                                //Line 이미지 설정
                                val LineDrawableId = getLineDrawableId(freeMedicineData[0].FreeMedicineLine)
                                bottomSheetView.findViewById<ImageView>(R.id.pill_line_iv).setImageResource(LineDrawableId)

                                bottomSheetDialog.setContentView(bottomSheetView)
                                bottomSheetDialog.show()
                            }else{
                                val pillName = addpillList[position].MedicineName // 현재 아이템의 약 이름 가져오기
                                Log.d("pillName", "$pillName")
                                SendpillName = pillName
                                authService.seteffectInfoView(this@ViewHolder)
                                authService.effectInfo(pillName)
                            }
                        }
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
    ): MedicineAdapter.ViewHolder =
        ViewHolder(ItemCalendarAddPillBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)

    override fun onBindViewHolder(holder: MedicineAdapter.ViewHolder, position: Int) {
        holder.bind(addpillList[position])
    }

    override fun getItemCount(): Int = addpillList.size

    fun getShapeDrawableId(shape: String): Int {
        return when (shape) {
            "삼각형" -> R.drawable.pill_shape_triangle
            "사각형" -> R.drawable.pill_shape_rectangler
            "원형" -> R.drawable.pill_shape_circle_ic
            "타원형" -> R.drawable.pill_shape_oval
            "반원형" -> R.drawable.pill_shape_ellipse
            "마름모형" -> R.drawable.pill_shape_diamond
            "육각형" -> R.drawable.pill_shape_hexagon
            "팔각형" -> R.drawable.pill_shape_octagon
            "오각형" -> R.drawable.pill_shape_pentagon
            "정방형" -> R.drawable.pill_shape_oblong
            else -> R.drawable.pill_etc // 기본값 지정
        }
    }

    fun getColorDrawableId(shape: String): Int {
        return when (shape) {
            "하양" -> R.drawable.pill_color_white
            "노랑" -> R.drawable.pill_color_yellow
            "주황" -> R.drawable.pill_color_orange
            "분홍" -> R.drawable.pill_color_pink
            "빨강" -> R.drawable.pill_color_red
            "갈색" -> R.drawable.pill_color_brown
            "연두" -> R.drawable.pill_color_lightgreen
            "초록" -> R.drawable.pill_color_green
            "검정" -> R.drawable.pill_color_black
            "남색" -> R.drawable.pill_color_navy
            "파랑" -> R.drawable.pill_color_blue
            "자주" -> R.drawable.pill_color_purple
            "보라" -> R.drawable.pill_color_violet
            "청록" -> R.drawable.pill_color_greenblue
            "투명" -> R.drawable.pill_color_transparency
            else -> R.drawable.pill_etc // 기본값 지정
        }
    }

    fun getTypeDrawableId(shape: String): Int {
        return when (shape) {
            "정제" -> R.drawable.pill_formulation_tablet
            "경질캡슐" -> R.drawable.pill_formulation_reshuffle
            "정제캡슐" -> R.drawable.pill_formulation_soft
            else -> R.drawable.pill_etc // 기본값 지정
        }
    }

    fun getLineDrawableId(shape: String): Int {
        return when (shape) {
            "+" -> R.drawable.pill_line_plus
            "-" -> R.drawable.pill_line_minus
            "없음" -> R.drawable.pill_line_none
            else -> R.drawable.pill_line_etc // 기본값 지정
        }
    }


}