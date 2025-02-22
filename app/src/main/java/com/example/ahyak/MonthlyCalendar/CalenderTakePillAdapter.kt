package com.example.ahyak.MonthlyCalendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemCalendarWhetherTakePillBinding

class CalenderTakePillAdapter(val takepillList:ArrayList<DailyDataItemTakePill>):RecyclerView.Adapter<CalenderTakePillAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemCalendarWhetherTakePillBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(takepill:DailyDataItemTakePill){
            binding.itemCalendarTakePillNameTv.text = takepill.takepillname
            binding.itemCalendarTakePillPercentTv.text = takepill.takepillpercent.toString() + "%"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalenderTakePillAdapter.ViewHolder {
        val binding = ItemCalendarWhetherTakePillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalenderTakePillAdapter.ViewHolder, position: Int) {
        holder.bind(takepillList[position])
    }

    override fun getItemCount(): Int {
        return takepillList.size
    }
}