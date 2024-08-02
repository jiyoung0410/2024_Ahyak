package com.example.ahyak.HomeRecord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.DB.ExtraPillEntity
import com.example.ahyak.databinding.ItemCalendarExtraPillBinding

class ExtraPillAdapter(val extrapillList: ArrayList<ExtraPillEntity>):RecyclerView.Adapter<ExtraPillAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemCalendarExtraPillBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(extarpill: ExtraPillEntity){
            binding.itemCalendarExtraPillVolumeTv.text = extarpill.PillVolume + extarpill.PillType
            binding.itemCalendarExtraPillNameTv.text = extarpill.PillName
            binding.itemCalendarExtraPillEatTimeTv.text = extarpill.PillTime
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraPillAdapter.ViewHolder {
        val binding = ItemCalendarExtraPillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //View Holder를 반환함. 그때 파라미터 값으로 binding을 넣어줌.
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExtraPillAdapter.ViewHolder, position: Int) {
        holder.bind(extrapillList[position])
    }

    override fun getItemCount(): Int {
        return extrapillList.size
    }
}