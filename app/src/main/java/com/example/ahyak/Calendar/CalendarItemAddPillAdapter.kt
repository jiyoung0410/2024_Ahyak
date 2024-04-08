package com.example.ahyak.Calendar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.PillDetailGuide.DetailPillActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemCalendarAddPillBinding

class CalendarItemAddPillAdapter() : RecyclerView.Adapter<CalendarItemAddPillAdapter.ViewHolder>() {

    lateinit var addpillList: ArrayList<DataItemSymptom.DataItemAddPill>

    var takepill : Boolean = false

    fun build(i: ArrayList<DataItemSymptom.DataItemAddPill>):CalendarItemAddPillAdapter{
        addpillList = i
        return this
    }

    inner class ViewHolder(val binding: ItemCalendarAddPillBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(addpill:DataItemSymptom.DataItemAddPill){
            binding.itemCalendarPillVolumeTv.text = addpill.pillvolume
            binding.itemCalendarPillNameTv.text = addpill.pillname

            // 복용 상태를 나타내는 변수
            takepill = false

            binding.itemCalendarPillTakeStateLl.setOnClickListener {
                // 클릭할 때마다 복용 상태를 토글
                takepill = !takepill

                // 복용 상태에 따라 layout 스타일 변경
                if (takepill) {
                    // 즐겨찾기 등록 로직
                    binding.itemCalendarPillTakeStateLl.setBackgroundResource(R.drawable.white_radi_50dp_gray1_stroke)
                    binding.itemCalendarPillEatStateTv.setTextColor(Color.GRAY)
                    binding.itemCalendarPillEatStateTv.setText("완료")
                } else {
                    binding.itemCalendarPillTakeStateLl.setBackgroundResource(R.drawable.point_radi_50dp)
                    binding.itemCalendarPillEatStateTv.setTextColor(Color.WHITE)
                    binding.itemCalendarPillEatStateTv.setText("복용")
                }
            }
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarItemAddPillAdapter.ViewHolder =
        ViewHolder(ItemCalendarAddPillBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CalendarItemAddPillAdapter.ViewHolder, position: Int) {
        holder.bind(addpillList[position])
    }

    override fun getItemCount(): Int = addpillList.size

}