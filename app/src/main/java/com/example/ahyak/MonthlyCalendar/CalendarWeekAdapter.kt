package com.example.ahyak.MonthlyCalendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.databinding.ItemCalendarWeekBinding

class CalendarWeekAdapter(var dateList: ArrayList<String>) : RecyclerView.Adapter<CalendarWeekAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCalendarWeekBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myWeek: String) {
            binding.itemCalendarWeekTv.text = myWeek
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarWeekBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dateList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dateList[position])
    }
}