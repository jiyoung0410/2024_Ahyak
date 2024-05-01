package com.example.ahyak.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetroInterface {
    @POST("drugsearch_name")
    fun signup(
        @Body request: DrugSearchNameRequest //내부에 포함할 데이터
    ) : Call<BaseResponse<DrugSearchNameResponse>> //반환할 데이터
}