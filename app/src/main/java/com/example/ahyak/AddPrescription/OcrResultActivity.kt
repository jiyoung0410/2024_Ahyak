package com.example.ahyak.AddPrescription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ahyak.MainActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityOcrResultBinding
import com.example.ahyak.databinding.ActivityOcrprescriptionBinding

class OcrResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOcrResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOcrResultBinding.inflate(layoutInflater)

        //약 등록 누르면
        binding.ocrResultBtn.setOnClickListener {

        }

        //취소 누르면
        binding.ocrReturnBtn.setOnClickListener {
            finish()
            val intent = Intent(this, OCRprescriptionActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)

        // 전달받은 데이터 처리
        val drugInfoList = intent.getStringArrayListExtra("drugInfoList")

        // 데이터를 화면에 표시
        if (drugInfoList != null && drugInfoList.isNotEmpty()) {
            val totalDrugs = drugInfoList.size / 4
            val displayText = StringBuilder()

            for (i in 0 until totalDrugs) {
                displayText.append("약 이름: ${drugInfoList[i*4]}\n")
                displayText.append("1회 투여량: ${drugInfoList[i*4 + 1]}\n")
                displayText.append("1일 투여횟수: ${drugInfoList[i*4 + 2]}\n")
                displayText.append("총 투약 일수: ${drugInfoList[i*4 + 3]}\n\n")
            }

            binding.ocrResultTv.text = displayText.toString() // ocr_result_tv에 표시
        }

    }
}