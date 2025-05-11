package com.example.ahyak.DB

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class XAccessTokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val accessToken: String? = getAccessToken()
        Log.d("Interceptor", "AccessToken: $accessToken")

        accessToken?.let {
            builder.addHeader("Authorization","Bearer $accessToken")
            Log.d("token heder", accessToken)
        }

        return chain.proceed(builder.build())
    }
}