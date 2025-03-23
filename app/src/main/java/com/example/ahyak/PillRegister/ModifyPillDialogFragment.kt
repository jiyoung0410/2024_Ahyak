package com.example.ahyak.PillRegister

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.ahyak.R
import com.example.ahyak.databinding.FragmentModifyPillDialogBinding

class ModifyPillDialogFragment(modifyPillDialogInterface: ModifyPillDialogInterface) : DialogFragment() {

    private lateinit var binding: FragmentModifyPillDialogBinding

    private var modifyPillDialogInterface: ModifyPillDialogInterface

    init {
        this.modifyPillDialogInterface = modifyPillDialogInterface
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentModifyPillDialogBinding.inflate(layoutInflater)

        //레이아웃 배경 투명화
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.modifyPillDialogBtn1Cl.setOnClickListener {
            //finish를 인터페이스 이용하여 activity에 전달
            dismiss()
            this.modifyPillDialogInterface.onConfirmButton1Click()
        }

        binding.modifyPillDialogBtn2Cl.setOnClickListener {
            //finish를 인터페이스 이용하여 activity에 전달
            dismiss()
            this.modifyPillDialogInterface.onConfirmButton2Click()
        }

        binding.modifyPillDialogBtn3Cl.setOnClickListener {
            //finish를 인터페이스 이용하여 activity에 전달
            dismiss()
            this.modifyPillDialogInterface.onConfirmButton3Click()
        }

        binding.modifyPillDialogCancelCl.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}