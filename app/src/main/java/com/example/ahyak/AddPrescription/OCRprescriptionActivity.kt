package com.example.ahyak.AddPrescription

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ahyak.MainActivity
import com.example.ahyak.databinding.ActivityOcrprescriptionBinding
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
}
