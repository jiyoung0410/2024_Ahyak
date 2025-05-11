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

    @POST("auth/refresh")
    fun refreshToken(
        @Body request: RefreshTokenRequest
    ) : Call<BaseResponse<LoginResponse>>

    @POST("auth/login")
    fun login(
        @Body request: SignupRequest
    ) : Call<BaseResponse<LoginResponse>>

    //처방 등록
    @POST("prescription")
    fun registPrescription(
        @Body request: RegistPresRequest
    ) : Call<BaseResponse<RegistPresResponse>>
}