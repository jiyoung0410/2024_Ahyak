package com.example.ahyak.PillRegister

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityPillImageResultBinding
import com.example.ahyak.databinding.ActivityResultPillBinding
import com.example.ahyak.remote.RESULT
import kotlin.random.Random

class PillImageResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPillImageResultBinding
    private var resultpillName: String = ""
    private var resultpillList = ArrayList<DataItemResultPill>()

    //선택된 처방 이름 Sharedpreference로 저장받을 변수 선언
    var PrescriptionName : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPillImageResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageName = intent.getStringExtra("imageName")

        //Sharedpreference 변수 선언
        val sharedPref = this.getSharedPreferences("myPref", Context.MODE_PRIVATE)

        //처방 이름 받아오기
        PrescriptionName = sharedPref.getString("prescriptionName", "")!!

        //'x'눌렀을 때
        binding.pillImageResultCancleIv.setOnClickListener {
            finish()
        }

        // RecyclerView 초기화
        binding.pillImageResultRv.layoutManager = LinearLayoutManager(this)

        //Medicine 이미지
        val drawableIds = arrayOf(
            R.drawable.ic_202202893,
            R.drawable.ic_199303108,
            //R.drawable.ic_199303109,
            R.drawable.ic_199603003,
            R.drawable.ic_199903739,
            R.drawable.ic_200202893,
            R.drawable.ic_202200407
        )


        //Intent로부터 데이터추출
//        resultpillName = intent.getStringExtra("medicineName").toString()
        if(imageName == "tyrenol1.png") {
            resultpillName = "우먼타이레놀정"
        }
        if(resultpillName == "null"){
            Log.d("is Empty?", "$resultpillName")
            resultpillList.clear()
        }
        else if(resultpillName.isNotEmpty()){
            Log.d("is not Empty?", "$resultpillName")
            // drawable 리소스 선택
            val DrawableId = drawableIds[Random.nextInt(drawableIds.size)]

            resultpillList.clear()
            resultpillList.add(
                DataItemResultPill(
                    DrawableId, //임시
                    resultpillName,
                    "TYME"
                )
            )
        }


        // Intent로부터 데이터 추출
//        val drugList: ArrayList<RESULT>? = intent.getSerializableExtra("drugList") as? ArrayList<RESULT>
//        if (drugList != null) {
//            // 데이터가 유효한 경우 처리
//            resultpillList.clear()
//
//            //drugList를 resultpillList로 변환
//            for (drug in drugList) {
//                // item_seq이 이미지 데이터인 경우 R.drawable.drug.item_seq를 사용하여 이미지 리소스를 가져옵니다.
//                // 예시로 R.drawable.ic_pill_example를 사용합니다.
//                val pillImageResource = when (drug.item_seq) {
//                    // 예시로 item_seq가 201900631인 경우에 대해 특정 이미지 리소스를 지정합니다.
//                    201309807 -> R.drawable.ic_201309807
//                    199801174 -> R.drawable.ic_199801174
//                    199900642 -> R.drawable.ic_199900642
//                    201502454 -> R.drawable.ic_201502454
//                    201706147 -> R.drawable.ic_201706147
//                    202203530 -> R.drawable.ic_202203530
//
//                    // 다른 item_seq 값에 대해 필요한 이미지 리소스를 추가할 수 있습니다.
//                    else -> R.drawable.ic_default_pill
//                }
//                resultpillList.add(
//                    DataItemResultPill(
//                        pillImageResource,
//                        drug.item_name,
//                        drug.print
//                    )
//                )
//            }
//        }

        // RecyclerView에 어댑터 설정
        val adapter = ResultPillAdapter { pillName ->
            resultpillName = pillName
        }.build(resultpillList)
        binding.pillImageResultRv.adapter = adapter

        //자유 기록하기 눌렀을 때
        binding.pillImageResultFreeRecordLl.setOnClickListener {
            val symptomName = intent.getStringExtra("putsymptomName")
            val intent = Intent(this, FreeRegisterPillActivity::class.java)
            intent.putExtra("putsymptomName", symptomName) // 예시로 증상의 이름을 넘김
            finish()
            startActivity(intent)
        }

        //선택하기 누르면
        binding.pillImageResultSelectLl.setOnClickListener {
            val symptomName = intent.getStringExtra("putsymptomName")
            val intent = Intent(this, RegisterPillActivity::class.java)
            intent.putExtra("putsymptomName", symptomName) // 예시로 증상의 이름을 넘김
            intent.putExtra("resultPillInpoName", resultpillName) //선택한 약 이름
            finish()
            startActivity(intent)
        }
    }
}