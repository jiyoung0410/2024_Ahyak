package com.example.ahyak.RecordSymptoms

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.TodayRecordSymptomEntity
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemSymptomsRecordBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordSymptomsAdapter(val recordSymptoms: ArrayList<TodayRecordSymptomEntity>):RecyclerView.Adapter<RecordSymptomsAdapter.ViewHolder>() {

    var ahyakDatabase : AhyakDataBase? = null
    inner class ViewHolder(val binding : ItemSymptomsRecordBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(recordSymptom: TodayRecordSymptomEntity){
            binding.itemSymptomName.text = recordSymptom.SymptomName

            val backgroundColor = recordSymptom.SymptomStrength

            when (backgroundColor) {
                1 -> { binding.itemSymptom.setBackgroundResource(R.drawable.light_point_radi_15dp) }
                2 -> { binding.itemSymptom.setBackgroundResource(R.drawable.point_radi_15dp)
                    binding.itemSymptomName.setTextColor(Color.WHITE)}
                3 -> { binding.itemSymptom.setBackgroundResource(R.drawable.light_deep_point_radi_15dp)
                    binding.itemSymptomName.setTextColor(Color.WHITE)}
                4 -> { binding.itemSymptom.setBackgroundResource(R.drawable.reguler_deep_point_radi_15dp_)
                    binding.itemSymptomName.setTextColor(Color.WHITE)}
                5 -> {
                    binding.itemSymptom.setBackgroundResource(R.drawable.deep_point_radi_15dp)
                    binding.itemSymptomName.setTextColor(Color.WHITE)
                }
                else -> { binding.itemSymptom.setBackgroundResource(R.drawable.white_radi_15dp_gray1_stroke) }
            }

            binding.root.setOnLongClickListener {
                val position = adapterPosition
                recordSymptoms.removeAt(position)
                GlobalScope.launch(Dispatchers.IO) {
                    ahyakDatabase = AhyakDataBase.getInstance(context)
                    //symptoms 데이터 제거
                    ahyakDatabase!!.getTodayRecordSymptomDao()?.deleteTodayRecordSymptom(recordSymptom.SymptomName)
                    withContext(Dispatchers.Main) {
                        notifyItemRemoved(position)
                        notifyDataSetChanged()
                    }
                }
                true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordSymptomsAdapter.ViewHolder {
        val binding = ItemSymptomsRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //View Holder를 반환함. 그때 파라미터 값으로 binding을 넣어줌.
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: RecordSymptomsAdapter.ViewHolder, position: Int) {
        holder.bind(recordSymptoms[position])
    }

    override fun getItemCount(): Int {
        return recordSymptoms.size
    }
}