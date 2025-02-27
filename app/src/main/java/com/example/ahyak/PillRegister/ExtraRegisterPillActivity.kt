package com.example.ahyak.PillRegister

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.ExtraPillEntity
import com.example.ahyak.DB.PrescriptionEntity
import com.example.ahyak.MainActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityExtraRegisterPillBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ExtraRegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityExtraRegisterPillBinding

    //캘린더 관련
    private var cal : Calendar = Calendar.getInstance()

    //시작월, 시작일 변수
    private var TakeMonth : Int = 0
    private var TakeDay : Int = 0
    private var Takedate : String = ""

    // formattedTime을 클래스 멤버 변수로 선언
    private var formattedTime: String = ""


    var pillType: String = "mg"
    var pillName: String = ""
    var pillVolume: String = ""

    //데이터 베이스 객체
    var ahyakDatabase : AhyakDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //월, 일 정보 받아오기
        //추가 약 저장하기 위함
        val sharedPref = this.getSharedPreferences("myPref", Context.MODE_PRIVATE)

        val selectedMonth = sharedPref.getInt("selectedMonth", 0)
        val selectedDay = sharedPref.getInt("selectedDay", 0)


        binding = ActivityExtraRegisterPillBinding.inflate(layoutInflater)

        binding.extraRegisterPillNameInputEt.imeOptions = EditorInfo.IME_ACTION_DONE

        //mg 버튼 누르면
        binding.extraRegisterPillDosageMgCv.setOnClickListener {
            binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.bg_radi_5dp)
            binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
            pillType = "mg"
        }

        //정 버튼 누르면
        binding.extraRegisterPillDosageTabletCv.setOnClickListener {
            binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.bg_radi_5dp)
            binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.white_radi_5dp)
            pillType = "정"
        }

        //추가 약 이름
        binding.extraRegisterPillNameInputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.extraRegisterPillNameInputEt.clearFocus() // EditTextView1의 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.extraRegisterPillDosageInputEt.windowToken, 0) // 키보드 숨김
                binding.extraRegisterPillDosageInputEt.requestFocus() // EditTextView2로 포커스 이동
                return@setOnEditorActionListener true
            }
            false
        }

        //복용 용량
        binding.extraRegisterPillDosageInputEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.extraRegisterPillDosageInputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.extraRegisterPillDosageInputEt.clearFocus() // 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.extraRegisterPillDosageInputEt.windowToken, 0) // 키보드 숨김
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        //복약일 기본 설정은 [추가 약]화면으로 진입한 날짜로 선택
        // 오늘 날짜의 "연도"를 가져와서 변수에 할당(연도만 가져옴)
        val localDate = LocalDateTime.now()
        val todayYear = localDate.year
        Takedate = todayYear.toString() + "." + (selectedMonth).toString() + "." + (selectedDay).toString() + "."
        binding.takeDaySelectTv.text = Takedate

        //날짜 선택
        var TakeMonth = selectedMonth
        var TakeDay = selectedDay

        //복약일 설정
        binding.takeDaySelectTv.setOnClickListener {

            //데이터피커로 날짜 선택
            val datePickerDialog = DatePickerDialog(this, {_, year, month, day ->
                Takedate = todayYear.toString() + "." + (month + 1).toString() + "." + day.toString()+ "."
                binding.takeDaySelectTv.text = Takedate
                TakeMonth = month+1
                TakeDay = day
                Log.d("Takedate", "$Takedate")
            }, todayYear, TakeMonth-1, TakeDay)

            datePickerDialog.show()
        }

        //복약 시간 설정
        //복약일 디폴트 설정은 [추가 약]화면으로 진입한 시간

        //현재 시간 가져오기
        val now = LocalDateTime.now()
        // 시간 형식 지정 (a: 오전/오후, h:mm -> 12시간제)
        val formatter = DateTimeFormatter.ofPattern("a hh:mm")
        //포맷 적용
        var formattedTime = now.format(formatter)

        binding.takeTimeSelectTv.text = formattedTime

        //타임 피커 사용해서 복약 시간 설정하기 - 스피너 형식
        // 타임 피커 사용해서 복약 시간 설정하기 - 스피너 형식
        binding.takeTimeSelectTv.setOnClickListener {
            // 현재 시간 가져오기
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)  // 24시간제 기준
            val minute = cal.get(Calendar.MINUTE)

            // TimePickerDialog 생성 (12시간제 AM/PM 사용)
            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.THEME_HOLO_LIGHT, // 기본 스피너 스타일 적용
                { _, selectedHour, selectedMinute ->
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    val hour12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12

                    // 선택한 시간을 formattedTime 변수에 저장
                    formattedTime = "$amPm $hour12Format:${String.format("%02d", selectedMinute)}"

                    // TextView 업데이트
                    binding.takeTimeSelectTv.text = formattedTime

                    // 로그 출력
                    Log.d("SelectedTime", "타임 피커에서 선택한 시간: $formattedTime")
                },
                hour,
                minute,
                false // 12시간제 설정
            )

            timePickerDialog.show()
        }


        // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
        binding.root.setOnTouchListener { _, _ ->
            binding.extraRegisterPillNameInputEt.clearFocus() // 포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.extraRegisterPillNameInputEt.windowToken, 0) // 키보드 숨김
            true
        }

        // EditText 내용 변경 시 버튼 상태 업데이트
        binding.extraRegisterPillNameInputEt.addTextChangedListener {
            updateSaveButtonState()
        }

        binding.extraRegisterPillDosageInputEt.addTextChangedListener {
            updateSaveButtonState()
        }

        //저장하기 버튼 누르면
        binding.extraRegisterPillSaveLl.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            //Edit Text에서 약 이름과 용량 정보 받아오기
            pillVolume = binding.extraRegisterPillDosageInputEt.getText().toString()
            pillName = binding.extraRegisterPillNameInputEt.getText().toString()

//            //현재 시간 계산하기
//            val currentTimeMillis = System.currentTimeMillis()
//            val dateFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
//            val formattedTime = dateFormat.format(currentTimeMillis)


            GlobalScope.launch(Dispatchers.IO) {

                //데이터베이스 초기화
                ahyakDatabase = AhyakDataBase.getInstance(this@ExtraRegisterPillActivity)

                Log.d("Prescription Saving", "$pillName, $selectedMonth, $selectedDay, $pillVolume, $pillType, $formattedTime")
                //추가 약 등록
                ahyakDatabase!!.getExtraPillDao()?.insertPill(
                    ExtraPillEntity(pillName, selectedMonth, selectedDay, "기상 직후", pillVolume, pillType, formattedTime)
                )
            }

            finish()
            startActivity(intent)
        }

        //'X'버튼 눌렀을 때
        binding.extraRegisterPillCancleIv.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

    private fun updateSaveButtonState() {
        pillVolume = binding.extraRegisterPillDosageInputEt.text.toString()
        pillName = binding.extraRegisterPillNameInputEt.text.toString()

        if (pillVolume.isNullOrEmpty() || pillName.isNullOrEmpty()) {
            binding.extraRegisterPillGraySaveLl.visibility = View.VISIBLE
            binding.extraRegisterPillSaveLl.visibility = View.GONE
        } else {
            binding.extraRegisterPillGraySaveLl.visibility = View.GONE
            binding.extraRegisterPillSaveLl.visibility = View.VISIBLE
        }
    }
}