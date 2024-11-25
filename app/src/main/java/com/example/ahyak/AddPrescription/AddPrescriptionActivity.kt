package com.example.ahyak.AddPrescription

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.Toast
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.PrescriptionEntity
import com.example.ahyak.MainActivity
import com.example.ahyak.PillRegister.ExtraRegisterPillActivity
import com.example.ahyak.databinding.ActivityAddSymptomsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale

class AddPrescriptionActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var binding : ActivityAddSymptomsBinding
    private var cal : Calendar = Calendar.getInstance()

    //시작월, 시작일 변수
    private var StartMonth : Int = 0
    private var StartDay : Int = 0
    private var StartDate : String = ""
    private var EndDate : String = ""

    //데이터 베이스 객체
    var ahyakDatabase : AhyakDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSymptomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //음성 인식으로 얻은 데이터를 EditText에 설정
        // Intent로부터 데이터 수신
        val prescription = intent.getStringExtra("Speech_Prescription")
        val hospital = intent.getStringExtra("Speech_Hospital")

        // 수신된 데이터를 EditText에 설정
        if (prescription != null) {
            binding.addSymptomsSymptomNameEt.setText(prescription)
            binding.addSymptomsSymptomNameEt.clearFocus()
            //종료일 레이아웃 뜨도록
            binding.addSymptomsEndTv.visibility = View.VISIBLE
            binding.endLl.visibility = View.VISIBLE
        }
        if (hospital != null) {
            binding.addSymptomsHospitalNameEt.setText(hospital+"병원")
            binding.addSymptomsSymptomNameEt.clearFocus()
            binding.addSymptomsHospitalNameEt.visibility = View.VISIBLE
            binding.addSymptomsHospitalNameTv.visibility = View.VISIBLE
            binding.startLl.visibility = View.VISIBLE
            binding.addSymptomsStartTv.visibility = View.VISIBLE
        }

        //처방 이름 Edit Text
        binding.addSymptomsSymptomNameEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.addSymptomsSymptomNameTv.visibility = View.VISIBLE } }
        binding.addSymptomsSymptomNameEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.addSymptomsSymptomNameEt.setOnEditorActionListener { _, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                binding.addSymptomsSymptomNameEt.clearFocus()
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.addSymptomsSymptomNameEt.windowToken, 0)
                binding.nameBlankLl.visibility = View.VISIBLE
                binding.addSymptomsHospitalNameEt.visibility = View.VISIBLE
                binding.addSymptomsHospitalNameTv.visibility = View.VISIBLE
                return@setOnEditorActionListener true
            }else {
                return@setOnEditorActionListener false
            }
        }

        //병원 이름 Edit Text
        binding.addSymptomsHospitalNameEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.addSymptomsHospitalNameTv.visibility = View.VISIBLE
                binding.nameBlankLl.visibility = View.INVISIBLE } }
        binding.addSymptomsHospitalNameEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.addSymptomsHospitalNameEt.setOnEditorActionListener { _, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                binding.addSymptomsHospitalNameEt.clearFocus()
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.addSymptomsHospitalNameEt.windowToken, 0)
                binding.startLl.visibility = View.VISIBLE
                binding.addSymptomsStartTv.visibility = View.VISIBLE
                return@setOnEditorActionListener true
            }else {
                return@setOnEditorActionListener false
            }
        }

        // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
        binding.root.setOnTouchListener { _, _ ->
            binding.addSymptomsSymptomNameEt.clearFocus() // 포커스 해제
            binding.addSymptomsHospitalNameEt.clearFocus() // 포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.addSymptomsSymptomNameEt.windowToken, 0) // 키보드 숨김
            inputMethodManager.hideSoftInputFromWindow(binding.addSymptomsHospitalNameEt.windowToken, 0)
            true
        }

        //시작일 기본 설정은 오늘 날짜
        // 오늘 날짜의 연도와 월을 가져와서 변수에 할당합니다.
        val localDate = LocalDateTime.now()
        val todayYear = localDate.year
        StartMonth = localDate.monthValue
        StartDay = localDate.dayOfMonth
        StartDate = todayYear.toString() + "." + (StartMonth).toString() + "." + StartDay.toString()+ "."

        binding.addSymptomsStartdayTv.text = StartDate

        //시작 날짜 설정
        binding.addSymptomsStartdayTv.setOnClickListener {

            //종료일 레이아웃 뜨도록
            binding.addSymptomsEndTv.visibility = View.VISIBLE
            binding.endLl.visibility = View.VISIBLE

            val year = cal.get(Calendar.YEAR)
            StartMonth = cal.get(Calendar.MONTH)
            StartDay = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                StartDate = year.toString() + "." + (month + 1).toString() + "." + day.toString()+ "."
                binding.addSymptomsStartdayTv.text = StartDate
                StartMonth = month + 1
                StartDay = day

            }, year, StartMonth, StartDay)

            datePickerDialog.show()
        }

        //종료 날짜 설정
        binding.addSymptomsEnddayClickTv.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->

                EndDate = year.toString() + "." + (month + 1).toString() + "." + day.toString()+ "."
                binding.addSymptomsEnddayTv.text =EndDate
                binding.addSymptomsAddbtnGrayLl.visibility = View.GONE
                binding.addSymptomsAddbtnLl.visibility = View.VISIBLE

            }, year, month, dayOfMonth)
            datePickerDialog.show()
        }

        //'X'누르면 실행
        binding.addSymptomsCancleIv.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.addSymptomsAddbtnLl.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO) {

                val prescriptionName = binding.addSymptomsSymptomNameEt.text.toString()
                val hospitalName = binding.addSymptomsHospitalNameEt.text.toString()

                //데이터베이스 초기화
                ahyakDatabase = AhyakDataBase.getInstance(this@AddPrescriptionActivity)

                val times = listOf("아침", "저녁", "점심","취침 전", "기상 직후")

                val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                val start_Date = dateFormat.parse(StartDate)!!
                val end_Date = dateFormat.parse(EndDate)!!

                val calendar = Calendar.getInstance()
                calendar.time = start_Date

                while (calendar.time <= end_Date) {
                    val currentDateString = dateFormat.format(calendar.time)

                    for (time in times) {
                        // 증상 등록
                        ahyakDatabase!!.getPrescriptionDao()?.insertPrescription(
                            PrescriptionEntity(
                                prescriptionName,
                                calendar.get(Calendar.MONTH) + 1, // 월 (1부터 시작)
                                calendar.get(Calendar.DAY_OF_MONTH), // 일
                                time,
                                hospitalName,
                                currentDateString,
                                EndDate
                            )
                        )
                    }
                    calendar.add(Calendar.DAY_OF_MONTH, 1) // 다음 날짜로 이동
                }
            }
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cal.set(year,month,dayOfMonth)
        cal.set(Calendar.HOUR_OF_DAY,0)
        cal.set(Calendar.MINUTE,0)
        cal.set(Calendar.SECOND,0)
    }
}