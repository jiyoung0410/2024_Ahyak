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
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivitySearchPillBinding
import com.example.ahyak.remote.AuthService
import com.example.ahyak.remote.DrugSearchNameView
import com.example.ahyak.remote.DrugSearchShapeView
import com.example.ahyak.remote.RESULT

class SearchPillActivity : AppCompatActivity(), DrugSearchNameView, DrugSearchShapeView {

    private lateinit var binding : ActivitySearchPillBinding
    var selectshape : String = ""
    var selectcolor : String = ""
    var selectformulation : String = ""
    var selectline : String = ""
    var print_discrimination : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchPillBinding.inflate(layoutInflater)

        //API 연결
        val authService = AuthService(this@SearchPillActivity)
//        authService.setdrugSearchNameView(this)
//        authService.drugSearchName("타이레놀")

        //각각 태그 설정
        binding.searchPillShapeCircleLl.tag = "원형"
        binding.searchPillShapeOvalLl.tag = "타원형"
        binding.searchPillShapeEllipseLl.tag = "반원형"
        binding.searchPillShapeTriangleLl.tag = "삼각형"
        binding.searchPillShapeRectanglerLl.tag = "사각형"
        binding.searchPillShapeDiamondLl.tag = "마름모형"
        binding.searchPillShapeHexagonLl.tag = "육각형"
        binding.searchPillShapeOblongLl.tag = "장방형"
        binding.searchPillShapeOctagonLl.tag = "팔각형"
        binding.searchPillShapePentagonLl.tag = "오각형"
        binding.searchPillShapeEtcLl.tag = "NULL"

        //모양을 클릭했을 때
        val shapeList = listOf(
            binding.searchPillShapeCircleLl,
            binding.searchPillShapeOvalLl,
            binding.searchPillShapeEllipseLl,
            binding.searchPillShapeTriangleLl,
            binding.searchPillShapeRectanglerLl,
            binding.searchPillShapeDiamondLl,
            binding.searchPillShapeHexagonLl,
            binding.searchPillShapeOblongLl,
            binding.searchPillShapeOctagonLl,
            binding.searchPillShapePentagonLl,
            binding.searchPillShapeEtcLl)

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
                    selectshape = "NULL"
                }

            }
        }
        //각각 태그 설정
        binding.searchPillColorWhiteLl.tag = "하양"
        binding.searchPillColorBrownLl.tag = "갈색"
        binding.searchPillColorYellowLl.tag = "노랑"
        binding.searchPillColorOrangeLl.tag = "주황"
        binding.searchPillColorPinkLl.tag = "분홍"
        binding.searchPillColorRedLl.tag = "빨강"
        binding.searchPillColorGreenLl.tag = "초록"
        binding.searchPillColorLightgreenLl.tag = "연두"
        binding.searchPillColorBlackLl.tag = "검정"
        binding.searchPillColorNavyLl.tag = "남색"
        binding.searchPillColorBlueLl.tag = "파랑"
        binding.searchPillColorPurpleLl.tag = "자주"
        binding.searchPillColorVioletLl.tag = "보라"
        binding.searchPillColorGreenbuleLl.tag = "청록"
        binding.searchPillColorTransLl.tag = "투명"
        binding.searchPillColorEtcLl.tag = "NULL"

        //색상을 클릭했을 때
        val colorList = listOf(
            binding.searchPillColorWhiteLl,
            binding.searchPillColorBrownLl,
            binding.searchPillColorYellowLl,
            binding.searchPillColorOrangeLl,
            binding.searchPillColorPinkLl,
            binding.searchPillColorRedLl,
            binding.searchPillColorGreenLl,
            binding.searchPillColorLightgreenLl,
            binding.searchPillColorBlackLl,
            binding.searchPillColorNavyLl,
            binding.searchPillColorBlueLl,
            binding.searchPillColorPurpleLl,
            binding.searchPillColorVioletLl,
            binding.searchPillColorGreenbuleLl,
            binding.searchPillColorTransLl,
            binding.searchPillColorEtcLl)


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
                    selectcolor = "NULL"
                }
            }
        }
        //각각 태그달기
        binding.searchPillFormulationTabletLl.tag = "정제"
        binding.searchPillFormulationEtcLl.tag = "NULL"
        binding.searchPillFormulationReshffleLl.tag = "경질캡슐"
        binding.searchPillFormulationSoftLl.tag = "연질캡슐"

        //제형을 클릭했을 때
        val formulationList = listOf(
            binding.searchPillFormulationTabletLl,
            binding.searchPillFormulationEtcLl,
            binding.searchPillFormulationReshffleLl,
            binding.searchPillFormulationSoftLl)

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
                    selectformulation = selectedformulationTag
                } else {
                    selectformulation = "NULL"
                }
            }
        }
        //각각 태그
        binding.searchPillLineEtcLl.tag = "기타"
        binding.searchPillLineMinusLl.tag = "-"
        binding.searchPillLineNoLl.tag = "없음"
        binding.searchPillLinePlusLl.tag = "+"

        //분할선을 클릭했을 때
        val lineList = listOf(
            binding.searchPillLineEtcLl,
            binding.searchPillLineMinusLl,
            binding.searchPillLineNoLl,
            binding.searchPillLinePlusLl)

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
                    selectline = "NULL"
                }
                //edit_text에서 받아온 내용 저장
                print_discrimination = binding.serachPillSerachForShapeEt.text.toString()
                Toast.makeText(this, "$print_discrimination,$selectshape,$selectcolor,$selectformulation,$selectline", Toast.LENGTH_SHORT).show()
                authService.setdrugSearchShapeView(this)
                Log.d("Send Shape", "$print_discrimination,$selectshape,$selectcolor,$selectformulation,$selectline")
                authService.drugSearchShape(print_discrimination,selectshape,selectcolor,selectformulation,selectline)
                //authService.drugSearchShape("NULL", "타원형", "하양", "정제","+")
            }
        }

        binding.serachPillSerachForShapeEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.serachPillSerachForShapeEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.serachPillSerachForShapeEt.clearFocus() // 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.serachPillSerachForShapeEt.windowToken, 0) // 키보드 숨김
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
        binding.root.setOnTouchListener { _, _ ->
            binding.serachPillSerachForShapeEt.clearFocus() // 포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.serachPillSerachForShapeEt.windowToken, 0) // 키보드 숨김
            true
        }

        binding.searchLl.setOnTouchListener { _, _ ->
            binding.serachPillSerachForShapeEt.clearFocus() // 포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.serachPillSerachForShapeEt.windowToken, 0) // 키보드 숨김
            true
        }


        //'X'버튼 누르면
        binding.searchPillCancleIv.setOnClickListener {
            finish()
        }

        //검색하기 버튼 누르면
        binding.searchPillSearchLl.setOnClickListener {

//            val symptomName = intent.getStringExtra("putsymptomName")
//            val intent = Intent(this, ResultPillActivity::class.java)
//            intent.putExtra("putsymptomName", symptomName) // 예시로 증상의 이름을 넘김
//            finish()
//            startActivity(intent)
        }

        setContentView(binding.root)
    }

    //DrugSearch Name
    override fun DrugSearchNameLoading() {
    }

    override fun DrugSearchNameSuccess(drug_list:List<RESULT>) {
        Log.d("name activity success", drug_list.toString())
    }

    override fun DrugSearchNameFailure() {
    }

    //DrugSearch Shape

    override fun DrugSearchShapeLoading() {

    }

    override fun DrugSearchShapeSuccess(drug_list: List<RESULT>) {
        Log.d("shape activity success", drug_list.toString())

        binding.searchPillSearchLl.setOnClickListener {
            val intent = Intent(this@SearchPillActivity, ResultPillActivity::class.java)
            intent.putExtra("drugList", ArrayList(drug_list)) // 데이터를 Intent에 추가

            val symptomName = intent.getStringExtra("putsymptomName")
            intent.putExtra("putsymptomName", symptomName) // 예시로 증상의 이름을 넘김
            finish()
            startActivity(intent)
        }
    }

    override fun DrugSearchShapeFailure() {

    }
}