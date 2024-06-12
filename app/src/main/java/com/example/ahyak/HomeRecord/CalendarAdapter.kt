package com.example.ahyak.HomeRecord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemTodayWeekCalenderBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarAdapter(private val cList: List<CalendarVO>, private val selectDate: LocalDateTime,
                      private val existSelectedItem: Boolean, val onClick: (CalendarVO)->(Unit)) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
//    private var selectedDay: CalendarVO = CalendarVO("","")

    inner class CalendarViewHolder(private val binding: ItemTodayWeekCalenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalendarVO) {
            binding.date.text = item.cl_date
            binding.day.text = item.cl_day

//            var today = binding.date.text

            // 오늘 날짜
//            val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                LocalDate.now().format(DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko")))
//            } else {
//                TODO("VERSION.SDK_INT < O")
//            }

            // 오늘 날짜와 캘린더의 오늘 날짜가 같을 경우 background_blue 적용하기
            if (selectedItemPosition == RecyclerView.NO_POSITION && existSelectedItem == true &&
                binding.date.text == selectDate.format(DateTimeFormatter.ofPattern("d").withLocale(Locale.forLanguageTag("ko")))) {
                // 클릭된 아이템의 경우
                selectedItemPosition = adapterPosition
//                selectedDay.cl_day = binding.day.text.toString()
//                selectedDay.cl_date = binding.date.text.toString()

                binding.weekCardview.setBackgroundResource(R.drawable.today_week_calender_style_select)
                binding.date.setTextColor(ContextCompat.getColor(binding.root.context, R.color.point))
                binding.day.setTextColor(ContextCompat.getColor(binding.root.context, R.color.point))
            } else {
                // 클릭되지 않은 아이템의 경우
                binding.weekCardview.setBackgroundResource(R.drawable.today_week_calender_style)
                binding.date.setTextColor(ContextCompat.getColor(binding.root.context,R.color.white))
                binding.day.setTextColor(ContextCompat.getColor(binding.root.context,R.color.white))
            }

            binding.root.setOnClickListener {
                notifyItemChanged(selectedItemPosition)
                selectedItemPosition = adapterPosition
//                selectedDay.cl_day = binding.day.text.toString()
//                selectedDay.cl_date = binding.date.text.toString()

                binding.weekCardview.setBackgroundResource(R.drawable.today_week_calender_style_select)
                binding.date.setTextColor(ContextCompat.getColor(binding.root.context, R.color.point))
                binding.day.setTextColor(ContextCompat.getColor(binding.root.context, R.color.point))
                onClick(item)
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

//    fun getSelectedPosition(): CalendarVO = selectedDay
}