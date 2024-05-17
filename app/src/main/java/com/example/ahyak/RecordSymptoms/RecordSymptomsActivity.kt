package com.example.ahyak.RecordSymptoms

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.TodayRecordEntity
import com.example.ahyak.DB.TodayRecordSymptomEntity
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
    var selectedMonth : Int = 0
    var selectedDay : Int = 0
    var getContent : String = ""

    //데이터 베이스 객체
    var ahyakDatabase: AhyakDataBase? = null

    override fun onResume() {
        super.onResume()

        // 코루틴을 사용하여 백그라운드 스레드에서 데이터베이스 작업 실행
        GlobalScope.launch(Dispatchers.IO) {

            var sharedPref = this@RecordSymptomsActivity.getSharedPreferences("myPref", Context.MODE_PRIVATE)
            selectedMonth = sharedPref.getInt("selectedMonth", 0)
            selectedDay = sharedPref.getInt("selectedDay", 0)


            // 데이터베이스 초기화
            ahyakDatabase = AhyakDataBase.getInstance(this@RecordSymptomsActivity)
            todayrecordSymptoms.clear()

            // 데이터베이스에서 content 데이터 가져오기 - 월/일/시간대 정보 전송
            val NewContent = ahyakDatabase!!.getTodayRecordDao()
                .getTodayRecordContent(selectedMonth, selectedDay)
            todayRecordContent.addAll(NewContent)

            // 데이터베이스에서 symptoms 데이터 가져오기 - 월/일/시간대 정보 전송
            val NewSymptom = ahyakDatabase!!.getTodayRecordSymptomDao()
                .getTodayRecordSymptom(selectedMonth, selectedDay)
            todayrecordSymptoms.addAll(NewSymptom)

            withContext(Dispatchers.Main) {
                // 리사이클러뷰 아이템 구성
                binding.recordSymptomsRv.adapter?.notifyDataSetChanged()
                getContent = extractSymptomNames(NewContent)
                binding.recordSymptomsTv.text = getContent
            }
        }
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

        //더 기록하고 싶나요를 클릭하면
        binding.recordSymptomsTv.setOnClickListener {

            // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
            binding.rootLl.setOnTouchListener { _, _ ->
                binding.recordSymptomsEt.clearFocus() // 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.recordSymptomsEt.windowToken, 0) // 키보드 숨김
                true
            }

            GlobalScope.launch(Dispatchers.IO) {
                // 데이터베이스에서 content 데이터 가져오기 - 월/일/시간대 정보 전송
                val NewContent = ahyakDatabase!!.getTodayRecordDao()
                    .getTodayRecordContent(selectedMonth, selectedDay)
                withContext(Dispatchers.Main) {
                    getContent = extractSymptomNames(NewContent)
                    binding.recordSymptomsEt.setText(getContent)
                }
            }
                binding.recordSymptomsOkIc.visibility = View.VISIBLE
                binding.recordSymptomsTv.visibility = View.GONE
                binding.recordSymptomsEt.visibility = View.VISIBLE
                binding.recordSymptomsEt.setText(getContent)
                binding.recordSymptomsEt.requestFocus() // EditText에 포커스를 설정하여 입력 가능하도록 함
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(
                    binding.recordSymptomsEt,
                    InputMethodManager.SHOW_IMPLICIT
                ) // 키보드를 자동으로 표시
                true // Long Click 이벤트를 소비하여 다른 클릭 이벤트가 발생하지 않도록 함
        }



        binding.recordSymptomsOkIc.setOnClickListener {
            texted = binding.recordSymptomsEt.text.toString()
            if (texted.isEmpty()) {
                binding.recordSymptomsTv.text = "증상에 관해 자유롭게 기록해보세요"
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    texted = binding.recordSymptomsEt.getText().toString()
                    val updatedContent = texted
                    ahyakDatabase!!.getTodayRecordDao().deleteTodayrecordcontent()
                    ahyakDatabase!!.getTodayRecordDao().insertTodayRecordContent(TodayRecordEntity(updatedContent,selectedMonth, selectedDay))
                }
            }
            binding.recordSymptomsEt.clearFocus() // EditText의 포커스 제거
            binding.recordSymptomsOkIc.visibility = View.INVISIBLE
            binding.recordSymptomsTv.visibility = View.VISIBLE
            binding.recordSymptomsTv.setText(texted)
            binding.recordSymptomsEt.visibility = View.INVISIBLE
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.recordSymptomsEt.windowToken, 0) // 키보드 숨김

        }

        //저장 누르면
        binding.recordSymptomsSaveLl.setOnClickListener {

            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
        recordSymptomsadapter = RecordSymptomsAdapter(todayrecordSymptoms)
        binding.recordSymptomsRv.adapter = recordSymptomsadapter
        binding.recordSymptomsRv.layoutManager = GridLayoutManager(this, 3)
    }
}