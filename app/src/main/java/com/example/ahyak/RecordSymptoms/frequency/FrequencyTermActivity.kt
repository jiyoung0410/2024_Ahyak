package com.example.ahyak.RecordSymptoms.frequency

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.PillRegister.RegisterPillActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityFrequencyTermBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.max

class FrequencyTermActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFrequencyTermBinding

    // 선택된 요일 관련 변수
    private val selectedDays = mutableListOf<Int>()
    private var selectDay: List<Int> = listOf()

    // Calendar
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    private val dayArr = ArrayList<String>()

    // SharedPreferences 로부터 받을 변수
    private var PrescriptionName: String = ""
    private var end_Date: String = ""

    // Database
    private var ahyakDatabase: AhyakDataBase? = null

    // 시작일을 담을 변수
    private var start_Date = Calendar.getInstance()

    // 간격 지정 변수
    private var term: Int = 1

    // 모드: 0=간격, 1=특정 요일
    private var type: Int = 0

    // 다음 화면으로 보낼 요일 리스트 (문자열)
    private var selectDayNext: List<String> = listOf()

    override fun onResume() {
        super.onResume()
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        PrescriptionName = sharedPref.getString("prescriptionName", "")!!

        GlobalScope.launch(Dispatchers.IO) {
            ahyakDatabase = AhyakDataBase.getInstance(this@FrequencyTermActivity)
            // DB에서 "2024.05.31." 처럼 점 구분 문자열을 가져온다
            end_Date = ahyakDatabase!!
                .getPrescriptionDao()
                .getPrescriptionEnd_Date(PrescriptionName)
                .toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrequencyTermBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences editor
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // 일 간격 배열 세팅
        createMinMaxArr()
        setdayPicker(0)

        // 요일 선택 레이아웃
        val layouts = listOf(
            binding.frequentcyTodaySelectSunLl,
            binding.frequentcyTodaySelectMonLl,
            binding.frequentcyTodaySelectTueLl,
            binding.frequentcyTodaySelectWedLl,
            binding.frequentcyTodaySelectThuLl,
            binding.frequentcyTodaySelectFriLl,
            binding.frequentcyTodaySelectSatLl
        )
        layouts.forEach { layout ->
            layout.setOnClickListener {
                toggleSelection(layout)
                updateSelectedDaysTextView()
            }
        }

        // 모드 선택: 간격
        binding.registerFrequentcyTermCl.setOnClickListener {
            binding.registerFrequentcyTermCheckIc.visibility = View.VISIBLE
            binding.registerFrequentcyDayIv.visibility = View.GONE
            binding.frequentcyTermCl.visibility = View.VISIBLE
            binding.frequentcyTodayLl.visibility = View.GONE
            binding.frequentcyStartdayCl.visibility = View.VISIBLE
            type = 0
        }

        // 모드 선택: 요일
        binding.registerFrequentcyDayCl.setOnClickListener {
            binding.registerFrequentcyTermCheckIc.visibility = View.GONE
            binding.registerFrequentcyDayIv.visibility = View.VISIBLE
            binding.frequentcyTermCl.visibility = View.GONE
            binding.frequentcyTodayLl.visibility = View.VISIBLE
            binding.frequentcyStartdayCl.visibility = View.VISIBLE
            type = 1
        }

        // 취소
        binding.registerFrequentcyTermCancleIv.setOnClickListener {
            finish()
        }

        // 시작일 선택
        binding.frequentcySelectdateLl.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                binding.registerPillFrequencySelectTv.text = "${y}.${m + 1}.${d}."
                start_Date.set(y, m, d)
                year = y; month = m; day = d
                binding.registerPillSaveGrayLl.visibility = View.GONE
                binding.registerPillSaveLl.visibility = View.VISIBLE
            }, year, month, day).apply {
                show()
                getButton(DatePickerDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(this@FrequencyTermActivity, R.color.point))
                getButton(DatePickerDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(this@FrequencyTermActivity, R.color.point))
            }
        }

        // 저장
        binding.registerPillSaveLl.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val dates = when (type) {
                    0 -> {
                        editor.putInt("term", term).apply()
                        calculateMedicationTermDates(start_Date.timeInMillis, term)
                    }
                    1 -> {
                        editor.putString("selectDay", selectDayNext.toString()).apply()
                        calculateMedicationDates(start_Date.timeInMillis, selectDay)
                    }
                    else -> emptyList()
                }
                withContext(Dispatchers.Main) {
                    editor.putString("dates", dates.joinToString(","))
                    editor.putInt("type", type)
                    editor.apply()
                    finish()
                }
            }
        }
    }

    private fun updateSelectedDaysTextView() {
        selectDayNext = listOf(selectedDays.joinToString(", ") { dayOfWeekToString(it) })
        binding.selectedDaysTv.text = selectDayNext.toString()
        selectDay = selectedDays.toList()
    }

    private fun dayOfWeekToString(day: Int): String = when (day) {
        Calendar.SUNDAY    -> "일"
        Calendar.MONDAY    -> "월"
        Calendar.TUESDAY   -> "화"
        Calendar.WEDNESDAY -> "수"
        Calendar.THURSDAY  -> "목"
        Calendar.FRIDAY    -> "금"
        Calendar.SATURDAY  -> "토"
        else               -> ""
    }

    private fun stringToDayOfWeek(day: String): Int = when (day) {
        "일" -> Calendar.SUNDAY
        "월" -> Calendar.MONDAY
        "화" -> Calendar.TUESDAY
        "수" -> Calendar.WEDNESDAY
        "목" -> Calendar.THURSDAY
        "금" -> Calendar.FRIDAY
        "토" -> Calendar.SATURDAY
        else -> -1
    }

    private fun toggleSelection(layout: LinearLayout) {
        val tv = layout.getChildAt(0) as TextView
        val dow = stringToDayOfWeek(tv.text.toString())
        if (selectedDays.contains(dow)) {
            selectedDays.remove(dow)
            tv.setTextColor(Color.GRAY)
            layout.setBackgroundResource(R.drawable.bg_radi_100dp)
        } else {
            selectedDays.add(dow)
            tv.setTextColor(Color.WHITE)
            layout.setBackgroundResource(R.drawable.point_radi_100dp)
        }
        updateSelectedDaysTextView()
    }

    private fun createMinMaxArr() {
        val maxDays = max(1, 100)
        for (i in 1 until maxDays) dayArr.add("${i}일")
    }

    private fun setdayPicker(i: Int) {
        binding.numberDatepicker.apply {
            minValue = 1
            maxValue = 99
            wrapSelectorWheel = true
            displayedValues = dayArr.toTypedArray()
            setOnValueChangedListener { _, _, newVal ->
                binding.frequentcyTermDayTv.text = "${newVal}일"
                term = newVal
            }
        }
    }

    /**
     * 종료일 문자열을 파싱해서 다음 날까지 포함한 Calendar 반환
     * - "2024.05.31." 또는 "2025-07-23" 둘 다 허용
     */
    private fun parseEndDate(dateString: String): Calendar {
        val normalized = dateString.trimEnd('.')
        val pattern = when {
            normalized.contains('-') -> "yyyy-MM-dd"
            normalized.contains('.') -> "yyyy.MM.dd"
            else -> throw ParseException("Unknown date format: $dateString", 0)
        }
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val date = sdf.parse(normalized)
            ?: throw ParseException("Unparseable date: $dateString", 0)

        return Calendar.getInstance().apply {
            time = date
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    /**
     * 일정 간격으로 복약일 리스트 계산
     */
    private fun calculateMedicationTermDates(
        startDateMillis: Long,
        intervalDays: Int
    ): List<String> {
        val dates = mutableListOf<String>()
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val startCal = Calendar.getInstance().apply { timeInMillis = startDateMillis }
        val endCal = parseEndDate(end_Date)
        var current = startCal.timeInMillis

        while (current <= endCal.timeInMillis) {
            val c = Calendar.getInstance().apply { timeInMillis = current }
            dates.add(sdf.format(c.time))
            current += TimeUnit.DAYS.toMillis(intervalDays.toLong())
        }
        return dates
    }

    /**
     * 특정 요일에 맞춰 복약일 리스트 계산
     */
    private fun calculateMedicationDates(
        startDateMillis: Long,
        daysOfWeek: List<Int>
    ): List<String> {
        val dates = mutableListOf<String>()
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val calIter = Calendar.getInstance().apply { timeInMillis = startDateMillis }
        val endCal = parseEndDate(end_Date)

        while (calIter.timeInMillis <= endCal.timeInMillis) {
            if (daysOfWeek.contains(calIter.get(Calendar.DAY_OF_WEEK))) {
                dates.add(sdf.format(calIter.time))
            }
            calIter.add(Calendar.DAY_OF_MONTH, 1)
        }
        Log.d("dates", dates.toString())
        return dates
    }
}
