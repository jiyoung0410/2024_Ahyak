package com.example.ahyak.PillRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityResultPillBinding
import com.example.ahyak.remote.RESULT

class ResultPillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultPillBinding
    private var resultpillName: String = ""
    private var resultpillList = ArrayList<DataItemResultPill>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultPillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //'x'눌렀을 때
        binding.recordSymptomsCancleIv.setOnClickListener {
            finish()
        }

        // RecyclerView 초기화
        binding.resultPillRv.layoutManager = LinearLayoutManager(this)

        // Intent로부터 데이터 추출
        val drugList: ArrayList<RESULT>? = intent.getSerializableExtra("drugList") as? ArrayList<RESULT>
        if (drugList != null) {
            // 데이터가 유효한 경우 처리
            resultpillList.clear()

            //drugList를 resultpillList로 변환
            for (drug in drugList) {
                // item_seq이 이미지 데이터인 경우 R.drawable.drug.item_seq를 사용하여 이미지 리소스를 가져옵니다.
                // 예시로 R.drawable.ic_pill_example를 사용합니다.
                val pillImageResource = when (drug.item_seq) {
                    // 예시로 item_seq가 201900631인 경우에 대해 특정 이미지 리소스를 지정합니다.
                    201309807 -> R.drawable.ic_201309807
                    // 다른 item_seq 값에 대해 필요한 이미지 리소스를 추가할 수 있습니다.
                    else -> R.drawable.ic_default_pill
                }
                resultpillList.add(
                    DataItemResultPill(
                        pillImageResource,
                        drug.item_name,
                        drug.print
                    )
                )
            }
        }

        // RecyclerView에 어댑터 설정
        val adapter = ResultPillAdapter { pillName ->
            resultpillName = pillName
        }.build(resultpillList)
        binding.resultPillRv.adapter = adapter

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
            intent.putExtra("putsymptomName", symptomName) // 예시로 증상의 이름을 넘김
            intent.putExtra("resultPillInpoName", resultpillName)
            finish()
            startActivity(intent)
        }
    }
}
