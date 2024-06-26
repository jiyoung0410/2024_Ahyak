package com.example.ahyak.MonthlyCalendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemCalendarWhetherTakePillBinding

class CalenderTakePillAdapter(val takepillList:ArrayList<DataItemTakePill>):RecyclerView.Adapter<CalenderTakePillAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemCalendarWhetherTakePillBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(takepill:DataItemTakePill){
            binding.itemCalendarTakePillNameTv.text = takepill.takepillname

            if(takepill.takepillwhether==true){
                binding.itemCalendarTakePillCheckbox.setBackgroundResource(R.drawable.checkbox_check)
            }else{
                binding.itemCalendarTakePillCheckbox.setBackgroundResource(R.drawable.checkbox_nocheck)
            }
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