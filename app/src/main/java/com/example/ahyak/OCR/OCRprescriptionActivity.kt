package com.example.ahyak.OCR

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
        val drugNames = mutableListOf<String>() // 약 이름 저장
        val numericData = mutableListOf<String>() // 숫자 데이터 저장

        // "처방 의약품의 명칭" 및 "투약일수" 좌표 탐지
        val startBlock = visionText.textBlocks.firstOrNull { it.text.contains("처방의약품의명칭") }
        val endBlock = visionText.textBlocks.firstOrNull { it.text.contains("투약일수") }

        if (startBlock == null || endBlock == null) {
            Log.e("OCR Error", "처방 의약품의 명칭 또는 투약일수 영역을 찾을 수 없습니다.")
            return
        }

        // 가로 좌표와 세로 좌표 설정
        val referenceLeft = startBlock.boundingBox?.left ?: 0
        val referenceRight = endBlock.boundingBox?.right ?: 0
        val referenceTop = startBlock.boundingBox?.bottom ?: 0

        Log.d("OCR Debug", "필터링 기준: left=$referenceLeft, right=$referenceRight, top=$referenceTop")

        // OCR 데이터를 처리하여 필터링
        for (block in visionText.textBlocks) {
            for (line in block.lines) {
                val lineText = line.text.trim()
                val boundingBox = line.boundingBox

                // 필터링 조건: 가로 범위 및 세로 범위 내
                if (boundingBox != null &&
                    boundingBox.left >= referenceLeft &&
                    boundingBox.right <= referenceRight &&
                    boundingBox.top >= referenceTop
                ) {
                    // 약 이름 처리
                    if (lineText.contains("(내복)")) {
                        drugNames.add(lineText)
                        Log.d("OCR Debug", "약 이름 저장: $lineText")
                    }
                    // 숫자만 포함된 텍스트 처리
                    else if (lineText.matches(Regex("^\\d+$"))) {
                        numericData.add(lineText)
                        Log.d("OCR Debug", "숫자 저장: $lineText")
                    }
                    // 숫자+문자 조합은 필터링
                    else if (lineText.matches(Regex(".*\\d.*"))) {
                        Log.d("OCR Debug", "숫자+문자 조합 필터링: $lineText")
                    } else {
                        Log.d("OCR Debug", "다른 텍스트 필터링: $lineText")
                    }
                }
            }
        }

        // 약 이름과 숫자 데이터의 크기 일치 여부 확인
        if (drugNames.size * 3 != numericData.size) {
            Log.e("OCR Error", "약 이름과 숫자 데이터의 크기가 일치하지 않습니다.")
            return
        }

        // 약 이름과 숫자 데이터를 매칭하여 결과 생성
        val recognizedData = mutableListOf<List<String>>()
        for (i in drugNames.indices) {
            recognizedData.add(
                listOf(
                    drugNames[i],                  // 약 이름
                    numericData[i],            // 1회 투약량
                    numericData[i+3],        // 1일 투여 횟수
                    numericData[i+6]         // 총 투약 일수
                )
            )
        }

        // 결과 출력
        for (drugInfo in recognizedData) {
            Log.d("OCR Processed", "약물 정보: $drugInfo")
            Toast.makeText(
                this,
                "약: ${drugInfo[0]}, 1회: ${drugInfo[1]}, 1일: ${drugInfo[2]}, 총: ${drugInfo[3]}",
                Toast.LENGTH_SHORT
            ).show()
        }

        // 결과 전달
        finish()
        val intent = Intent(this, OcrResultActivity::class.java)
        intent.putStringArrayListExtra("drugInfoList", ArrayList(recognizedData.flatten())) // 데이터를 1차원 배열로 전달
        startActivity(intent)
    }

}