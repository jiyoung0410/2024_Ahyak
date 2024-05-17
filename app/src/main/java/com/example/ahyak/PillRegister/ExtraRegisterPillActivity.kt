package com.example.ahyak.PillRegister

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.ExtraPillEntity
import com.example.ahyak.DB.PrescriptionEntity
import com.example.ahyak.MainActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityExtraRegisterPillBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ExtraRegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityExtraRegisterPillBinding

    var pillType: String = "mg"
    var pillName: String = ""
    var pillVolume: String = ""

    //데이터 베이스 객체
    var ahyakDatabase : AhyakDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //월, 일 정보 받아오기
        val sharedPref = this.getSharedPreferences("myPref", Context.MODE_PRIVATE)

        val selectedMonth = sharedPref.getInt("selectedMonth", 0)
        val selectedDay = sharedPref.getInt("selectedDay", 0)


        binding = ActivityExtraRegisterPillBinding.inflate(layoutInflater)

        binding.extraRegisterPillNameInputEt.imeOptions = EditorInfo.IME_ACTION_DONE

        //mg 버튼 누르면
        binding.extraRegisterPillDosageMgCv.setOnClickListener {
            binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.bg_radi_5dp)
            pillType = "mg"
        }

        //정 버튼 누르면
        binding.extraRegisterPillDosageTabletCv.setOnClickListener {
            binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.bg_radi_5dp)
            pillType = "정"
        }

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

        // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
        binding.root.setOnTouchListener { _, _ ->
            binding.extraRegisterPillNameInputEt.clearFocus() // 포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.extraRegisterPillNameInputEt.windowToken, 0) // 키보드 숨김
            true
        }

        //저장하기 버튼 누르면
        binding.extraRegisterPillSaveLl.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)


            //Edit Text에서 약 이름과 용량 정보 받아오기
            pillVolume = binding.extraRegisterPillDosageInputEt.getText().toString()
            pillName = binding.extraRegisterPillNameInputEt.getText().toString()

            //현재 시간 계산하기
            val currentTimeMillis = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
            val formattedTime = dateFormat.format(currentTimeMillis)


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
}