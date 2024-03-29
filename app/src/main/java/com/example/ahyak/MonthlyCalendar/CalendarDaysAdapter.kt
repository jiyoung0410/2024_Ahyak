package com.example.ahyak.MonthlyCalendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.databinding.ItemCalendarDaysBinding
import com.example.ahyak.databinding.ItemCalendarWeekBinding
import java.util.Calendar

class CalendarDaysAdapter(var dateList: ArrayList<String>,year: Int,month: Int) : RecyclerView.Adapter<CalendarDaysAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCalendarDaysBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myDays: String) {
            val nowCal = Calendar.getInstance()
            binding.itemCalendarDaysTv.text = myDays

            if(myDays.toInt()==-1) {
                binding.itemCalendarDaysTv.setTextColor(Color.WHITE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDaysAdapter.ViewHolder {
        val binding = ItemCalendarDaysBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dateList.size

    override fun onBindViewHolder(holder: CalendarDaysAdapter.ViewHolder, position: Int) {
        holder.bind(dateList[position])
    }
}