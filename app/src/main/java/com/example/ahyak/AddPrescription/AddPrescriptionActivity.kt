package com.example.ahyak.AddPrescription

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.PrescriptionEntity
import com.example.ahyak.databinding.ActivityAddSymptomsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

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



        binding.addSymptomsSymptomNameEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.addSymptomsSymptomNameEt.setOnEditorActionListener { _, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                binding.addSymptomsSymptomNameEt.clearFocus()
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.addSymptomsSymptomNameEt.windowToken, 0)
                return@setOnEditorActionListener true
            }else {
                return@setOnEditorActionListener false
            }
        }

        binding.addSymptomsHospitalNameEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.addSymptomsHospitalNameEt.setOnEditorActionListener { _, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                binding.addSymptomsHospitalNameEt.clearFocus()
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.addSymptomsHospitalNameEt.windowToken, 0)
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


        binding.addSymptomsStartdayLl.setOnClickListener {
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

        binding.addSymptomsEnddayLl.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->

                EndDate = year.toString() + "." + (month + 1).toString() + "." + day.toString()+ "."
                binding.addSymptomsEnddayTv.text =EndDate

            }, year, month, dayOfMonth)
            datePickerDialog.show()
        }

        binding.addSymptomsCancleIv.setOnClickListener {
            finish()
        }

        binding.addSymptomsAddbtnLl.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO) {

                val prescriptionName = binding.addSymptomsSymptomNameEt.text.toString()
                val hospitalName = binding.addSymptomsHospitalNameEt.text.toString()

                //데이터베이스 초기화
                ahyakDatabase = AhyakDataBase.getInstance(this@AddPrescriptionActivity)

                Log.d("Prescription Saving", "$prescriptionName, $StartMonth, $StartDay, $hospitalName, $StartDate, $EndDate")
                //증상 등록
                ahyakDatabase!!.getPrescriptionDao()?.insertPrescription(
                    PrescriptionEntity(prescriptionName, StartMonth, StartDay, "기상 직후", hospitalName, StartDate, EndDate)
                )
            }
            finish()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cal.set(year,month,dayOfMonth)

        cal.set(Calendar.HOUR_OF_DAY,0)
        cal.set(Calendar.MINUTE,0)
        cal.set(Calendar.SECOND,0)
    }
}