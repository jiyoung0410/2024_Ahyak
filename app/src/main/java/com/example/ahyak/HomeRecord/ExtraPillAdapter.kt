package com.example.ahyak.HomeRecord

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.ahyak.AddPrescription.AddPrescriptionActivity
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.DB.ExtraPillEntity
import com.example.ahyak.PillRegister.ExtraRegisterPillActivity
import com.example.ahyak.R
import com.example.ahyak.databinding.ItemCalendarAddPillBinding
import com.example.ahyak.databinding.ItemCalendarExtraPillBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExtraPillAdapter(val extrapillList: ArrayList<ExtraPillEntity>):RecyclerView.Adapter<ExtraPillAdapter.ViewHolder>() {

    var ahyakDatabase : AhyakDataBase? = null
    inner class ViewHolder(val binding : ItemCalendarExtraPillBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(extrapill: ExtraPillEntity){
            binding.itemCalendarExtraPillVolumeTv.text = extrapill.PillVolume + extrapill.PillType
            binding.itemCalendarExtraPillNameTv.text = extrapill.PillName
            binding.itemCalendarExtraPillEatTimeTv.text = extrapill.PillTime

//            binding.root.setOnLongClickListener {
//                if(binding.itemCalendarExtraPillDeleteCl.visibility == View.VISIBLE) {
//                    binding.itemCalendarExtraPillDeleteCl.visibility = View.GONE
//                } else if(binding.itemCalendarExtraPillDeleteCl.visibility == View.GONE) {
//                    binding.itemCalendarExtraPillDeleteCl.visibility = View.VISIBLE
//                }
//                true
//            }
//
//            binding.itemCalendarExtraPillDeleteCl.setOnClickListener {
//                val position = adapterPosition
//                if(position != RecyclerView.NO_POSITION) {
//                    binding.itemCalendarExtraPillDeleteCl.visibility = View.GONE
//                    extrapillList.removeAt(position)
//                    GlobalScope.launch(Dispatchers.IO) {
//                        ahyakDatabase = AhyakDataBase.getInstance(context)
//                        ahyakDatabase!!.getExtraPillDao().deletePill(extarpill.PillName,extarpill.PillDay,extarpill.PillSlot)
//                        notifyItemRemoved(position)
//                        notifyDataSetChanged()
//                    }
//                }
//            }

            binding.itemCalendarExtraPillMoreCl.setOnClickListener {
                val popupmenu = PopupMenu(context, binding.itemCalendarExtraPillMoreCl)
                popupmenu.menuInflater.inflate(R.menu.menu_item_more, popupmenu.menu)

                popupmenu.setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.item_more_menu1 -> {
                            //수정 시 행동
                            val intent = Intent(binding.root.context, ExtraRegisterPillActivity::class.java).apply {
                                putExtra("extrapillmodify_name", extrapill.PillName)
                                putExtra("extrapillmodify_volume", extrapill.PillVolume)
                                putExtra("extrapillmodify_type", extrapill.PillType)
                                putExtra("extrapillmodify_month",extrapill.PillMonth)
                                putExtra("extrapillmodify_day",extrapill.PillDay)
                                putExtra("extrapillmodify_time",extrapill.PillTime)
                            }
                            //parentFragmentManager.popBackStack()
                            binding.root.context.startActivity(intent)
                            true
                        }
                        R.id.item_more_menu2 -> {
                            //삭제 시 행동
                            val position = adapterPosition
                            if(position != RecyclerView.NO_POSITION) {
                                binding.itemCalendarExtraPillDeleteCl.visibility = View.GONE
                                extrapillList.removeAt(position)
                                GlobalScope.launch(Dispatchers.IO) {
                                    ahyakDatabase = AhyakDataBase.getInstance(context)
                                    ahyakDatabase!!.getExtraPillDao().deletePill(extrapill.PillName,extrapill.PillDay,extrapill.PillSlot)
                                    withContext(Dispatchers.Main) {
                                        notifyItemRemoved(position)
                                        notifyDataSetChanged()
                                    }
                                }
                            }
                            true
                        }
                        else -> false
                    }
                }
                popupmenu.show()
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