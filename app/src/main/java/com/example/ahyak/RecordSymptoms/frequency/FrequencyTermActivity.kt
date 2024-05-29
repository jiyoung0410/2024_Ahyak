package com.example.ahyak.RecordSymptoms.frequency

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.PillRegister.RegisterPillActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityFrequencyTermBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Integer.max
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class FrequencyTermActivity : AppCompatActivity() {

    lateinit var binding : ActivityFrequencyTermBinding

    //선택된 요일 관련 변수
    private val selectedDays = mutableListOf<Int>()
    private var selectDay: List<Int> = listOf()
    //Calendar
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    private val dayArr = ArrayList<String>()

    //선택된 처방 이름 Sharedpreference로 저장받을 변수 선언
    var PrescriptionName: String = ""

    //데이터 베이스 객체
    var ahyakDatabase: AhyakDataBase? = null

    //종료일 담을 변수
    var end_Date: String = ""

    //시작일을 담을 변수
    var start_Date  = Calendar.getInstance()

    //간격 지정 변수
    var term : Int = 0

    //지정된 간격(0)/특정 요일(1)/필요할 때 투여(2) 선택 시 바뀌는 변수
    var type : Int = 0

    override fun onResume() {
        super.onResume()

        //sharedpreference 변수 선언
        val sharedPref = this.getSharedPreferences("myPref", Context.MODE_PRIVATE)

        //처방 이름 받아오기
        PrescriptionName = sharedPref.getString("prescriptionName", "")!!

        //종료일 받아오기
        GlobalScope.launch(Dispatchers.IO) {
            //데이터베이스 초기화
            ahyakDatabase = AhyakDataBase.getInstance(this@FrequencyTermActivity)
            end_Date = ahyakDatabase!!.getPrescriptionDao()?.getPrescriptionEnd_Date(PrescriptionName).toString()
            //getEndDate 결과 : 2024.05.31.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrequencyTermBinding.inflate(layoutInflater)

        //일 간격 배열 설정
        createMinMaxArr()

        // 일 간격 피커 설정
        setdayPicker(0)

        //요일 선택에 사용할 리스트 설정
        val layouts = listOf(
            binding.frequentcyTodaySelectSunLl,
            binding.frequentcyTodaySelectMonLl,
            binding.frequentcyTodaySelectTueLl,
            binding.frequentcyTodaySelectWedLl,
            binding.frequentcyTodaySelectThuLl,
            binding.frequentcyTodaySelectFriLl,
            binding.frequentcyTodaySelectSatLl)

        layouts.forEach { layout ->
            layout.setOnClickListener {
                toggleSelection(layout)
                updateSelectedDaysTextView()
            }
        }
        //특정 요일에 눌렀을 때
        binding.registerFrequentcyDayCl.setOnClickListener {
            //check icon 가시성 관련
            binding.registerFrequentcyTermCheckIc.visibility = View.GONE
            binding.registerFrequentcyDayIv.visibility = View.VISIBLE
            binding.registerFrequentcyNeedIv.visibility = View.GONE

            //요일 지정 레이아웃 등장
            binding.frequentcyTermCl.visibility = View.GONE
            binding.frequentcyTodayLl.visibility = View.VISIBLE
            binding.frequentcyStartdayCl.visibility = View.VISIBLE
            type = 0
        }

        //지정된 간격으로 눌렀을 때
        binding.registerFrequentcyTermCl.setOnClickListener {
            //check icon 가시성 관련
            binding.registerFrequentcyTermCheckIc.visibility = View.VISIBLE
            binding.registerFrequentcyDayIv.visibility = View.GONE
            binding.registerFrequentcyNeedIv.visibility = View.GONE

            //간격 지정 레이아웃 등장
            binding.frequentcyTermCl.visibility = View.VISIBLE
            binding.frequentcyTodayLl.visibility = View.GONE
            binding.frequentcyStartdayCl.visibility = View.VISIBLE
            type = 1
        }

        //필요할 때 투여 눌렀을 때
        binding.registerFrequentcyNeedCl.setOnClickListener {
            //check icon 가시성 관련
            binding.registerFrequentcyTermCheckIc.visibility = View.GONE
            binding.registerFrequentcyDayIv.visibility = View.GONE
            binding.registerFrequentcyNeedIv.visibility = View.VISIBLE

            //요일 지정 레이아웃 등장
            binding.frequentcyTermCl.visibility = View.GONE
            binding.frequentcyTodayLl.visibility = View.GONE
            binding.frequentcyStartdayCl.visibility = View.GONE

            type = 2
        }

        //취소 버튼 눌렀을 때
        binding.registerFrequentcyTermCancleIv.setOnClickListener {
            finish()
        }

        //시작일의 날짜 선택 눌렀을 때
        binding.frequentcySelectdateLl.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                binding.registerPillFrequencySelectTv.text =
                    year.toString() + "." + (month + 1).toString() + "." + day.toString()+ "."
            }, year, month, day)
            start_Date.set(year, month, day)

            datePickerDialog.show()
        }

        //저장 누르면
        binding.registerPillSaveLl.setOnClickListener {
            if (type == 0) {
                calculateMedicationTermDates(start_Date.timeInMillis, term)
            } else if (type == 1) {
                calculateMedicationDates(start_Date.timeInMillis, selectDay)
            }

            finish()
            val intent = Intent(this, RegisterPillActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }

    private fun updateSelectedDaysTextView() {
        binding.selectedDaysTv.text = selectedDays.joinToString(", ") { dayOfWeekToString(it) }
        selectDay = selectedDays.toList() // Ensure `selectDay` is updated with selected days
    }

    // 요일을 문자열로 변환하는 함수
    private fun dayOfWeekToString(day: Int): String {
        return when (day) {
            Calendar.SUNDAY -> "일"
            Calendar.MONDAY -> "월"
            Calendar.TUESDAY -> "화"
            Calendar.WEDNESDAY -> "수"
            Calendar.THURSDAY -> "목"
            Calendar.FRIDAY -> "금"
            Calendar.SATURDAY -> "토"
            else -> ""
        }
    }

    // 문자열을 요일로 변환하는 함수
    private fun stringToDayOfWeek(day: String): Int {
        return when (day) {
            "일" -> Calendar.SUNDAY
            "월" -> Calendar.MONDAY
            "화" -> Calendar.TUESDAY
            "수" -> Calendar.WEDNESDAY
            "목" -> Calendar.THURSDAY
            "금" -> Calendar.FRIDAY
            "토" -> Calendar.SATURDAY
            else -> -1
        }
    }

    //요일 선택되었을 때 스타일 바꾸는 코드
    private fun toggleSelection(layout: LinearLayout) {
        val textView = layout.getChildAt(0) as TextView // LinearLayout의 첫 번째 자식은 TextView입니다.
        val day = textView.text.toString()
        val dayInt = stringToDayOfWeek(day)
        if (selectedDays.contains(dayInt)) {
            selectedDays.remove(dayInt)
            textView.setTextColor(Color.GRAY)
            layout.setBackgroundResource(R.drawable.bg_radi_100dp)
        } else {
            selectedDays.add(dayInt)
            textView.setTextColor(Color.WHITE)
            layout.setBackgroundResource(R.drawable.point_radi_100dp)
        }
        updateSelectedDaysTextView() // Ensure `selectedDaysTv` is updated
    }

    private fun setdayPicker(i: Int) {
        binding.numberDatepicker.let {
            it.minValue = 1
            it.maxValue = 99
            it.wrapSelectorWheel = true
            it.displayedValues = dayArr.toTypedArray()
            it.setOnValueChangedListener { picker, oldVal, newVal ->
                binding.frequentcyTermDayTv.text = "${newVal}일"
                term = newVal
            }
        }
    }

    private fun createMinMaxArr() {
        val max = max(1, 100)
        for (i in 1 until max) dayArr.add("${i}일")
    }

    // 종료일 문자열을 Calendar 객체로 변환하는 함수
    private fun parseEndDate(dateString: String): Calendar {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        return Calendar.getInstance().apply { time = date }
    }

    // 약 복용 날짜 계산 함수
    private fun calculateMedicationTermDates(startDateMillis: Long, intervalDays: Int = term) {
        val dates = mutableListOf<String>()
        start_Date = Calendar.getInstance().apply { timeInMillis = startDateMillis }
        val endDate = parseEndDate(end_Date)
        val endDateMillis = endDate.timeInMillis

        var currentDateMillis = startDateMillis
        while (currentDateMillis <= endDateMillis) {
            val currentDate = Calendar.getInstance().apply { timeInMillis = currentDateMillis }
            val formattedDate = "${currentDate.get(Calendar.YEAR)}.${currentDate.get(Calendar.MONTH) + 1}.${currentDate.get(Calendar.DAY_OF_MONTH)}"
            dates.add(formattedDate)
            currentDateMillis += TimeUnit.DAYS.toMillis(intervalDays.toLong())
        }
        Log.d("dates", "$dates")
    }

    // 약 복용 날짜 계산 함수 (특정 요일)
    private fun calculateMedicationDates(startDateMillis: Long, daysOfWeek: List<Int>) {
        GlobalScope.launch(Dispatchers.IO) {
            val dates = mutableListOf<String>()
            start_Date = Calendar.getInstance().apply { timeInMillis = startDateMillis }
            val endDate = parseEndDate(end_Date)
            val endDateMillis = endDate.timeInMillis

            var currentDateMillis = startDateMillis
            while (currentDateMillis <= endDateMillis) {
                val currentDate = Calendar.getInstance().apply { timeInMillis = currentDateMillis }
                val dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK)
                if (daysOfWeek.contains(dayOfWeek)) {
                    val formattedDate = "${currentDate.get(Calendar.YEAR)}.${currentDate.get(Calendar.MONTH) + 1}.${currentDate.get(Calendar.DAY_OF_MONTH)}"
                    dates.add(formattedDate)
                }
                currentDateMillis += TimeUnit.DAYS.toMillis(1)
            }
            Log.d("dates", "$dates")
        }
    }
}
