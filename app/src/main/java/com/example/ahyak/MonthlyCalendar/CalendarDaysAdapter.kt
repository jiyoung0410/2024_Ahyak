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

class CalendarDaysAdapter(var dateList: ArrayList<CalDaysInfo>,val onClick: (CalDaysInfo)->(Unit)) :
    RecyclerView.Adapter<CalendarDaysAdapter.ViewHolder>() {

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    inner class ViewHolder(val binding: ItemCalendarDaysBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myDays: CalDaysInfo) {
            val nowCal = Calendar.getInstance()
            binding.itemCalendarDaysTv.text = myDays.day

            if(myDays.day.toInt()==-1) {
                binding.itemCalendarDaysTv.setTextColor(Color.WHITE)
            }

            if(adapterPosition == selectedItemPosition) {
                if(myDays.bg == 1) {
                    binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.double_circle_point_point_stroke)
                } else if(myDays.bg == 2) {
                    binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.double_circle_gray3_point_stroke)
                } else if(myDays.bg == 0) {
                    binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.radi_50dp_point_stroke)
                }
            } else {
                when(myDays.bg) {
                    0 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.white_radi_100dp)
                    //복용 완료
                    1 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.point_radi_50dp)
                    //복용 미완
                    2 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.gray3_radi_50dp)
                    //선택 날짜 설정
                    3 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.radi_50dp_point_stroke)
                    //선택 날짜 설정 & 복용 완료
                    4 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.double_circle_point_point_stroke)
                    //선택 날짜 설정 & 복용 미완
                    5 -> binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.double_circle_gray3_point_stroke)
                }
            }

            binding.root.setOnClickListener {
                val previousPosition = selectedItemPosition
                selectedItemPosition = adapterPosition
                if(myDays.bg == 1) {
                    binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.double_circle_point_point_stroke)
                } else if(myDays.bg == 2) {
                    binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.double_circle_gray3_point_stroke)
                } else if(myDays.bg == 0) {
                    binding.itemCalendarDaysCl.setBackgroundResource(R.drawable.radi_50dp_point_stroke)
                }
                notifyItemChanged(previousPosition)
//                notifyItemChanged(selectedItemPosition)
                onClick(myDays)
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