package com.example.ahyak.DB

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetroInterface {

    //회원가입
    @POST("auth/signup")
    fun signup(
        @Body request: SignupRequest
    ) : Call<BaseResponse<MessageResponse>>

    @POST("auth/refresh")
    fun refreshToken(
        @Body request: RefreshTokenRequest
    ) : Call<BaseResponse<LoginResponse>>

    @POST("auth/login")
    fun login(
        @Body request: SignupRequest
    ) : Call<BaseResponse<LoginResponse>>

    //Daily Status - 조회
    @GET("/dailyStatus")
    fun getDailyStatus(
        @Query("date") date: String
    ): Call<BaseResponse<DailyStatusResponse>>

}