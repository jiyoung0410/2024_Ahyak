package com.example.ahyak.RecordSymptoms.frequency

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.ahyak.PillRegister.RegisterPillActivity
import com.example.ahyak.PillRegister.ResultPillActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityFrequencyTermBinding
import java.lang.Integer.max
import java.util.Calendar

class FrequencyTermActivity : AppCompatActivity() {

    lateinit var binding : ActivityFrequencyTermBinding
    private val selectedDays = mutableListOf<String>()

    //Calendar
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    private val dayArr = ArrayList<String>()
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

        //지정된 간격으로 눌렀을 때
        binding.registerFrequentcyDayCl.setOnClickListener{

            //check icon 가시성 관련
            binding.registerFrequentcyTermCheckIc.visibility = View.GONE
            binding.registerFrequentcyDayIv.visibility = View.VISIBLE
            binding.registerFrequentcyNeedIv.visibility = View.GONE

            //요일 지정 레이아웃 등장
            binding.frequentcyTermCl.visibility = View.GONE
            binding.frequentcyTodayLl.visibility = View.VISIBLE
            binding.frequentcyStartdayCl.visibility = View.VISIBLE

        }


        //특정 요일에 눌렀을 때
        binding.registerFrequentcyTermCl.setOnClickListener{

            //check icon 가시성 관련
            binding.registerFrequentcyTermCheckIc.visibility = View.VISIBLE
            binding.registerFrequentcyDayIv.visibility = View.GONE
            binding.registerFrequentcyNeedIv.visibility = View.GONE

            //간격 지정 레이아웃 등장
            binding.frequentcyTermCl.visibility = View.VISIBLE
            binding.frequentcyTodayLl.visibility = View.GONE
            binding.frequentcyStartdayCl.visibility = View.VISIBLE
        }

        //필요할 때 투여 눌렀을 때
        binding.registerFrequentcyNeedCl.setOnClickListener{

            //check icon 가시성 관련
            binding.registerFrequentcyTermCheckIc.visibility = View.GONE
            binding.registerFrequentcyDayIv.visibility = View.GONE
            binding.registerFrequentcyNeedIv.visibility = View.VISIBLE

            //요일 지정 레이아웃 등장
            binding.frequentcyTermCl.visibility = View.GONE
            binding.frequentcyTodayLl.visibility = View.GONE
            binding.frequentcyStartdayCl.visibility = View.GONE
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
            datePickerDialog.show()
        }

        //저장 누르면
        binding.registerPillSaveLl.setOnClickListener {
            finish()
            val intent = Intent(this, RegisterPillActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }

    private fun updateSelectedDaysTextView() {
        binding.selectedDaysTv.text = selectedDays.joinToString(", ")
    }

    //요일 선택되었을 때 스타일 바꾸는 코드
    private fun toggleSelection(layout: LinearLayout) {
        val textView = layout.getChildAt(0) as TextView // LinearLayout의 첫 번째 자식은 TextView입니다.
        val day = textView.text.toString()
        if (selectedDays.contains(day)) {
            selectedDays.remove(day)
            textView.setTextColor(Color.GRAY)
            layout.setBackgroundResource(R.drawable.bg_radi_100dp)
        } else {
            selectedDays.add(day)
            textView.setTextColor(Color.WHITE)
            layout.setBackgroundResource(R.drawable.point_radi_100dp)
        }

    }

    private fun setdayPicker(i: Int) {
        binding.numberDatepicker.let {
            it.minValue = 1
            it.maxValue = 99
            it.wrapSelectorWheel = true
            it.displayedValues = dayArr.toTypedArray()
            it.setOnValueChangedListener { picker, oldVal, newVal ->
                binding.frequentcyTermDayTv.setText("${newVal}일")
            }
        }

    }

    private fun createMinMaxArr() {
        val max = max(1, 100)
        for (i in 1 until max) dayArr.add("${i}일")
    }
}