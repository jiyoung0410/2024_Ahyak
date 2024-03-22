package com.example.ahyak.PillRegister

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivitySearchPillBinding

class SearchPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchPillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchPillBinding.inflate(layoutInflater)

        binding.serachPillSerachForNameEt.imeOptions = EditorInfo.IME_ACTION_DONE

        //모양을 클릭했을 때
        val shapeList = listOf(
            binding.searchPillShapeCircleLl,
            binding.searchPillShapeOvalLl,
            binding.searchPillShapeEllipseLl,
            binding.searchPillShapeTriangleLl,
            binding.searchPillShapeRectanglerLl,
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
            }
        }

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
            }
        }

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
            }
        }

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
            }
        }

        binding.serachPillSerachForNameEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.serachPillSerachForNameEt.clearFocus() // EditTextView1의 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.serachPillSerachForNameEt.windowToken, 0) // 키보드 숨김
                binding.serachPillSerachForNameEt.requestFocus() // EditTextView2로 포커스 이동
                return@setOnEditorActionListener true
            }
            false
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
            binding.serachPillSerachForNameEt.clearFocus() //포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.serachPillSerachForShapeEt.windowToken, 0) // 키보드 숨김
            inputMethodManager.hideSoftInputFromWindow(binding.serachPillSerachForNameEt.windowToken, 0) // 키보드 숨김
            true
        }

        binding.searchLl.setOnTouchListener { _, _ ->
            binding.serachPillSerachForShapeEt.clearFocus() // 포커스 해제
            binding.serachPillSerachForNameEt.clearFocus() //포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.serachPillSerachForShapeEt.windowToken, 0) // 키보드 숨김
            inputMethodManager.hideSoftInputFromWindow(binding.serachPillSerachForNameEt.windowToken, 0) // 키보드 숨김
            true
        }

        //'X'버튼 누르면
        binding.searchPillCancleIv.setOnClickListener {
            finish()
        }

        //검색하기 버튼 누르면
        binding.searchPillSearchLl.setOnClickListener {
            val intent = Intent(this, ResultPillActivity::class.java)
            finish()
            startActivity(intent)
        }

        setContentView(binding.root)
    }
}