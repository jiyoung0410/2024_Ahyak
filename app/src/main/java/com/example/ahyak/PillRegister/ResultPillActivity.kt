package com.example.ahyak.PillRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.MainActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityResultPillBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ResultPillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultPillBinding
    var resultpillName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultPillBinding.inflate(layoutInflater)

        //'x'눌렀을 때
        binding.recordSymptomsCancleIv.setOnClickListener {
            finish()
        }

        binding.resultPillRv.apply {
            val adapter = ResultPillAdapter(
            onClick = {pillName ->
                resultpillName = pillName.toString()

            }).build(resultpillList)

            layoutManager = LinearLayoutManager(this@ResultPillActivity, LinearLayoutManager.VERTICAL,false)
            this.adapter = adapter
        }

        //자유 기록하기 눌렀을 때
        binding.resultPillFreeRecordLl.setOnClickListener {
            val symptomName = intent.getStringExtra("putsymptomName")
            val intent = Intent(this, FreeRegisterPillActivity::class.java)
            intent.putExtra("putsymptomName", symptomName) // 예시로 증상의 이름을 넘김
            finish()
            startActivity(intent)
        }

        //선택하기 누르면
        binding.resultPillSelectLl.setOnClickListener {
            val symptomName = intent.getStringExtra("putsymptomName")
            val intent = Intent(this, RegisterPillActivity::class.java)
            Toast.makeText(this,"$symptomName", Toast.LENGTH_SHORT).show()
            intent.putExtra("putsymptomName", symptomName) // 예시로 증상의 이름을 넘김
            intent.putExtra("resultPillInpoName", resultpillName)
            finish()
            startActivity(intent)
        }
        setContentView(binding.root)
    }
    val resultpillList = arrayListOf<DataItemResultPill>(
        DataItemResultPill(R.drawable.pill_example_img, "가네듀오캡슐", "UDDB NTB"),
        DataItemResultPill(R.drawable.pill_example_img, "가뉴틴캡슐", "ISP G300")

    )
}