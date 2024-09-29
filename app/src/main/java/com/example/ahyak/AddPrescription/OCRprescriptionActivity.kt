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

        // '다시 불러오기'버튼 클릭 시 처방전 가져오기 다시 등장
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

                // 크롭된 이미지 처리
                UCrop.REQUEST_CROP -> {
                    // 크롭된 이미지 처리
                    val resultUri = UCrop.getOutput(data!!)
                    resultUri?.let {
                        // 이미지 캐시를 방지하기 위해 무작위 쿼리 파라미터를 추가하여 새로 고침
                        val newUri = Uri.parse(it.toString() + "?time=" + System.currentTimeMillis())

                        // ImageView에 새 이미지 설정
                        binding.imageView.setImageURI(newUri)
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

        // 기본적으로 자유로운 비율 설정
        //uCrop.useSourceImageAspectRatio()  // 원본 이미지 비율 사용
        uCrop.withAspectRatio(0f, 0f)  // 자유롭게 비율 설정 가능 (0f, 0f는 비율 자유)
        //uCrop.withAspectRatio(21f, 9f)  // 자유롭게 비율 설정 가능 (0f, 0f는 비율 자유)

        // 크롭 박스에 대해 최솟값과 최댓값 설정
        //uCrop.withMaxResultSize(1080, 1080)  // 최대 크기 설정
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

                // 여기서 processRecognizedText를 호출하여 인식된 텍스트 처리
                processRecognizedText(visionText)
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

        val allData = mutableListOf<String>() // OCR로 인식된 모든 데이터를 저장할 리스트

        // 원하는 텍스트 형식으로 결과를 처리
        for (block in visionText.textBlocks) {
            for (line in block.lines) {
                val lineText = line.text.trim()

                // 약 이름이나 숫자를 모두 저장
                if (lineText.matches(Regex("[가-힣A-Za-z]+")) || lineText.matches(Regex("\\d+"))) {
                    allData.add(lineText)
                }
            }
        }

        // 데이터 크기가 올바르지 않을 경우
        if (allData.size % 4 != 0) {
            Log.e("OCR Error", "데이터의 형식이 올바르지 않습니다. 총 데이터의 개수가 맞지 않습니다.")
            return
        }

        // 데이터를 순차적으로 묶어서 약 정보로 처리
        val recognizedData = mutableListOf<List<String>>()

        val totalDrugs = allData.size / 4 // 총 약의 개수는 데이터 총 길이 나누기 4

        for (i in 0 until totalDrugs) {
            // 인덱스를 맞춰 4개의 데이터를 묶어줌
            val drugInfo = listOf(
                allData[i],                   // 약 이름
                allData[i + totalDrugs],       // 1회 투여량
                allData[i + totalDrugs * 2],   // 1일 투여횟수
                allData[i + totalDrugs * 3]    // 총 투약 일수
            )
            recognizedData.add(drugInfo)
        }

        // 결과 출력
        for (drugInfo in recognizedData) {
            Log.d("OCR Processed", "약 정보: $drugInfo")
            Toast.makeText(this, "약 정보: ${drugInfo[0]}, 1회 투여량: ${drugInfo[1]}, 1일 투여횟수: ${drugInfo[2]}, 총 투약 일수: ${drugInfo[3]}", Toast.LENGTH_SHORT).show()
        }

        // recognizedData를 새로운 Activity로 전달
        finish()
        val intent = Intent(this, OcrResultActivity::class.java)
        intent.putStringArrayListExtra("drugInfoList", ArrayList(recognizedData.flatten()))  // 데이터를 1차원 배열로 전달
        startActivity(intent)
    }

}
