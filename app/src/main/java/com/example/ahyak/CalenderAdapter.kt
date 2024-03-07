package com.example.ahyak

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.databinding.ItemTodayWeekCalenderBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarAdapter(private val cList: List<CalendarVO>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(private val binding: ItemTodayWeekCalenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalendarVO) {
            binding.date.text = item.cl_date
            binding.day.text = item.cl_day

            var today = binding.date.text

            // 오늘 날짜
            val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.now().format(DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko")))
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            // 오늘 날짜와 캘린더의 오늘 날짜가 같을 경우 background_blue 적용하기
            if (today == now) {
                binding.weekCardview.setBackgroundResource(R.drawable.today_week_calender_style_select)
                binding.date.setTextColor(ContextCompat.getColor(binding.root.context, R.color.point))
                binding.day.setTextColor(ContextCompat.getColor(binding.root.context, R.color.point))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemTodayWeekCalenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(cList[position])
    }

    override fun getItemCount(): Int {
        return cList.size
    }

}