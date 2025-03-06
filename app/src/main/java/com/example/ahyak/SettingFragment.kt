package com.example.ahyak

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.ahyak.databinding.FragmentSettingBinding
import com.example.ahyak.databinding.FragmentTodayRecordBinding

class SettingFragment : Fragment() {
    lateinit var binding : FragmentSettingBinding
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater)

        binding.logoutBtn.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("네",
                    DialogInterface.OnClickListener { _, _ ->
                        Log.d(TAG, "로그아웃 취소 - 네")
                        //requireActivity().finish()
                    })
                .setNegativeButton("아니요",
                    DialogInterface.OnClickListener { _, _ ->
                        Log.d(TAG, "로그아웃 취소 - 아니요")
                    })
            val dialog = builder.create()
            dialog.show()

            // '네' 버튼 색상 변경
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.black, null))
            // '아니요' 버튼 색상 변경
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.black, null))

        }

        binding.secessionBtn.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("탈퇴 하시겠습니까?")
                .setPositiveButton("네",
                    DialogInterface.OnClickListener { _, _ ->
                        Log.d(TAG, "탈퇴 취소 - 네")
                        //requireActivity().finish()
                    })
                .setNegativeButton("아니요",
                    DialogInterface.OnClickListener { _, _ ->
                        Log.d(TAG, "탈퇴 취소 - 아니요")
                    })
            val dialog = builder.create()
            dialog.show()

            // '네' 버튼 색상 변경
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.black, null))
            // '아니요' 버튼 색상 변경
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.black, null))
        }
        return binding.root
    }
}