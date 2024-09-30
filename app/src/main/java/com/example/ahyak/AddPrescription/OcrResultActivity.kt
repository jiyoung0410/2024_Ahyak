package com.example.ahyak.AddPrescription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.MainActivity
import com.example.ahyak.RecordSymptoms.DataItemSearchSymptom
import com.example.ahyak.databinding.ActivityOcrResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OcrResultActivity : AppCompatActivity(), OnItemClickListener2 {

    private lateinit var binding : ActivityOcrResultBinding

    private var searchPrescriptions : ArrayList<DataItemSearchSymptom> = arrayListOf()
    private var searchPrescriptionadapter : SearchPrescriptionAdapter ?= null

    //DataBase 객체
    var ahyakDatabase : AhyakDataBase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOcrResultBinding.inflate(layoutInflater)
        ahyakDatabase = AhyakDataBase.getInstance(this)

        setContentView(binding.root)

        searchPrescriptionInit()
        initPrescriptionadapter()

        //'x'누르면
        binding.cancleIv.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Edit Text 관련
        binding.searchPrescripionEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전에 실행할 작업
                binding.searchPrescriptionRv.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때마다 실행할 작업
                filterSymptoms(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력 후에 실행할 작업
            }
        })

        //약 등록 누르면
        binding.ocrResultBtn.setOnClickListener {

        }

        //취소 누르면
        binding.ocrReturnBtn.setOnClickListener {
            finish()
            val intent = Intent(this, OCRprescriptionActivity::class.java)
            startActivity(intent)
        }


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

    private fun initPrescriptionadapter() {
        searchPrescriptionadapter = SearchPrescriptionAdapter(searchPrescriptions, this)
        binding.searchPrescriptionRv.adapter = searchPrescriptionadapter
        binding.searchPrescriptionRv.layoutManager = LinearLayoutManager(this)
    }

    private fun searchPrescriptionInit() {
        // Coroutine을 사용하여 비동기적으로 데이터베이스 작업 수행
        CoroutineScope(Dispatchers.IO).launch {
            // 데이터베이스에서 Prescription 목록 가져오기
            val prescriptionNames = ahyakDatabase?.getPrescriptionDao()?.getAllSymptomNames()


            // 중복 제거 (Set으로 변환하여 중복을 제거한 후 다시 List로 변환)
            val uniqueNames = prescriptionNames?.toSet()?.toList()
            Log.d("uniqueNames", "$uniqueNames")

            // UI 작업은 메인 스레드에서 해야 하므로 Dispatchers.Main으로 전환
            withContext(Dispatchers.Main) {
                // 검색 데이터 리스트에 추가
                if (uniqueNames != null) {
                    for (name in uniqueNames) {
                        searchPrescriptions.add(DataItemSearchSymptom(name))
                    }

                    // 어댑터에 데이터 변경 알림
                    searchPrescriptionadapter?.notifyDataSetChanged()
                }
            }
        }
    }


    private fun filterSymptoms(query: String) {
        val filteredList = ArrayList<DataItemSearchSymptom>()

        for (item in searchPrescriptions) {
            // 증상 명칭에 검색어가 포함되어 있는지 확인
            if (item.searchsympotmName.contains(query, ignoreCase = true)) {
                filteredList.add(item)
            }
        }

        // 어댑터에 필터링된 목록 설정
        searchPrescriptionadapter?.filterList(filteredList)
    }

    override fun onItemClick(searchPrescription: DataItemSearchSymptom) {
        binding.searchPrescripionEt.setText(searchPrescription.searchsympotmName)
        binding.searchPrescriptionRv.visibility = View.GONE
    }
}