package com.example.ahyak.OCR

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.RecordSymptoms.DataItemSearchSymptom
import com.example.ahyak.databinding.ItemPrescriptionSearchBinding

class SearchPrescriptionAdapter(var searchPrescriptions:ArrayList<DataItemSearchSymptom>, private val itemClickListener: OcrResultActivity):RecyclerView.Adapter<SearchPrescriptionAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemPrescriptionSearchBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init{
            binding.root.setOnClickListener(this)
        }

        fun bind(searchPrescription: DataItemSearchSymptom){
            binding.itemPrescriptionSearchTv.text = searchPrescription.searchsympotmName
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                itemClickListener.onItemClick(searchPrescriptions[position])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemPrescriptionSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchPrescriptions[position])
    }

    override fun getItemCount(): Int {
        return searchPrescriptions.size
    }

    // 필터링된 목록 설정 메서드
    fun filterList(filteredList: ArrayList<DataItemSearchSymptom>) {
        searchPrescriptions = filteredList
        notifyDataSetChanged()
    }
}