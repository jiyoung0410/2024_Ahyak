package com.example.ahyak.DB

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class GlobalApplication : Application() {
    private val NATIVE_APP_KEY : String = "9747a9ea1f6e6d8f451605dc1f18f9a1"
    override fun onCreate() {
        super.onCreate()

        var keyHash = Utility.getKeyHash(this)
        Log.i("GlobalApplication","$keyHash")
        //kakao sdk 초기화
        KakaoSdk.init(this, NATIVE_APP_KEY)
    }
}