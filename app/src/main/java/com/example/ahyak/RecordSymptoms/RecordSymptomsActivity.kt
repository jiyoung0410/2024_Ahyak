package com.example.ahyak.RecordSymptoms

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.AuthService
import com.example.ahyak.DB.DailyStatusCallback
import com.example.ahyak.DB.DailyStatusResponse
import com.example.ahyak.DB.TodayRecordEntity
import com.example.ahyak.DB.getAccessToken
import com.example.ahyak.MainActivity
import com.example.ahyak.databinding.ActivityRecordSymptomsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordSymptomsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordSymptomsBinding

    private var todayRecordContent: MutableList<TodayRecordEntity> = mutableListOf()
    private var todayrecordSymptoms: ArrayList<TodayRecordSymptomEntity> = arrayListOf()
    private var recordSymptomsadapter: RecordSymptomsAdapter? = null
    var texted : String = ""
    var additional_info : String = ""

    //25-06-07 year 변수 추가
    var selectedYear : Int = 0
    var selectedMonth : Int = 0
    var selectedDay : Int = 0

    //0000-00-00 형식으로 날짜를 저장하는 변수
    var postDate : String = ""

    var getContent : String = ""

    //데이터 베이스 객체
    var ahyakDatabase: AhyakDataBase? = null

    override fun onResume() {
        super.onResume()

//        //선택한 날짜 불러오는 코드(년-월-일)
//        var sharedPref = this@RecordSymptomsActivity.getSharedPreferences("myPref", Context.MODE_PRIVATE)
//        selectedYear = sharedPref.getInt("selectedYear", 0)
//        selectedMonth = sharedPref.getInt("selectedMonth", 0)
//        selectedDay = sharedPref.getInt("selectedDay", 0)
//        Log.d("selet day", "$selectedYear+$selectedMonth+$selectedDay")
//
//        //YYYY-MM-DD 형식으로 변경
//        postDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay)
//        Log.d("postDate", "$postDate")

        // 코루틴을 사용하여 백그라운드 스레드에서 데이터베이스 작업 실행 - 사용X
//        GlobalScope.launch(Dispatchers.IO) {
//
//            //선택한 날짜 불러오는 코드
////            var sharedPref = this@RecordSymptomsActivity.getSharedPreferences("myPref", Context.MODE_PRIVATE)
////            selectedYear = sharedPref.getInt("selectedYear", 0)
////            selectedMonth = sharedPref.getInt("selectedMonth", 0)
////            selectedDay = sharedPref.getInt("selectedDay", 0)
////            Log.d("selet day", "$selectedYear+$selectedMonth+$selectedDay")
//
////            // 데이터베이스 초기화
////            ahyakDatabase = AhyakDataBase.getInstance(this@RecordSymptomsActivity)
////            todayrecordSymptoms.clear()
//
////            // 데이터베이스에서 content 데이터 가져오기 - 월/일/시간대 정보 전송
////            val NewContent = ahyakDatabase!!.getTodayRecordDao()
////                .getTodayRecordContent(selectedMonth, selectedDay)
////            todayRecordContent.addAll(NewContent)
//
//            // 데이터베이스에서 symptoms 데이터 가져오기 - 월/일/시간대 정보 전송
////            val NewSymptom = ahyakDatabase!!.getTodayRecordSymptomDao()
////                .getTodayRecordSymptom(selectedMonth, selectedDay)
////            Log.d("NewSymptom", "$NewSymptom")
////            todayrecordSymptoms.addAll(NewSymptom)
//
////            withContext(Dispatchers.Main) {
////                // 리사이클러뷰 아이템 구성
////                binding.recordSymptomsRv.adapter?.notifyDataSetChanged()
////                getContent = extractSymptomNames(NewContent)
////                binding.recordSymptomsTv.text = getContent
////            }
//        }
    }

    private fun extractSymptomNames(contentList: List<TodayRecordEntity>): String {
        val symptomNames = contentList.map { it.RecordContent }
        return symptomNames.joinToString(separator = ", ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecordSymptomsBinding.inflate(layoutInflater)

        initrecordSymptomsadapter()

        //증상 추가하기 누르면
        binding.recordSymptomsAddLl.setOnClickListener {
            val intent = Intent(this, SearchSymptomsActivity::class.java)
            startActivity(intent)
        }

        //선택한 날짜 불러오는 코드(년-월-일)
        var sharedPref = this@RecordSymptomsActivity.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        selectedYear = sharedPref.getInt("selectedYear", 0)
        selectedMonth = sharedPref.getInt("selectedMonth", 0)
        selectedDay = sharedPref.getInt("selectedDay", 0)
        Log.d("selet day", "$selectedYear+$selectedMonth+$selectedDay")

        //YYYY-MM-DD 형식으로 변경
        postDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay)
        Log.d("postDate", "$postDate")

        //Daily Status API 호출
        val authService = AuthService(this@RecordSymptomsActivity)
        val postDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay)
        Log.d("postDate_debug", "postDate = $postDate")
        authService.getDailyStatus(postDate, object : DailyStatusCallback {
            override fun onDailyStatusSuccess(data: DailyStatusResponse) {
                val dailyStatus = data.dailyStatus
                val date = dailyStatus.date

                val localDatePart = date.substringBefore("T")  // "2025-03-20"

                val (yearStr, monthStr, dayStr) = localDatePart.split("-")
                val year = yearStr.toInt()
                val month = monthStr.toInt()
                val day = dayStr.toInt()


                val discomforts = dailyStatus.discomforts
                additional_info = dailyStatus.additionalInfo

                // 🔁 증상 리스트 초기화 후 갱신
                todayrecordSymptoms.clear()
                discomforts.forEach {
                    val entity = TodayRecordSymptomEntity(
                        SymptomName = it.description,
                        SymptomStrength = it.severity,
                        RecordSymptomYear = year,
                        RecordSymptomMonth = month,
                        RecordSymptomDay = day
                    )
                    todayrecordSymptoms.add(entity)
                }

                // ✅ 어댑터 갱신
                recordSymptomsadapter?.notifyDataSetChanged()

                binding.recordSymptomsTv.text = additional_info
            }

            override fun onDailyStatusFailure(message: String) {
                if (message.contains("null", ignoreCase = true) || message.contains("없", ignoreCase = true)) {
                    //데이터가 없거나 null일 때
                    Toast.makeText(this@RecordSymptomsActivity, "기록된 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
                    Log.w("DailyStatus", "데이터는 받았지만 내용이 null이거나 없음")
                } else {
                    //서버 요청 실패 등 일반 오류
                    Toast.makeText(this@RecordSymptomsActivity, "서버 통신 실패: $message", Toast.LENGTH_SHORT).show()
                    Log.e("DailyStatus", "서버에서 데이터를 가져오는 데 실패함: $message")
                }
            }
        })

        //더 기록하고 싶나요를 클릭하면
        binding.recordSymptomsTv.setOnClickListener {

            // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
            binding.rootLl.setOnTouchListener { _, _ ->
                binding.recordSymptomsEt.clearFocus() // 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.recordSymptomsEt.windowToken, 0) // 키보드 숨김
                true
            }
            binding.recordSymptomsEt.setText(
                if (additional_info.isNotBlank()) "$additional_info\n\n$getContent" else getContent
            )
            binding.recordSymptomsOkIc.visibility = View.VISIBLE
            binding.recordSymptomsTv.visibility = View.GONE
            binding.recordSymptomsEt.visibility = View.VISIBLE
            binding.recordSymptomsEt.requestFocus() // EditText에 포커스를 설정하여 입력 가능하도록 함
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(
                binding.recordSymptomsEt,
                InputMethodManager.SHOW_IMPLICIT
            ) // 키보드를 자동으로 표시
            true // Long Click 이벤트를 소비하여 다른 클릭 이벤트가 발생하지 않도록 함
        }

        binding.recordSymptomsOkIc.setOnClickListener {
            //additional_info = binding.recordSymptomsEt.text.toString()
            if (additional_info.isEmpty()) {
                binding.recordSymptomsTv.text = "증상에 관해 자유롭게 기록해보세요"
            } else {
                //추가 글 입력 시
//                GlobalScope.launch(Dispatchers.IO) {
//                    texted = binding.recordSymptomsEt.getText().toString()
//                    val updatedContent = texted
//                    ahyakDatabase!!.getTodayRecordDao().deleteTodayrecordcontent()
//                    ahyakDatabase!!.getTodayRecordDao().insertTodayRecordContent(TodayRecordEntity(updatedContent,selectedMonth, selectedDay))
//                }
            }
            binding.recordSymptomsEt.clearFocus() // EditText의 포커스 제거
            binding.recordSymptomsOkIc.visibility = View.INVISIBLE
            binding.recordSymptomsTv.visibility = View.VISIBLE
            binding.recordSymptomsTv.setText(additional_info)
            binding.recordSymptomsEt.visibility = View.INVISIBLE
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.recordSymptomsEt.windowToken, 0) // 키보드 숨김
        }

        //'x'누르면
        binding.recordSymptomsCancleIv.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }

    private fun initrecordSymptomsadapter() {

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2, // 가로 3개 열
            StaggeredGridLayoutManager.HORIZONTAL // 가로 방향
        )

        recordSymptomsadapter = RecordSymptomsAdapter(todayrecordSymptoms)
        binding.recordSymptomsRv.layoutManager = staggeredGridLayoutManager
        binding.recordSymptomsRv.adapter = recordSymptomsadapter
        staggeredGridLayoutManager.reverseLayout = false // 추가 순서를 왼쪽부터 강제
        binding.recordSymptomsRv.setHasFixedSize(false) // 크기 고정 해제 시 화면에 안 뜰 가능성 방지
        binding.recordSymptomsRv.isNestedScrollingEnabled = true // NestedScrollView 내부라면 추가
        binding.recordSymptomsRv.itemAnimator = null
    }
}