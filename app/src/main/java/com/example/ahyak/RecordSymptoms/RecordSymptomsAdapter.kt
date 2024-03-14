package com.example.ahyak.RecordSymptoms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.databinding.ItemCalendarExtraPillBinding
import com.example.ahyak.databinding.ItemSymptomsRecordBinding

class RecordSymptomsAdapter(val recordSymptoms:ArrayList<DataItemRecordSymptom>):RecyclerView.Adapter<RecordSymptomsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemSymptomsRecordBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(recordSymptom: DataItemRecordSymptom){
            binding.itemSymptomName.text = recordSymptom.recordsympotmName
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