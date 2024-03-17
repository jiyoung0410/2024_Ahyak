package com.example.ahyak.RecordSymptoms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.databinding.ItemCalendarExtraPillBinding
import com.example.ahyak.databinding.ItemSymptomsSearchBinding

class SearchSymptomsAdapter(var searchSymptoms:ArrayList<DataItemSearchSymptom>, private val itemClickListener: OnItemClickListener):RecyclerView.Adapter<SearchSymptomsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemSymptomsSearchBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(searchSymptom: DataItemSearchSymptom){
            binding.itemSymptomSearchNameTv.text = searchSymptom.searchsympotmName
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(searchSymptoms[position])
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSymptomsAdapter.ViewHolder {
        val binding = ItemSymptomsSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //View Holder를 반환함. 그때 파라미터 값으로 binding을 넣어줌.
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchSymptomsAdapter.ViewHolder, position: Int) {
        holder.bind(searchSymptoms[position])
    }

    override fun getItemCount(): Int {
        return searchSymptoms.size
    }
    // 필터링된 목록 설정 메서드
    fun filterList(filteredList: ArrayList<DataItemSearchSymptom>) {
        searchSymptoms = filteredList
        notifyDataSetChanged()
    }
}