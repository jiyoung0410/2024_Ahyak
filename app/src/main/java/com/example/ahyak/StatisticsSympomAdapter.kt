package com.example.ahyak

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.databinding.ItemStatisticsSympomsBinding

class StatisticsSympomAdapter(var sympomList: ArrayList<String>) : RecyclerView.Adapter<StatisticsSympomAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemStatisticsSympomsBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(mySympom : String) {
            binding.itemStatisticsSympomsTv.text = mySympom
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatisticsSympomAdapter.ViewHolder {
        val binding = ItemStatisticsSympomsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticsSympomAdapter.ViewHolder, position: Int) {
        holder.bind(sympomList[position])
    }

    override fun getItemCount(): Int = sympomList.size
}