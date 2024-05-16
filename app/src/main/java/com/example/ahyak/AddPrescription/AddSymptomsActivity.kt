package com.example.ahyak.AddPrescription

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import com.example.ahyak.databinding.ActivityAddSymptomsBinding
import java.util.Calendar

class AddSymptomsActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var binding : ActivityAddSymptomsBinding
    private var cal : Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSymptomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addSymptomsStartdayLl.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                binding.addSymptomsStartdayTv.text =
                    year.toString() + "." + (month + 1).toString() + "." + day.toString()+ "."
            }, year, month, dayOfMonth)

            datePickerDialog.show()
        }

        binding.addSymptomsEnddayLl.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                binding.addSymptomsEnddayTv.text =
                    year.toString() + "." + (month + 1).toString() + "." + day.toString()+ "."
            }, year, month, dayOfMonth)
            datePickerDialog.show()
        }

        binding.addSymptomsCancleIv.setOnClickListener {
            finish()
        }

        binding.addSymptomsAddbtnLl.setOnClickListener {
            finish()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cal.set(year,month,dayOfMonth)

        cal.set(Calendar.HOUR_OF_DAY,0)
        cal.set(Calendar.MINUTE,0)
        cal.set(Calendar.SECOND,0)
    }
}