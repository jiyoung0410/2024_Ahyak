package com.example.ahyak.DB

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetroInterface {

    //회원가입
    @POST("auth/signup")
    fun signup(
        @Body request: SignupRequest
    ) : Call<BaseResponse<MessageResponse>>

    @POST("auth/login")
    fun login(
        @Body request: SignupRequest
    ) : Call<BaseResponse<LoginResponse>>
}