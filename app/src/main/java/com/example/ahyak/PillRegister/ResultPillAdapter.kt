package com.example.ahyak.PillRegister

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemResultPillBinding

class ResultPillAdapter(val onClick: (pillName: String) -> Unit) : RecyclerView.Adapter<ResultPillAdapter.ViewHolder>() {
    lateinit var resultPillList : ArrayList<DataItemResultPill>
    private var selectedPosition = -1 // 선택된 아이템의 위치를 저장하는 변수 추가

    fun build(i:ArrayList<DataItemResultPill>):ResultPillAdapter{
        resultPillList = i
        return this
    }
    inner class ViewHolder(val binding : ItemResultPillBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){

        fun bind(resultpill : DataItemResultPill){
            binding.itemResultPillImg.setImageResource(resultpill.resultpillimg)
            binding.itemResultPillNameTv.text = resultpill.resultpillname
            binding.itemResultPillCodeTv.text = resultpill.resultpillcode

            binding.root.setOnClickListener {
                val previousPosition = selectedPosition // 이전에 선택된 아이템의 위치 저장
                selectedPosition = adapterPosition // 현재 선택된 아이템의 위치 저장
                notifyItemChanged(previousPosition) // 이전에 선택된 아이템의 가시성 변경 갱신
                notifyItemChanged(selectedPosition) // 현재 선택된 아이템의 가시성 변경 갱신

                val resultpillName = binding.itemResultPillNameTv.text

                onClick(resultpillName.toString()) // 생성자 파라미터로 받은 람다함수 onClick 실행
            }
            if (adapterPosition ==selectedPosition){
                binding.resultPillItem.setBackgroundResource(R.drawable.white_radi_5dp_point_stroke)
            }else{
                binding.resultPillItem.setBackgroundResource(R.drawable.white_radi_5dp_gray1_stroke)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultPillAdapter.ViewHolder =
        ViewHolder(
            ItemResultPillBinding.inflate(LayoutInflater.from(parent.context),parent,false),
            parent.context
        )

    override fun getItemCount(): Int {
        return resultPillList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultPillList[position])
    }

}