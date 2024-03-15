package com.example.ahyak.Calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemCalendarSymptomBinding

class CalendarItemSympotmAdapter(val onClick: ()->(Unit), val onAddPillClick: (DataItemSymptom) -> Unit) : RecyclerView.Adapter<CalendarItemSympotmAdapter.ViewHolder>() {

    lateinit var sympotms: ArrayList<DataItemSymptom>
    private var selectedPosition = -1 // 선택된 아이템의 위치를 저장하는 변수 추가
    fun build(i:ArrayList<DataItemSymptom>) : CalendarItemSympotmAdapter{
        sympotms = i
        return this
    }

    inner class ViewHolder(val binding: ItemCalendarSymptomBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){

        // 아래의 코드를 추가하여 item_addpill의 어댑터에 접근할 수 있도록 합니다.
        private val addPillAdapter: CalendarItemAddPillAdapter = CalendarItemAddPillAdapter()
        init {
            binding.itemCalendarSymptomPillRv.adapter = addPillAdapter
        }
        fun bind(sympotm:DataItemSymptom){

            binding.itemCalendarAddSymptomPillLl.setOnClickListener {
                // 아이템 추가 이벤트 발생
                onAddPillClick(sympotm)
            }

            binding.itemCalendarSymptomName.text = sympotm.sympotmname
            binding.itemCalendarSymptomDate.text= sympotm.startdate
            binding.itemCalendarSymptomHospitalName.text = sympotm.hospitalname
            binding.itemCalendarSymptomPillRv.apply {
                    adapter = CalendarItemAddPillAdapter().build(sympotm.ItemAddPill)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            binding.root.setOnClickListener {
                // 리사이클러뷰 아이템에 클릭이벤트 발생
                val previousPosition = selectedPosition // 이전에 선택된 아이템의 위치 저장
                selectedPosition = adapterPosition // 현재 선택된 아이템의 위치 저장
                notifyItemChanged(previousPosition) // 이전에 선택된 아이템의 가시성 변경 갱신
                notifyItemChanged(selectedPosition) // 현재 선택된 아이템의 가시성 변경 갱신
                onClick() // 생성자 파라미터로 받은 람다함수 onClick 실행
            }
            // 선택된 아이템인 경우 뷰의 가시성 변경
            if (adapterPosition == selectedPosition) {
                // 여러 가지 뷰들의 가시성을 조절하는 코드 작성
                binding.itemCalendarAddSymptomPillLl.visibility = View.VISIBLE
                binding.itemCalendarSymptomDate.visibility = View.GONE
                binding.itemCalendarSymptomPillRv.visibility = View.VISIBLE
                binding.itemCalendarSymptomPillAllTakeLl.visibility = View.VISIBLE
                binding.itemCalendarSymptomHospitalName.visibility = View.INVISIBLE
            } else {
                binding.itemCalendarAddSymptomPillLl.visibility = View.GONE
                binding.itemCalendarSymptomDate.visibility = View.VISIBLE
                binding.itemCalendarSymptomPillRv.visibility = View.GONE
                binding.itemCalendarSymptomPillAllTakeLl.visibility = View.GONE
                binding.itemCalendarSymptomHospitalName.visibility = View.VISIBLE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemSympotmAdapter.ViewHolder =
        ViewHolder(
            ItemCalendarSymptomBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context
        )

    override fun onBindViewHolder(holder: CalendarItemSympotmAdapter.ViewHolder, position: Int) {
        holder.bind(sympotms[position])
    }

    override fun getItemCount(): Int = sympotms.size
}