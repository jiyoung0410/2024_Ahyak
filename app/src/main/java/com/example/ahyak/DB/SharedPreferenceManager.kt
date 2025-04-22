package com.example.ahyak.DB

import android.util.Log

private const val ACCESS_TOKEN = "accessToken"
private const val REFRESH_TOKEN = "refreshToken"

fun getAccessToken() : String? {
    return ApplicationClass.mSharedPreferences?.getString(ACCESS_TOKEN,null)
}

fun getRefreshToken() : String? {
    return ApplicationClass.mSharedPreferences?.getString(REFRESH_TOKEN,null)
}

fun removeTokens() { //회원탈퇴 = 삭제
    ApplicationClass.mSharedPreferences?.edit()?.clear()?.apply()
}


fun saveTokens(accessToken: String, refreshToken: String) {
    if (ApplicationClass.mSharedPreferences == null) {
        Log.d("Signup response", "sharedpreference 빔")
    } else {
        Log.d("Signup Response", "Sharedpreference 안 빔")
    }
//    ApplicationClass.mSharedPreferences?.edit()?.putString("token", token)?.apply()
    ApplicationClass.mSharedPreferences.edit().apply{
        putString(ACCESS_TOKEN,accessToken)
        putString(REFRESH_TOKEN,refreshToken)
        apply()
    }
}