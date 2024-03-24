package com.example.ahyak.MonthlyCalendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemCalendarInconvenienceSymptomsBinding

class CalenderInconvenienceSymptomsAdapter(val calendersymptoms:ArrayList<DataItemCalenderSymptoms>):RecyclerView.Adapter<CalenderInconvenienceSymptomsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemCalendarInconvenienceSymptomsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(calendersymptom:DataItemCalenderSymptoms){
            binding.itemCalenderSymptomNameTv.text = calendersymptom.calendersymptomname

            when (calendersymptom.calendersymptomscolor) {
                1 -> { binding.itemInconvenienceSymptom.setBackgroundResource(R.drawable.light_point_radi_15dp) }
                2 -> { binding.itemInconvenienceSymptom.setBackgroundResource(R.drawable.point_radi_15dp)
                    binding.itemCalenderSymptomNameTv.setTextColor(Color.WHITE)}
                3 -> { binding.itemInconvenienceSymptom.setBackgroundResource(R.drawable.light_deep_point_radi_15dp)
                    binding.itemCalenderSymptomNameTv.setTextColor(Color.WHITE)}
                4 -> { binding.itemInconvenienceSymptom.setBackgroundResource(R.drawable.reguler_deep_point_radi_15dp_)
                    binding.itemCalenderSymptomNameTv.setTextColor(Color.WHITE)}
                5 -> {
                    binding.itemInconvenienceSymptom.setBackgroundResource(R.drawable.deep_point_radi_15dp)
                    binding.itemCalenderSymptomNameTv.setTextColor(Color.WHITE)
                }
                else -> { binding.itemInconvenienceSymptom.setBackgroundResource(R.drawable.white_radi_15dp_gray1_stroke) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalenderInconvenienceSymptomsAdapter.ViewHolder {
        val binding = ItemCalendarInconvenienceSymptomsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalenderInconvenienceSymptomsAdapter.ViewHolder, position: Int) {
        holder.bind(calendersymptoms[position])
    }

    override fun getItemCount(): Int {
        return calendersymptoms.size
    }
}