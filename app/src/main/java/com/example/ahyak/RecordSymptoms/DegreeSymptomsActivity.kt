package com.example.ahyak.RecordSymptoms

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ahyak.R
import com.example.ahyak.databinding.ActivityDegreeSymptomsBinding

class DegreeSymptomsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDegreeSymptomsBinding
    var degreecolor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDegreeSymptomsBinding.inflate(layoutInflater)

        // Intent를 통해 전달된 데이터를 받음
        val searchText = intent.getStringExtra("searchText")
        binding.degreeSymptomsNameTv.setText(searchText)

        //'x'누르면
        binding.recordSymptomsCancleIv.setOnClickListener {
            finish()
        }

        //추가 누르면
        binding.degreeSymptomsAddLl.setOnClickListener {
            finish()
            val intent = Intent(this, RecordSymptomsActivity::class.java)
            intent.putExtra("degreeColor", DataItemRecordSymptom(searchText.toString(), degreecolor))
            startActivity(intent)
        }

        //강도를 선택했을 때
        //일단은 토스트 메세지만 띄움. UI 수정 예정
        binding.degree1.setOnClickListener {
            //Toast.makeText(this,"1", Toast.LENGTH_SHORT).show()
            binding.degreeSymptomsLl.setBackgroundResource(R.drawable.light_point_radi_15dp)
            binding.degreeSymptomsNameTv.setTextColor(Color.BLACK)
            degreecolor = 1
        }
        binding.degree2.setOnClickListener {
            //Toast.makeText(this,"2", Toast.LENGTH_SHORT).show()
            binding.degreeSymptomsLl.setBackgroundResource(R.drawable.point_radi_15dp)
            binding.degreeSymptomsNameTv.setTextColor(Color.WHITE)
            degreecolor = 2
        }
        binding.degree3.setOnClickListener {
            //Toast.makeText(this,"3", Toast.LENGTH_SHORT).show()
            binding.degreeSymptomsLl.setBackgroundResource(R.drawable.light_deep_point_radi_15dp)
            binding.degreeSymptomsNameTv.setTextColor(Color.WHITE)
            degreecolor = 3
        }
        binding.degree4.setOnClickListener {
            //Toast.makeText(this,"4", Toast.LENGTH_SHORT).show()
            binding.degreeSymptomsLl.setBackgroundResource(R.drawable.reguler_deep_point_radi_15dp_)
            binding.degreeSymptomsNameTv.setTextColor(Color.WHITE)
            degreecolor = 4
        }
        binding.degree5.setOnClickListener {
            //Toast.makeText(this,"5", Toast.LENGTH_SHORT).show()
            binding.degreeSymptomsLl.setBackgroundResource(R.drawable.deep_point_radi_15dp)
            binding.degreeSymptomsNameTv.setTextColor(Color.WHITE)
            degreecolor = 5
        }

        setContentView(binding.root)
    }
}