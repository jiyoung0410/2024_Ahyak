package com.example.ahyak.DB

import android.util.Log

fun getJwt() : String? { //서버에서 가져오기
    return ApplicationClass.mSharedPreferences?.getString("token",null)
}

fun removeJwt() { //회원탈퇴 = 삭제
    ApplicationClass.mSharedPreferences?.edit()?.clear()?.apply()
}


fun saveJwt(token: String) {
    if (ApplicationClass.mSharedPreferences == null) {
        Log.d("Signup response", "sharedpreference 빔")
    } else {
        Log.d("Signup Response", "Sharedpreference 안 빔")
    }
    ApplicationClass.mSharedPreferences?.edit()?.putString("token", token)?.apply()
}