package com.example.ahyak.AddPrescription

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.ahyak.MainActivity
import com.example.ahyak.databinding.ActivityOcrprescriptionBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.yalantis.ucrop.UCrop
import java.io.File

class OCRprescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOcrprescriptionBinding
    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOcrprescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 카메라 버튼 클릭 시 카메라 실행
        binding.cameraButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }

        // 앨범 버튼 클릭 시 갤러리 실행
        binding.albumButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }

        // 취소 버튼 클릭 시 액티비티 종료
        binding.cameraCancleIc.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //ocr버튼 클릭 시
        binding.ocrStartLl.setOnClickListener {
            val bitmap = binding.imageView.drawable.toBitmap()  // imageView에 있는 이미지 비트맵 가져오기
            recognizeTextFromImage(bitmap)

        }

        // '<'버튼 클릭 시 처방전 가져오기 다시 등장
        binding.retryLl.setOnClickListener {
            binding.imageView.visibility = View.GONE
            binding.albumButton.visibility = View.VISIBLE
            binding.cameraButton.visibility = View.VISIBLE
            binding.PrecriptionTv.visibility = View.VISIBLE
            binding.selectLl.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    // 카메라로 찍은 사진 처리
                    val photo: Bitmap = data?.extras?.get("data") as Bitmap
                    val tempUri = getImageUri(photo)
                    tempUri?.let { startCrop(it) }
                }

                GALLERY_REQUEST_CODE -> {
                    // 앨범에서 선택한 이미지 처리
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let { startCrop(it) }
                }

                UCrop.REQUEST_CROP -> {
                    // 크롭된 이미지 처리
                    val resultUri = UCrop.getOutput(data!!)
                    resultUri?.let {
                        binding.imageView.setImageURI(it)
                        binding.imageView.visibility = View.VISIBLE
                        binding.albumButton.visibility = View.GONE
                        binding.cameraButton.visibility = View.GONE
                        binding.PrecriptionTv.visibility = View.GONE
                        binding.selectLl.visibility = View.VISIBLE

                        Log.d("CropSuccess", "resultUri is not null")
                    } ?: Log.e("CropError", "resultUri is null")
                }
            }
        }
    }

    // 비트맵 이미지를 임시 URI로 변환
    private fun getImageUri(inImage: Bitmap): Uri? {
        val path = MediaStore.Images.Media.insertImage(contentResolver, inImage, "Temp Image", null)
        return Uri.parse(path)
    }

    // 크롭 작업 시작
    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))
        val uCrop = UCrop.of(uri, destinationUri)
        uCrop.withAspectRatio(1f, 1f) // 원하는 비율 설정
        uCrop.start(this)
    }

    //ocr 관련 코드
    // OCR 처리 함수
    private fun recognizeTextFromImage(bitmap: Bitmap) {
        // InputImage로 비트맵을 변환
        val image = InputImage.fromBitmap(bitmap, 0)

        // ML Kit TextRecognizer 생성 (on-device, 라틴 문자)
        // Cloud 기반의 텍스트 인식 (한글 등 다양한 언어 지원)
        val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())


        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // 인식 성공 시 텍스트 처리
                Log.d("OCR Result", "인식된 텍스트: ${visionText.text}")
            }
            .addOnFailureListener { e ->
                // 인식 실패 시 에러 처리
                Log.e("OCR Error", "텍스트 인식 실패: ${e.message}")
            }
    }

    // 인식된 텍스트 처리
    private fun processRecognizedText(visionText: Text) {
        val resultText = visionText.text
        Log.d("OCR Result", "인식된 텍스트: $resultText")

        // 원하는 텍스트 형식으로 결과를 처리
        for (block in visionText.textBlocks) {
            for (line in block.lines) {
                val lineText = line.text
                Log.d("OCR Line", lineText)

                // 여기서 약 이름, 투여 횟수, 투약 일수를 추출하는 로직 적용
                extractPrescriptionInfo(lineText)
            }
        }
    }

    // 텍스트에서 약 이름과 투여 횟수, 일수를 추출하는 함수
    private fun extractPrescriptionInfo(lineText: String) {
        // 약 이름, 1일 투여 횟수, 총 투약 일수 추출을 위한 정규식
        val pattern = Regex("([가-힣A-Za-z0-9]+)\\s+(\\d+)\\s+(\\d+)")
        val matchResult = pattern.find(lineText)

        if (matchResult != null) {
            val drugName = matchResult.groupValues[1] // 약 이름
            val dosePerDay = matchResult.groupValues[2] // 1일 투여 횟수
            val totalDays = matchResult.groupValues[3] // 총 투약 일수

            Log.d("OCR Extracted", "약 이름: $drugName, 투여 횟수: $dosePerDay, 총 일수: $totalDays")

            // UI에 결과 출력 (필요에 따라 사용)
            //binding.resultTextView.text = "약 이름: $drugName\n1일 투여 횟수: $dosePerDay\n총 투약 일수: $totalDays"
            Toast.makeText(this, "약 이름: $drugName\n1일 투여 횟수: $dosePerDay\n총 투약 일수: $totalDays", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("OCR Extracted", "추출된 정보가 없습니다.")
        }
    }
}
