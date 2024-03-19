package com.example.ahyak.PillRegister

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.databinding.ItemResultPillBinding

class ResultPillAdapter(val resultPillList:ArrayList<DataItemResultPill>):RecyclerView.Adapter<ResultPillAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemResultPillBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(resultpill : DataItemResultPill){
            binding.itemResultPillImg.setImageResource(resultpill.resultpillimg)
            binding.itemResultPillNameTv.text = resultpill.resultpillname
            binding.itemResultPillCodeTv.text = resultpill.resultpillcode

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResultPillBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return resultPillList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultPillList[position])
    }

}