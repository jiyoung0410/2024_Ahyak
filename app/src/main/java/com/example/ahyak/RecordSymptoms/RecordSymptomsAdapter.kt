package com.example.ahyak.RecordSymptoms

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemSymptomsRecordBinding

class RecordSymptomsAdapter(val recordSymptoms:ArrayList<DataItemRecordSymptom>):RecyclerView.Adapter<RecordSymptomsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemSymptomsRecordBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(recordSymptom: DataItemRecordSymptom){
            binding.itemSymptomName.text = recordSymptom.recordsympotmName

            val backgroundColor = recordSymptom.recordsympotmNum

            when (backgroundColor) {
                1 -> { binding.itemSymptom.setBackgroundResource(R.drawable.light_point_radi_15dp) }
                2 -> { binding.itemSymptom.setBackgroundResource(R.drawable.point_radi_15dp)
                    binding.itemSymptomName.setTextColor(Color.WHITE)}
                3 -> { binding.itemSymptom.setBackgroundResource(R.drawable.light_deep_point_radi_15dp)
                    binding.itemSymptomName.setTextColor(Color.WHITE)}
                4 -> { binding.itemSymptom.setBackgroundResource(R.drawable.reguler_deep_point_radi_15dp_)
                    binding.itemSymptomName.setTextColor(Color.WHITE)}
                5 -> {
                    binding.itemSymptom.setBackgroundResource(R.drawable.deep_point_radi_15dp)
                    binding.itemSymptomName.setTextColor(Color.WHITE)
                }
                else -> { binding.itemSymptom.setBackgroundResource(R.drawable.white_radi_15dp_gray1_stroke) }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordSymptomsAdapter.ViewHolder {
        val binding = ItemSymptomsRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //View Holder를 반환함. 그때 파라미터 값으로 binding을 넣어줌.
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordSymptomsAdapter.ViewHolder, position: Int) {
        holder.bind(recordSymptoms[position])
    }

    override fun getItemCount(): Int {
        return recordSymptoms.size
    }
}