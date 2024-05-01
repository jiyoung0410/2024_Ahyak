package com.example.ahyak.MonthlyCalendar

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemCalendarDaysBinding
import com.example.ahyak.databinding.ItemCalendarWeekBinding
import java.util.Calendar

class CalendarDaysAdapter(var dateList: ArrayList<CalDaysInfo>) : RecyclerView.Adapter<CalendarDaysAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCalendarDaysBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myDays: CalDaysInfo) {
            val nowCal = Calendar.getInstance()
            binding.itemCalendarDaysTv.text = myDays.day

            if(myDays.day.toInt()==-1) {
                binding.itemCalendarDaysTv.setTextColor(Color.WHITE)
            }
            when(myDays.bg) {
                1 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.point_radi_50dp)
                2 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.gray3_radi_50dp)
                3 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.radi_50dp_point_stroke)
                4 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.double_circle_point_point_stroke)
                5 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.double_circle_gray3_point_stroke)
            }
//            if(myDays.bg==1) {
//                Log.d("logcat",myDays.toString())
//                binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.radi_50dp_point_stroke)
//            }
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