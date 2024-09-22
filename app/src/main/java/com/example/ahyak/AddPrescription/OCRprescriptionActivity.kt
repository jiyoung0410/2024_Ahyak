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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOcrprescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 카메라 버튼 클릭 시 카메라 실행
        binding.cameraButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }

        // 취소 버튼 클릭 시 액티비티 종료
        binding.cameraCancleIc.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            if (resultUri != null) {
                binding.imageView.setImageURI(resultUri)
                binding.imageView.visibility = View.VISIBLE
            } else {
                Log.e("CropError", "resultUri is null")
            }
        }


        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // 사진이 찍힌 후 바로 데이터로 변환
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            val tempUri = getImageUri(photo)
            tempUri?.let {
                startCrop(it)
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                // 크롭된 이미지를 ImageView에 표시
                binding.imageView.setImageURI(it)
                binding.imageView.visibility = View.VISIBLE
                Log.d("CropSuccess", "resultUri is not null")
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
