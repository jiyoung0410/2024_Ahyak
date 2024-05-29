package com.example.ahyak.PillRegister

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.FreeMedicineEntity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityFreeRegisterPillBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FreeRegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFreeRegisterPillBinding
    var selectshape : String = ""
    var selectcolor : String = ""
    var selecttype : String = ""
    var selectline : String = ""

    //약 이름 중복확인
    var existingMedicineNames :String = ""

    //데이터 베이스 객체
    var ahyakDatabase : AhyakDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFreeRegisterPillBinding.inflate(layoutInflater)

        binding.freeRecordPillSerachForNameEt.imeOptions = EditorInfo.IME_ACTION_DONE

        //각각 태그 설정
        binding.freeRecordPillShapeCircleLl.tag = "원형"
        binding.freeRecordPillShapeOvalLl.tag = "타원형"
        binding.freeRecordPillShapeEllipseLl.tag = "반원형"
        binding.freeRecordPillShapeTriangleLl.tag = "삼각형"
        binding.freeRecordPillShapeRectanglerLl.tag = "사각형"
        binding.freeRecordPillShapeDiamondLl.tag = "마름모형"
        binding.freeRecordPillShapeHexagonLl.tag = "육각형"
        binding.freeRecordPillShapeOblongLl.tag = "장방형"
        binding.freeRecordPillShapeOctagonLl.tag = "팔각형"
        binding.freeRecordPillShapePentagonLl.tag = "오각형"
        binding.freeRecordPillShapeEtcLl.tag = "기타"

        //모양을 클릭했을 때
        val shapeList = listOf(
            binding.freeRecordPillShapeCircleLl,
            binding.freeRecordPillShapeOvalLl,
            binding.freeRecordPillShapeEllipseLl,
            binding.freeRecordPillShapeTriangleLl,
            binding.freeRecordPillShapeRectanglerLl,
            binding.freeRecordPillShapeDiamondLl,
            binding.freeRecordPillShapeHexagonLl,
            binding.freeRecordPillShapeOblongLl,
            binding.freeRecordPillShapeOctagonLl,
            binding.freeRecordPillShapePentagonLl,
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
                val selectedShapeTag = it.tag as? String // 클릭된 버튼의 태그 읽어오기
                if (selectedShapeTag != null) {
                    selectshape = selectedShapeTag
                } else {
                    selectshape = "기타"
                }
            }
        }

        //각각 태그 설정
        binding.freeRecordPillColorWhiteLl.tag = "하양"
        binding.freeRecordPillColorBrownLl.tag = "갈색"
        binding.freeRecordPillColorYellowLl.tag = "노랑"
        binding.freeRecordPillColorOrangeLl.tag = "주황"
        binding.freeRecordPillColorPinkLl.tag = "분홍"
        binding.freeRecordPillColorRedLl.tag = "빨강"
        binding.freeRecordPillColorGreenLl.tag = "초록"
        binding.freeRecordPillColorLightgreenLl.tag = "연두"
        binding.freeRecordPillColorBlackLl.tag = "검정"
        binding.freeRecordPillColorNavyLl.tag = "남색"
        binding.freeRecordPillColorBlueLl.tag = "파랑"
        binding.freeRecordPillColorPurpleLl.tag = "자주"
        binding.freeRecordPillColorVioletLl.tag = "보라"
        binding.freeRecordPillColorGreenbuleLl.tag = "청록"
        binding.freeRecordPillColorTransLl.tag = "투명"
        binding.freeRecordPillColorEtcLl.tag = "기타"

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
            binding.freeRecordPillColorNavyLl,
            binding.freeRecordPillColorBlueLl,
            binding.freeRecordPillColorPurpleLl,
            binding.freeRecordPillColorVioletLl,
            binding.freeRecordPillColorGreenbuleLl,
            binding.freeRecordPillColorTransLl,
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
                val selectedColorTag = it.tag as? String // 클릭된 버튼의 태그 읽어오기
                if (selectedColorTag != null) {
                    selectcolor = selectedColorTag
                } else {
                    selectcolor = "기타"
                }
            }
        }
        //각각 태그달기
        binding.freeRecordPillFormulationTabletLl.tag = "정제"
        binding.freeRecordPillFormulationEtcLl.tag = "기타"
        binding.freeRecordPillFormulationReshffleLl.tag = "경질캡슐"
        binding.freeRecordPillFormulationSoftLl.tag = "연질캡슐"

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
                val selectedformulationTag = it.tag as? String // 클릭된 버튼의 태그 읽어오기
                if (selectedformulationTag != null) {
                    selecttype = selectedformulationTag
                } else {
                    selecttype = "기타"
                }
            }
        }

        //분할선 각각 태그
        binding.freeRecordPillLineEtcLl.tag = "기타"
        binding.freeRecordPillLineMinusLl.tag = "-"
        binding.freeRecordPillLineNoLl.tag = "없음"
        binding.freeRecordPillLinePlusLl.tag = "+"

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

                val selectedLineTag = it.tag as? String // 클릭된 버튼의 태그 읽어오기
                if (selectedLineTag != null) {
                    selectline = selectedLineTag
                } else {
                    selectline = "기타"
                }
            }
        }

        binding.freeRecordPillSerachForNameEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.freeRecordPillSerachForNameEt.clearFocus() // EditTextView1의 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.freeRecordPillSerachForNameEt.windowToken, 0) // 키보드 숨김
                binding.freeRecordPillSerachForNameEt.requestFocus() // EditTextView2로 포커스 이동
                return@setOnEditorActionListener true
            }
            false
        }

        binding.freeRecordPillSerachCodeEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.freeRecordPillSerachCodeEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.freeRecordPillSerachCodeEt.clearFocus() // 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.freeRecordPillSerachCodeEt.windowToken, 0) // 키보드 숨김
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
        binding.root.setOnTouchListener { _, _ ->
            binding.freeRecordPillSerachForNameEt.clearFocus() // 포커스 해제
            binding.freeRecordPillSerachCodeEt.clearFocus() //포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.freeRecordPillSerachForNameEt.windowToken, 0) // 키보드 숨김
            inputMethodManager.hideSoftInputFromWindow(binding.freeRecordPillSerachCodeEt.windowToken, 0) // 키보드 숨김
            true
        }


        //'x'버튼 눌렀을때
        binding.freeRecordPillCancleIv.setOnClickListener {
            finish()
        }

        //저장하기 버튼 누르면
        binding.freeRecordPillSearchLl.setOnClickListener {

            //약 이름 정보
            val FreeMedicineName = binding.freeRecordPillSerachForNameEt.text.toString()

            //식별 정보
            val FreeMedicineCode = binding.freeRecordPillSerachCodeEt.text.toString()

            // 코루틴을 사용하여 백그라운드 스레드에서 데이터베이스 작업 실행
            GlobalScope.launch(Dispatchers.IO) {
                // 데이터베이스 초기화
                ahyakDatabase = AhyakDataBase.getInstance(this@FreeRegisterPillActivity)

                // 데이터베이스에서 해당 이름을 가진 자유기록 약이 있는지 불러오기
                val existingMedicineList =
                    ahyakDatabase!!.getFreeMedicineDao()?.getFreeMedicine(FreeMedicineName)

                // 약 이름만 추출
                existingMedicineNames = existingMedicineList?.map { it.FreeMedicineName }.toString()
            }
            if (existingMedicineNames.contains(FreeMedicineName)) {
                // 메인 스레드에서 Toast 메시지 표시
                    Toast.makeText(this, "이미 등록된 약입니다.", Toast.LENGTH_SHORT).show()

            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    //자유 약 기록하기
                    ahyakDatabase!!.getFreeMedicineDao()?.insertFreeMedicine(FreeMedicineEntity(
                        FreeMedicineName, FreeMedicineCode, selectshape, selectcolor, selecttype, selectline))

                    val existingMedicine2 = ahyakDatabase!!.getFreeMedicineDao().getFreeMedicine(FreeMedicineName)
                    Log.d("제대로 저장되었는지 check", "$existingMedicine2")
                }

                // 약 이름이 중복되지 않으면 다음 화면으로 이동
                val intent = Intent(this@FreeRegisterPillActivity, RegisterPillActivity::class.java)
                intent.putExtra("FreeMedicineName", FreeMedicineName)
                finish()
                startActivity(intent)
            }
        }
        setContentView(binding.root)
    }
}
