package com.example.ahyak.PillRegister

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.ahyak.Calendar.DataItemExtraPill
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityRegisterPillBinding

class RegisterPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterPillBinding
    var registerpillDosage: String = "정"
    var registerpillDosageSize:String = ""
    var registerpillName:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterPillBinding.inflate(layoutInflater)

        //text에 밑줄 추가하는 코드
        binding.registerPillSearchShapeTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.registerPillNameInputEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.registerPillNameInputEt.setOnEditorActionListener { _, actionId1, _ ->
            if (actionId1 == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.registerPillNameInputEt.clearFocus() // 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.registerPillNameInputEt.windowToken, 0) // 키보드 숨김
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        binding.registerPillDosageInputEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.registerPillDosageInputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter 키가 눌렸을 때 실행할 동작
                binding.registerPillDosageInputEt.clearFocus() // 포커스 해제
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.registerPillDosageInputEt.windowToken, 0) // 키보드 숨김
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        // EditText 외부를 터치했을 때 포커스 제거 및 키보드 숨김 처리
        binding.root.setOnTouchListener { _, _ ->
            binding.registerPillNameInputEt.clearFocus() // 포커스 해제
            binding.registerPillDosageInputEt.clearFocus() // 포커스 해제
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.registerPillNameInputEt.windowToken, 0) // 키보드 숨김
            inputMethodManager.hideSoftInputFromWindow(binding.registerPillDosageInputEt.windowToken, 0)
            true
        }


        //돋보기 아이콘 누르면 결과 화면으로 이동
        binding.registerPillSearchIv.setOnClickListener {
            val intent = Intent(this, ResultPillActivity::class.java)
            startActivity(intent)
        }

        //mg 버튼 누르면
        binding.registerPillDosageMgCv.setOnClickListener {
            binding.registerPillDosageMgCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.registerPillDosageTabletCv.setBackgroundResource(R.drawable.gray2_radi_5dp)
            registerpillDosage = "mg"
        }

        //정 버튼 누르면
        binding.registerPillDosageTabletCv.setOnClickListener {
            binding.registerPillDosageTabletCv.setBackgroundResource(R.drawable.white_radi_5dp)
            binding.registerPillDosageMgCv.setBackgroundResource(R.drawable.gray2_radi_5dp)
            registerpillDosage = "정"

        }

        //모양으로 검색하기 눌렀을 때
        binding.registerPillSearchShapeTv.setOnClickListener {
            val intent = Intent(this, SearchPillActivity::class.java)
            finish()
            startActivity(intent)
        }

        val resultPillName = intent.getStringExtra("resultPillInpoName") ?: ""
        if(resultPillName!!.isNotEmpty()){
            binding.registerPillNameInputEt.hint = resultPillName
        }

        val freeRecordPillName = intent.getStringExtra("freeRecordPillInpoName") ?: ""
        if(freeRecordPillName!!.isNotEmpty()){
            binding.registerPillNameInputEt.hint = freeRecordPillName
        }

        //'X'버튼 눌렀을 때
        binding.registerPillCancleIv.setOnClickListener {
            finish()
        }

        //저장 눌렀을 때
        binding.registerPillSaveLl.setOnClickListener {
            val resultIntent = Intent()
            registerpillDosageSize = binding.registerPillDosageInputEt.getText().toString()
            registerpillName = binding.registerPillNameInputEt.getText().toString()

            resultIntent.putExtra("extraPillInpoName", registerpillName)
            resultIntent.putExtra("extraPillInpoDosageSize", registerpillDosageSize)
            resultIntent.putExtra("extraPillInpoDosage", registerpillDosage)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
        setContentView(binding.root)

    }
}