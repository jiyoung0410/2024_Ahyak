package com.example.ahyak

import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ahyak.AddPrescription.AddPrescriptionActivity
import com.example.ahyak.OCR.OCRprescriptionActivity
import com.example.ahyak.MonthlyCalendar.CalenderFragment
import com.example.ahyak.Statistics.StatisticsFragment
import com.example.ahyak.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //음성 인식 관련
    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fun replaceFragment(fragment: Fragment) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.containers, fragment)
            fragmentTransaction.commit()
        }

        replaceFragment(TodayRecordFragment())

        binding.bottomNavigationview.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_today -> {
                    replaceFragment(TodayRecordFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_statistics -> {
                    replaceFragment(StatisticsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_calender -> {
                    replaceFragment(CalenderFragment())
                    return@setOnNavigationItemSelectedListener true
                }else -> return@setOnNavigationItemSelectedListener false
            }
        }

        //음성인식 관련 코드
        requestPermission()

        // RecognizerIntent 생성
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)    // 여분의 키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)// 언어 설정

        // <말하기> 버튼 눌러서 음성인식 시작
        binding.speechBtn.setOnClickListener {
            // 새 SpeechRecognizer 를 만드는 팩토리 메서드
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this@MainActivity)
            speechRecognizer.setRecognitionListener(recognitionListener)    // 리스너 설정
            speechRecognizer.startListening(intent)                         // 듣기 시작
        }

        //임시 OCR 코드 입력
        binding.ocrBtn.setOnClickListener{
            //처방전 페이지로 이동
            val intent = Intent(this@MainActivity, OCRprescriptionActivity::class.java)
            startActivity(intent)
        }

    }

    //     권한 설정 메소드
    private fun requestPermission() {
        // 버전 체크, 권한 허용했는지 체크
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.RECORD_AUDIO), 0
            )
        }
    }


    // 리스너 설정
    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        // 말하기 시작할 준비가 되면 호출
        override fun onReadyForSpeech(params: Bundle) {
            Toast.makeText(this@MainActivity, "음성인식 시작", Toast.LENGTH_SHORT).show()
        }

        // 말하기 시작했을 때 호출
        override fun onBeginningOfSpeech() {
            binding.speechBtn.visibility = View.GONE
            binding.speechIngBtn.visibility = View.VISIBLE
        }

        // 입력받는 소리의 크기를 알려줌
        override fun onRmsChanged(rmsdB: Float) {}

        // 말을 시작하고 인식이 된 단어를 buffer에 담음
        override fun onBufferReceived(buffer: ByteArray) {}

        // 말하기를 중지하면 호출
        override fun onEndOfSpeech() {
            binding.speechBtn.visibility = View.VISIBLE
            binding.speechIngBtn.visibility = View.GONE
        }

        // 오류 발생했을 때 호출
        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
            Toast.makeText(this@MainActivity, "$message", Toast.LENGTH_SHORT).show()
            binding.speechBtn.visibility = View.VISIBLE
            binding.speechIngBtn.visibility = View.GONE
        }

        // 인식 결과가 준비되면 호출
        override fun onResults(results: Bundle) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            binding.speechBubbleLl.visibility = View.VISIBLE
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            for (i in matches!!.indices) {
                binding.speechBubbleTv.setText(matches[i])

                //음성 인식이 제대로 되었으면
                binding.speechCheckTv.setOnClickListener {
                    binding.speechBubbleLl.visibility = View.GONE
                    processSpeech(matches[i])
                }
            }
        }

        // 부분 인식 결과를 사용할 수 있을 때 호출
        override fun onPartialResults(partialResults: Bundle) {
        }

        // 향후 이벤트를 추가하기 위해 예약
        override fun onEvent(eventType: Int, params: Bundle) {
        }

        // 음성 인식 결과 처리 메서드
        private fun processSpeech(speech: String) {
            //증상 등록
            var prescription: String? = null
            var hospital: String? = null

            // 정규 표현식을 사용하여 "증상" 앞의 단어를 찾음
            val prescriptionPattern = Regex("(\\S+)\\s*증상")
            val hospitalPattern = Regex("(\\S+)\\s*병원")

            val prescriptionMatch = prescriptionPattern.find(speech)
            val hospitalMatch = hospitalPattern.find(speech)

            if (prescriptionMatch != null) {
                prescription = prescriptionMatch.groupValues[1]
                Log.d("Regex Match", "Found prescription: $prescription")
            } else {
                Log.d("Regex Match", "No prescription match found")
            }

            if (hospitalMatch != null) {
                hospital = hospitalMatch.groupValues[1]
                Log.d("Regex Match", "Found hospital: $hospital")
            } else {
                Log.d("Regex Match", "No hospital match found")
            }

            if (prescription != null && hospital != null) {
                val intent = Intent(this@MainActivity, AddPrescriptionActivity::class.java).apply {
                    putExtra("Speech_Prescription", prescription)
                    putExtra("Speech_Hospital", hospital)
                    Log.d("check check", "$prescription, $hospital")
                }
                startActivity(intent)
            } else if (speech.contains("처방전")) {
                val intent = Intent(this@MainActivity, OCRprescriptionActivity::class.java)
                startActivity(intent)
            } else {
                Log.d("check check", "Prescription or Hospital is null")
            }

            // "처방전 등록"이라는 음성 인식이 있을 때 OCRprescriptionActivity 시작
            if (speech.contains("처방전 등록")) {
                val intent = Intent(this@MainActivity, OCRprescriptionActivity::class.java)
                startActivity(intent)
            } else if (prescription != null && hospital != null) {
                val intent = Intent(this@MainActivity, AddPrescriptionActivity::class.java).apply {
                    putExtra("Speech_Prescription", prescription)
                    putExtra("Speech_Hospital", hospital)
                    Log.d("check check", "$prescription, $hospital")
                }
                startActivity(intent)
            } else {
                Log.d("check check", "Prescription or Hospital is null")
            }

        }

    }
}