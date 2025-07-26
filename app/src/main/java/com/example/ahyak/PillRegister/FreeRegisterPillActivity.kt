package com.example.ahyak.PillRegister

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.AuthService
import com.example.ahyak.DB.FreeMedicineEntity
import com.example.ahyak.DB.Medicine
import com.example.ahyak.DB.PostMedicineCallback
import com.example.ahyak.DB.PostMedicineRequest
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityFreeRegisterPillBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class  FreeRegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFreeRegisterPillBinding
    var selectshape : String = ""
    var selectcolor : String = ""
    var selecttype : String = ""
    var selectline : String = ""

    //약 이름 중복확인
    var existingMedicineNames :String = ""

    //약 ID 넘기기 위한 변수
    var medicine_id : String = ""

    //데이터 베이스 객체
    var ahyakDatabase : AhyakDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFreeRegisterPillBinding.inflate(layoutInflater)

        setContentView(binding.root)

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
                    previousButton.setBackgroundResource(R.drawable.white_radi_5dp)
                }

                // 클릭된 버튼의 배경색을 변경하기
                button.setBackgroundResource(R.drawable.light_point_radi_5dp)

                // 클릭된 버튼의 아이디 저장하기
                selectedShapeId = button.id
                val selectedShapeTag = it.tag as? String // 클릭된 버튼의 태그 읽어오기
                if (selectedShapeTag != null) {
                    selectshape = selectedShapeTag
                    binding.shapeFreeTv.text = selectedShapeTag.toString()
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
        binding.freeRecordPillColorGrayLl.tag = "회색"
        binding.freeRecordPillColorGreenblueLl.tag = "청록"
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
            binding.freeRecordPillColorGreenblueLl,
            binding.freeRecordPillColorTransLl,
            binding.freeRecordPillColorGrayLl,
            binding.freeRecordPillColorEtcLl)

        var selectedColorId: Int = -1

        for(button in colorList){
            button.setOnClickListener {
                //이전에 선택된 버튼의 배경색을 원래대로 되돌리기
                if (selectedColorId != -1) {
                    val previousButton = findViewById<LinearLayout>(selectedColorId)
                    previousButton.setBackgroundResource(R.drawable.white_radi_5dp)
                }

                // 클릭된 버튼의 배경색을 변경하기
                button.setBackgroundResource(R.drawable.light_point_radi_5dp)

                // 클릭된 버튼의 아이디 저장하기
                selectedColorId = button.id
                val selectedColorTag = it.tag as? String // 클릭된 버튼의 태그 읽어오기
                if (selectedColorTag != null) {
                    selectcolor = selectedColorTag
                    binding.colorFreeTv.text = selectcolor
                } else {
                    selectcolor = "기타"
                }
            }
        }
        //각각 태그달기
        binding.freeRecordPillFormulationTabletLl.tag = "정제"
        binding.freeRecordPillFormulationReshffleLl.tag = "경질캡슐"
        binding.freeRecordPillFormulationSoftLl.tag = "연질캡슐"
        binding.freeRecordPillFormulationEtcLl.tag = "기타"

        //제형을 클릭했을 때
        val formulationList = listOf(
            binding.freeRecordPillFormulationTabletLl,
            binding.freeRecordPillFormulationReshffleLl,
            binding.freeRecordPillFormulationSoftLl,
            binding.freeRecordPillFormulationEtcLl)

        var selectedFormulationId: Int = -1

        for(button in formulationList){
            button.setOnClickListener {
                //이전에 선택된 버튼의 배경색을 원래대로 되돌리기
                if (selectedFormulationId != -1) {
                    val previousButton = findViewById<LinearLayout>(selectedFormulationId)
                    previousButton.setBackgroundResource(R.drawable.white_radi_5dp)
                }

                // 클릭된 버튼의 배경색을 변경하기
                button.setBackgroundResource(R.drawable.light_point_radi_5dp)

                // 클릭된 버튼의 아이디 저장하기
                selectedFormulationId = button.id
                val selectedformulationTag = it.tag as? String // 클릭된 버튼의 태그 읽어오기
                if (selectedformulationTag != null) {
                    selecttype = selectedformulationTag
                    binding.typeFreeTv.text = selecttype
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
                    previousButton.setBackgroundResource(R.drawable.white_radi_5dp)
                }

                // 클릭된 버튼의 배경색을 변경하기
                button.setBackgroundResource(R.drawable.light_point_radi_5dp)

                // 클릭된 버튼의 아이디 저장하기
                selectedLineId = button.id

                val selectedLineTag = it.tag as? String // 클릭된 버튼의 태그 읽어오기
                if (selectedLineTag != null) {
                    selectline = selectedLineTag
                    binding.lineFreeTv.text = selectline

                    if(selectedLineTag == "-"){
                        binding.lineFreeTv.text = "(-)형"
                    }else if(selectedLineTag == "+"){
                        binding.lineFreeTv.text = "(+)형"
                    }else{
                        binding.lineFreeTv.text = selectline
                    }
                } else {
                    selectline = "NULL"
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

        // 2/6에서 '<' 버튼 누르면
        binding.cancleFree2Iv.setOnClickListener {
            //식별문자 레이아웃, 다음(1/6) 나타나고
            binding.freeNameLl.visibility = View.VISIBLE
            binding.nextFree1Ll.visibility = View.VISIBLE

            //모양 검색 레이아웃, < 이전버튼, 다음(2/6) 사라지기
            binding.codeFreeLl.visibility = View.GONE
            binding.cancleFree2Iv.visibility = View.GONE
            binding.nextFree2Ll.visibility = View.GONE

        }

        //  3/6에서 '<'버튼 누르면
        binding.cancleFree3Iv.setOnClickListener {
            //식별문자 레이아웃, 다음(2/6) 나타나고
            binding.codeFreeLl.visibility = View.VISIBLE
            binding.cancleFree2Iv.visibility = View.VISIBLE
            binding.nextFree2Ll.visibility = View.VISIBLE

            //모양 레이아웃, < 이전버튼, 다음(3/6) 사라지기
            binding.shapeFreeLl.visibility = View.GONE
            binding.cancleFree3Iv.visibility = View.GONE
            binding.nextFree3Ll.visibility = View.GONE

        }

        //  4/6에서 '<'버튼 누르면
        binding.cancleFree4Iv.setOnClickListener {
            //모양 레이아웃, 다음(3/6) 나타나고
            binding.shapeFreeLl.visibility = View.VISIBLE
            binding.cancleFree3Iv.visibility = View.VISIBLE
            binding.nextFree3Ll.visibility = View.VISIBLE

            //색상 레이아웃, < 이전버튼, 다음(4/6) 사라지기
            binding.colorFreeLl.visibility = View.GONE
            binding.cancleFree4Iv.visibility = View.GONE
            binding.nextFree4Ll.visibility = View.GONE

        }

        //  5/6에서 '<'버튼 누르면
        binding.cancleFree5Iv.setOnClickListener {
            //색상 레이아웃, 다음(4/6) 나타나고
            binding.colorFreeLl.visibility = View.VISIBLE
            binding.cancleFree4Iv.visibility = View.VISIBLE
            binding.nextFree4Ll.visibility = View.VISIBLE

            //제형 레이아웃, < 이전버튼, 다음(5/6) 사라지기
            binding.typeFreeLl.visibility = View.GONE
            binding.cancleFree5Iv.visibility = View.GONE
            binding.nextFree5Ll.visibility = View.GONE
        }

        //  6/6에서 '<'버튼 누르면
        binding.cancleFree6Iv.setOnClickListener {
            //제형 레이아웃, 다음(5/6) 나타나고
            binding.typeFreeLl.visibility = View.VISIBLE
            binding.cancleFree5Iv.visibility = View.VISIBLE
            binding.nextFree5Ll.visibility = View.VISIBLE

            //분할선 레이아웃, < 이전버튼, 다음(6/6) 사라지기
            binding.lineFreeLl.visibility = View.GONE
            binding.cancleFree6Iv.visibility = View.GONE
            binding.freeRecordPillSearchLl.visibility = View.GONE
        }

        // 1/6에서 검색하기 버튼 누르면
        binding.nextFree1Ll.setOnClickListener {
            //이름 입력 레이아웃, 다음(1/6) 사라지고
            binding.freeNameLl.visibility = View.GONE
            binding.nextFree1Ll.visibility = View.GONE

            //식별 문자 입력 레이아웃, < 이전버튼, 다음(2/6) 등장
            binding.codeFreeLl.visibility = View.VISIBLE
            binding.cancleFree2Iv.visibility = View.VISIBLE
            binding.nextFree2Ll.visibility = View.VISIBLE
        }

        // 2/6에서 검색하기 버튼 누르면
        binding.nextFree2Ll.setOnClickListener {
            //식별 문자 레이아웃, 다음(2/6) 사라지고
            binding.codeFreeLl.visibility = View.GONE
            binding.cancleFree2Iv.visibility = View.GONE
            binding.nextFree2Ll.visibility = View.GONE

            //모양 레이아웃, < 이전버튼, 다음(3/6) 등장
            binding.shapeFreeLl.visibility = View.VISIBLE
            binding.cancleFree3Iv.visibility = View.VISIBLE
            binding.nextFree3Ll.visibility = View.VISIBLE
        }

        // 3/6에서 검색하기 버튼 누르면
        binding.nextFree3Ll.setOnClickListener {
            //모양 레이아웃, 다음(3/6) 사라지고
            binding.shapeFreeLl.visibility = View.GONE
            binding.cancleFree3Iv.visibility = View.GONE
            binding.nextFree3Ll.visibility = View.GONE

            //색상 레이아웃, < 이전버튼, 다음(4/6) 등장
            binding.colorFreeLl.visibility = View.VISIBLE
            binding.cancleFree4Iv.visibility = View.VISIBLE
            binding.nextFree4Ll.visibility = View.VISIBLE
        }

        // 4/6에서 검색하기 버튼 누르면
        binding.nextFree4Ll.setOnClickListener {
            //색상 레이아웃, 다음(4/6) 사라지고
            binding.colorFreeLl.visibility = View.GONE
            binding.cancleFree4Iv.visibility = View.GONE
            binding.nextFree4Ll.visibility = View.GONE

            //타입 레이아웃, < 이전버튼, 다음(5/6) 등장
            binding.typeFreeLl.visibility = View.VISIBLE
            binding.cancleFree5Iv.visibility = View.VISIBLE
            binding.nextFree5Ll.visibility = View.VISIBLE
        }

        // 5/6에서 검색하기 버튼 누르면
        binding.nextFree5Ll.setOnClickListener {
            //타입 레이아웃, 다음(4/6) 사라지고
            binding.typeFreeLl.visibility = View.GONE
            binding.cancleFree5Iv.visibility = View.GONE
            binding.nextFree5Ll.visibility = View.GONE

            //분할선 레이아웃, < 이전버튼, 다음(6/6) 등장
            binding.lineFreeLl.visibility = View.VISIBLE
            binding.cancleFree6Iv.visibility = View.VISIBLE
            binding.freeRecordPillSearchLl.visibility = View.VISIBLE
        }

        //저장하기 버튼 누르면
        binding.freeRecordPillSearchLl.setOnClickListener {

            //약 이름 정보
            val FreeMedicineName = binding.freeRecordPillSerachForNameEt.text.toString()

            //식별 정보
            val FreeMedicineCode = binding.freeRecordPillSerachCodeEt.text.toString()

            //API 연결
            val authService = AuthService(this)
            authService.setPostMedicineView(object: PostMedicineCallback {
                override fun onPostMedicineLoading() {
                    // 로딩 인디케이터 표시
                }

                override fun onPostMedicineSuccess(medicine: Medicine) {
                    medicine_id = medicine.id
                    Toast.makeText(
                        this@FreeRegisterPillActivity,
                        "약 추가 성공: ${medicine.name}",  // 이제 실제 name 값이 찍힙니다!
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPostMedicineFailure(message: String) {
                    Toast.makeText(this@FreeRegisterPillActivity, "약 추가 실패: $message", Toast.LENGTH_SHORT).show()
                }
            })

            // 요청 객체 생성
            val req = PostMedicineRequest(
                name  = FreeMedicineName,
                print = FreeMedicineCode,
                shape = selectshape,
                color = selectcolor,
                type  = selecttype,
                line  = selectline
            )

            // 호출
            authService.postMedicine(req)


//            // 코루틴을 사용하여 백그라운드 스레드에서 데이터베이스 작업 실행
//            GlobalScope.launch(Dispatchers.IO) {
//                // 데이터베이스 초기화
//                ahyakDatabase = AhyakDataBase.getInstance(this@FreeRegisterPillActivity)
//
//                // 데이터베이스에서 해당 이름을 가진 자유기록 약이 있는지 불러오기
//                val existingMedicineList =
//                    ahyakDatabase!!.getFreeMedicineDao()?.getFreeMedicine(FreeMedicineName)
//
//                // 약 이름만 추출
//                existingMedicineNames = existingMedicineList?.map { it.FreeMedicineName }.toString()
//            }
//            if (existingMedicineNames.contains(FreeMedicineName)) {
//                // 메인 스레드에서 Toast 메시지 표시
//                    Toast.makeText(this, "이미 등록된 약입니다.", Toast.LENGTH_SHORT).show()
//
//            } else {
//                GlobalScope.launch(Dispatchers.IO) {
//                    //자유 약 기록하기
//                    ahyakDatabase!!.getFreeMedicineDao()?.insertFreeMedicine(FreeMedicineEntity(
//                        FreeMedicineName, FreeMedicineCode, selectshape, selectcolor, selecttype, selectline))
//
//                    val existingMedicine2 = ahyakDatabase!!.getFreeMedicineDao().getFreeMedicine(FreeMedicineName)
//                    Log.d("제대로 저장되었는지 check", "$existingMedicine2")
//                }

                // 약 이름이 중복되지 않으면 다음 화면으로 이동
//                val intent = Intent(this@FreeRegisterPillActivity, RegisterPillActivity::class.java)
//                intent.putExtra("FreeMedicineName", FreeMedicineName)
//                finish()
//                startActivity(intent)
            val intent = Intent(this@FreeRegisterPillActivity, RegisterPillActivity::class.java)
            intent.putExtra("FreeMedicineName", FreeMedicineName)
            intent.putExtra("FreeMedicineId", medicine_id)
            Log.d("medicine_id", "$medicine_id")
            finish()
            startActivity(intent)
            }
        }

}

