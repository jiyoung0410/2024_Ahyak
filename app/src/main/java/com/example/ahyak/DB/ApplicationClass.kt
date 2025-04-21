package com.example.ahyak.DB

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {
    companion object {
        //const val X_ACCESS_TOKEN : String = "x-access-token"
        const val DEV_URL : String = "https://ahyak-be.vercel.app" //개발용 URL
        const val PROD_URL : String = "" //배포용 URL
        const val BASE_URL : String = DEV_URL // 상황에 따라 DEV와 PROD로 바뀐는 작업용 URL
        private val NATIVE_APP_KEY : String = "9747a9ea1f6e6d8f451605dc1f18f9a1"

        lateinit var mSharedPreferences : SharedPreferences
        lateinit var client: OkHttpClient
        lateinit var retrofit: Retrofit
        lateinit var authService: RetroInterface
    }

    override fun onCreate() {
        super.onCreate()

        var keyHash = Utility.getKeyHash(this)
        Log.i("GlobalApplication","$keyHash")
        //kakao sdk 초기화
        KakaoSdk.init(this, NATIVE_APP_KEY)

        mSharedPreferences = applicationContext.getSharedPreferences("My App Spf", Context.MODE_PRIVATE)
        Log.d("Signup Response","sharedpreference 초기화 확인")

        val tempClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val tempRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(tempClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authService = tempRetrofit.create(RetroInterface::class.java)

        client = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(XAccessTokenInterceptor()) //JWT 헤더 자동전송
            .authenticator(AuthAuthenticator(authService))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        //retrofit build, GsonConverterFactory는 Gson을 json으로 사용할 수 있게 함
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.d("GlobalApplication", "Retrofit 및 OkHttp 초기화 완료")
    }
}