package com.example.ahyak.HomeRecord

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.ExtraPillEntity
import com.example.ahyak.databinding.ItemCalendarAddPillBinding
import com.example.ahyak.databinding.ItemCalendarExtraPillBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExtraPillAdapter(val extrapillList: ArrayList<ExtraPillEntity>):RecyclerView.Adapter<ExtraPillAdapter.ViewHolder>() {

    var ahyakDatabase : AhyakDataBase? = null
    inner class ViewHolder(val binding : ItemCalendarExtraPillBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(extarpill: ExtraPillEntity){
            binding.itemCalendarExtraPillVolumeTv.text = extarpill.PillVolume.toString()
            binding.itemCalendarExtraPillNameTv.text = extarpill.PillName
            binding.itemCalendarExtraPillEatTimeTv.text = extarpill.PillTime

            binding.root.setOnLongClickListener {
                if(binding.itemCalendarExtraPillDeleteCl.visibility == View.VISIBLE) {
                    binding.itemCalendarExtraPillDeleteCl.visibility = View.GONE
                } else if(binding.itemCalendarExtraPillDeleteCl.visibility == View.GONE) {
                    binding.itemCalendarExtraPillDeleteCl.visibility = View.VISIBLE
                }
                true
            }

            binding.itemCalendarExtraPillDeleteCl.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    binding.itemCalendarExtraPillDeleteCl.visibility = View.GONE
                    extrapillList.removeAt(position)
                    GlobalScope.launch(Dispatchers.IO) {
                        ahyakDatabase = AhyakDataBase.getInstance(context)
                        ahyakDatabase!!.getExtraPillDao().deletePill(extarpill.PillName,extarpill.PillDay,extarpill.PillSlot)
                        notifyItemRemoved(position)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExtraPillAdapter.ViewHolder
        = ViewHolder(ItemCalendarExtraPillBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)

    override fun onBindViewHolder(holder: ExtraPillAdapter.ViewHolder, position: Int) {
        holder.bind(extrapillList[position])
    }

    override fun getItemCount(): Int {
        return extrapillList.size
    }
}