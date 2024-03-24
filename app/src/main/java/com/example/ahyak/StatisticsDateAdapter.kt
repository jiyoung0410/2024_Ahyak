package com.example.ahyak

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.databinding.ItemStatisticsDateBinding
import com.example.ahyak.databinding.ItemStatisticsSympomsBinding

class StatisticsDateAdapter(var dateList: ArrayList<String>) : RecyclerView.Adapter<StatisticsDateAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemStatisticsDateBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(myDate : String) {
            binding.itemStatisticsDateTv.text = myDate
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatisticsDateAdapter.ViewHolder {
        val binding = ItemStatisticsDateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticsDateAdapter.ViewHolder, position: Int) {
        holder.bind(dateList[position])
    }

    override fun getItemCount(): Int = dateList.size
}