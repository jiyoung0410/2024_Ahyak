package com.example.ahyak

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ahyak.DB.AuthService
import com.example.ahyak.DB.LogoutView
import com.example.ahyak.databinding.FragmentSettingBinding
import com.example.ahyak.databinding.FragmentTodayRecordBinding
import com.kakao.sdk.user.UserApiClient

class SettingFragment : Fragment(), LogoutView {
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
            val authService = AuthService(requireContext())
            authService.setLogoutView(this)
            builder.setMessage("탈퇴 하시겠습니까?")
                .setPositiveButton("네",
                    DialogInterface.OnClickListener { _, _ ->
                        Log.d(TAG, "탈퇴 취소 - 네")
                        authService.deleteUser()
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

    override fun deleteUserLoading() {
    }

    override fun deleteUserSuccess() {
        Log.d("deleteUser","회원 탈퇴 성공")
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("deleteKakaoAccount", "카카오 연결 끊기 실패", error)
                Toast.makeText(context, "카카오 연결 해제 실패", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("deleteKakaoAccount","카카오 연결 끊기 성공")
                Toast.makeText(context, "카카오 연결 해제 완료", Toast.LENGTH_SHORT).show()
            }
        }

        val intent = Intent(requireContext(),ActivityLogin::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun deleteUserFailure() {
        Log.d("deleteUser","회원 탈퇴 실패")
    }
}