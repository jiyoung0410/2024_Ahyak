package com.example.ahyak.PillRegister

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.example.ahyak.MainActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityExtraRegisterPillBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ExtraRegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityExtraRegisterPillBinding

    var pillDosage: String = "mg"
    var pillName: String = ""
    var pillDosageSize: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExtraRegisterPillBinding.inflate(layoutInflater)

        binding.extraRegisterPillNameInputEt.imeOptions = EditorInfo.IME_ACTION_DONE

        //돋보기 아이콘 누르면 결과 화면으로 이동
        binding.extraRegisterPillSearchIv.setOnClickListener {
            val intent = Intent(this, ResultPillActivity::class.java)
            startActivity(intent)
        }

        //mg 버튼 누르면
        binding.extraRegisterPillDosageMgCv.setOnClickListener {
            binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.bg_radi_5dp)
            pillDosage = "mg"
        }

        //정 버튼 누르면
        binding.extraRegisterPillDosageTabletCv.setOnClickListener {
            binding.extraRegisterPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.extraRegisterPillDosageMgCv.setBackgroundResource(R.drawable.bg_radi_5dp)
            pillDosage = "정"
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
            //데이터 함께 보내기

            pillDosageSize = binding.extraRegisterPillDosageInputEt.getText().toString()
            pillName = binding.extraRegisterPillNameInputEt.getText().toString()

            intent.putExtra("extraPillInpoName", pillName)
            intent.putExtra("extraPillInpoDosageSize", pillDosageSize)
            intent.putExtra("extraPillInpoDosage", pillDosage)

            // 현재 시간을 포함하여 보내기
            val currentTimeMillis = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
            val formattedTime = dateFormat.format(currentTimeMillis)
            intent.putExtra("formattedTime", formattedTime)

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