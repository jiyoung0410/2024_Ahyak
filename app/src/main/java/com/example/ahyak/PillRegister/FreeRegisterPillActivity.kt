package com.example.ahyak.PillRegister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityFreeRegisterPillBinding

class FreeRegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFreeRegisterPillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFreeRegisterPillBinding.inflate(layoutInflater)

        binding.freeRecordPillSerachForNameEt.imeOptions = EditorInfo.IME_ACTION_DONE

        //모양을 클릭했을 때
        val shapeList = listOf(
            binding.freeRecordPillShapeCircleLl,
            binding.freeRecordPillShapeOvalLl,
            binding.freeRecordPillShapeEllipseLl,
            binding.freeRecordPillShapeTriangleLl,
            binding.freeRecordPillShapeRectanglerLl,
            binding.freeRecordPillShapeEtcLl)

        var selectedShapeId: Int = -1

        for(button in shapeList){
            button.setOnClickListener {
                //이전에 선택된 버튼의 배경색을 원래대로 되돌리기
                if (selectedShapeId != -1) {
                    val previousButton = findViewById<LinearLayout>(selectedShapeId)
                    previousButton.setBackgroundResource(R.drawable.white_gray3_stroke)
                }

                // 클릭된 버튼의 배경색을 변경하기
                button.setBackgroundResource(R.drawable.white_point_stroke)

                // 클릭된 버튼의 아이디 저장하기
                selectedShapeId = button.id
            }
        }

        //색상을 클릭했을 때
        val colorList = listOf(
            binding.freeRecordPillColorWhiteLl,
            binding.freeRecordPillColorBrownLl,
            binding.freeRecordPillColorYellowLl,
            binding.freeRecordPillColorOrangeLl,
            binding.freeRecordPillColorPinkLl,
            binding.freeRecordPillColorRedLl,
            binding.freeRecordPillColorGreenLl,
            binding.freeRecordPillColorLightgreenLl,
            binding.freeRecordPillColorBlackLl,
            binding.freeRecordPillColorEtcLl)

        var selectedColorId: Int = -1

        for(button in colorList){
            button.setOnClickListener {
                //이전에 선택된 버튼의 배경색을 원래대로 되돌리기
                if (selectedColorId != -1) {
                    val previousButton = findViewById<LinearLayout>(selectedColorId)
                    previousButton.setBackgroundResource(R.drawable.white_gray3_stroke)
                }

                // 클릭된 버튼의 배경색을 변경하기
                button.setBackgroundResource(R.drawable.white_point_stroke)

                // 클릭된 버튼의 아이디 저장하기
                selectedColorId = button.id
            }
        }

        //제형을 클릭했을 때
        val formulationList = listOf(
            binding.freeRecordPillFormulationTabletLl,
            binding.freeRecordPillFormulationEtcLl,
            binding.freeRecordPillFormulationReshffleLl,
            binding.freeRecordPillFormulationSoftLl)

        var selectedFormulationId: Int = -1

        for(button in formulationList){
            button.setOnClickListener {
                //이전에 선택된 버튼의 배경색을 원래대로 되돌리기
                if (selectedFormulationId != -1) {
                    val previousButton = findViewById<LinearLayout>(selectedFormulationId)
                    previousButton.setBackgroundResource(R.drawable.white_gray3_stroke)
                }

                // 클릭된 버튼의 배경색을 변경하기
                button.setBackgroundResource(R.drawable.white_point_stroke)

                // 클릭된 버튼의 아이디 저장하기
                selectedFormulationId = button.id
            }
        }

        //분할선을 클릭했을 때
        val lineList = listOf(
            binding.freeRecordPillLineEtcLl,
            binding.freeRecordPillLineMinusLl,
            binding.freeRecordPillLineNoLl,
            binding.freeRecordPillLinePlusLl)

        var selectedLineId: Int = -1

        for(button in lineList){
            button.setOnClickListener {
                //이전에 선택된 버튼의 배경색을 원래대로 되돌리기
                if (selectedLineId != -1) {
                    val previousButton = findViewById<LinearLayout>(selectedLineId)
                    previousButton.setBackgroundResource(R.drawable.white_gray3_stroke)
                }

                // 클릭된 버튼의 배경색을 변경하기
                button.setBackgroundResource(R.drawable.white_point_stroke)

                // 클릭된 버튼의 아이디 저장하기
                selectedLineId = button.id
            }
        }

        //'x'버튼 눌렀을때
        binding.freeRecordPillCancleIv.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
    }
}
