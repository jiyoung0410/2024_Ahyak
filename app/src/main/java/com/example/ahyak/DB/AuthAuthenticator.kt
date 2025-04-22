package com.example.ahyak.DB

import android.content.Context
import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthAuthenticator(
    private val authService: RetroInterface
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = getRefreshToken()

        // refreshToken이 없거나, 무한 재시도 방지
        if (refreshToken.isNullOrEmpty() || responseCount(response) >= 3) {
            return null
        }

        val refreshRequest = RefreshTokenRequest(refreshToken)

        // 새로운 토큰 요청
        return try {
            val tokenResponse = authService.refreshToken(refreshRequest).execute()
            if (tokenResponse.isSuccessful) {
                val newAccessToken = tokenResponse.body()?.data?.accessToken
                val newRefreshToken = tokenResponse.body()?.data?.refreshToken

                if (!newAccessToken.isNullOrEmpty() && !newRefreshToken.isNullOrEmpty()) {
                    saveTokens(newAccessToken, newRefreshToken)
                } else {
                    Log.d("Token","액세스 혹은 리프레쉬 토큰 빔")
                }

                // 새 토큰으로 원래 요청 재시도
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                removeTokens()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }
}