package com.example.ahyak.PillRegister

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.example.ahyak.MainActivity
import com.example.ahyak.databinding.ActivitySearchPillImageBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class SearchPillImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchPillImageBinding
    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200
    private var imageName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchPillImageBinding.inflate(layoutInflater)
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
        binding.imageStartLl.setOnClickListener {
            //val bitmap = binding.imageView.drawable.toBitmap()  // imageView에 있는 이미지 비트맵 가져오기
            //bringImage(bitmap)
            Toast.makeText(applicationContext,imageName,Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PillImageResultActivity::class.java)
            intent.putExtra("imageName",imageName)
            startActivity(intent)
        }

        // '다시 불러오기'버튼 클릭 시 처방전 가져오기 다시 등장
        binding.retryLl.setOnClickListener {
            binding.imageView.visibility = View.GONE
            binding.albumButton.visibility = View.VISIBLE
            binding.cameraButton.visibility = View.VISIBLE
            binding.PillImageTv.visibility = View.VISIBLE
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
                    //tempUri?.let { startCrop(it) }
                    tempUri?.let {
                        // 이미지 캐시를 방지하기 위해 무작위 쿼리 파라미터를 추가하여 새로 고침
                        //val newUri = Uri.parse(it.toString() + "?time=" + System.currentTimeMillis())

                        // ImageView에 새 이미지 설정
                        binding.imageView.setImageURI(tempUri)
                        binding.imageView.visibility = View.VISIBLE
                        binding.albumButton.visibility = View.GONE
                        binding.cameraButton.visibility = View.GONE
                        binding.PillImageTv.visibility = View.GONE
                        binding.selectLl.visibility = View.VISIBLE

                        Log.d("tempUri Success", "tempUri is not null")
                    }
                }

                GALLERY_REQUEST_CODE -> {
                    // 앨범에서 선택한 이미지 처리
                    val selectedImageUri: Uri? = data?.data
                    //selectedImageUri?.let { startCrop(it) }
                    selectedImageUri?.let {
                        // 이미지 캐시를 방지하기 위해 무작위 쿼리 파라미터를 추가하여 새로 고침
                        //val newUri = Uri.parse(it.toString() + "?time=" + System.currentTimeMillis())
                        imageName = getImageName(selectedImageUri)

                        // ImageView에 새 이미지 설정
                        binding.imageView.setImageURI(selectedImageUri)
                        binding.imageView.visibility = View.VISIBLE
                        binding.albumButton.visibility = View.GONE
                        binding.cameraButton.visibility = View.GONE
                        binding.PillImageTv.visibility = View.GONE
                        binding.selectLl.visibility = View.VISIBLE

                        Log.d("selectedUri Success", "selectedUri is not null")
                    }
                }

                // 크롭된 이미지 처리
//                UCrop.REQUEST_CROP -> {
//                    // 크롭된 이미지 처리
//                    val resultUri = UCrop.getOutput(data!!)
//                    resultUri?.let {
//                        // 이미지 캐시를 방지하기 위해 무작위 쿼리 파라미터를 추가하여 새로 고침
//                        val newUri = Uri.parse(it.toString() + "?time=" + System.currentTimeMillis())
//
//                        // ImageView에 새 이미지 설정
//                        binding.imageView.setImageURI(newUri)
//                        binding.imageView.visibility = View.VISIBLE
//                        binding.albumButton.visibility = View.GONE
//                        binding.cameraButton.visibility = View.GONE
//                        binding.PillImageTv.visibility = View.GONE
//                        binding.selectLl.visibility = View.VISIBLE
//
//                        Log.d("CropSuccess", "resultUri is not null")
//                    } ?: Log.e("CropError", "resultUri is null")
//                }
            }
        }
    }

//    private fun startCrop(uri: Uri) {
//        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))
//
//        val uCrop = UCrop.of(uri, destinationUri)
//
//        // 기본적으로 자유로운 비율 설정
//        //uCrop.useSourceImageAspectRatio()  // 원본 이미지 비율 사용
//        uCrop.withAspectRatio(0f, 0f)  // 자유롭게 비율 설정 가능 (0f, 0f는 비율 자유)
//        uCrop.withAspectRatio(21f, 9f)  // 자유롭게 비율 설정 가능 (0f, 0f는 비율 자유)
//
//        // 크롭 박스에 대해 최솟값과 최댓값 설정
//        //uCrop.withMaxResultSize(1080, 1080)  // 최대 크기 설정
//        uCrop.start(this)
//    }

//    private fun bringImage(bitmap: Bitmap) {
//        val imageUri = getImageUri(bitmap)
//        val imageName = getImageName(imageUri)
//        Toast.makeText(applicationContext,imageName,Toast.LENGTH_SHORT).show()
//    }

    private fun getImageName(uri: Uri): String? {
        var name: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(contentResolver, inImage, "Temp Image", null)
        return Uri.parse(path)
    }

    /*
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
    */
}