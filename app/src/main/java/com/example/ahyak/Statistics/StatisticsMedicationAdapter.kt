package com.example.ahyak.Statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.databinding.ItemStatisticsMedicationBinding

class StatisticsMedicationAdapter(val medicationList: ArrayList<StatMedication>): RecyclerView.Adapter<StatisticsMedicationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemStatisticsMedicationBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(medication: StatMedication){
            binding.itemStatisticsMedicationTv.text = medication.sympom
            binding.itemStatisticsMedicationPb.progress = medication.progress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatisticsMedicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = medicationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(medicationList[position])
    }
}