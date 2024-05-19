package com.example.ahyak.PillRegister

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.Calendar.CalendarItemSympotmAdapter
import com.example.ahyak.databinding.ItemCalendarSymptomBinding
import com.example.ahyak.databinding.ItemRegisterpillSearchBinding

class RegisterPillAdapter(var registerPills:ArrayList<DataItemRegisterPill>, private val OnItemRegisterClickListener: RegisterPillActivity):RecyclerView.Adapter<RegisterPillAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:ItemRegisterpillSearchBinding, val context: Context) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init{
            binding.root.setOnClickListener(this)
        }

        fun bind(registerPill: DataItemRegisterPill){
            binding.itemRegisterpillSearchNameTv.text = registerPill.RegisterPillName
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                OnItemRegisterClickListener.onItemClick(registerPills[position])
        }

    }

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterPillAdapter.ViewHolder  =
        ViewHolder(
            ItemRegisterpillSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context
        )

    override fun onBindViewHolder(holder: RegisterPillAdapter.ViewHolder, position: Int) {
        holder.bind(registerPills[position])
    }

    override fun getItemCount(): Int {
        return registerPills.size
    }

    // 필터링된 목록 설정 메서드
    fun filterList(filteredList: ArrayList<DataItemRegisterPill>) {
        registerPills = filteredList
        notifyDataSetChanged()
    }
}