package com.example.ahyak.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetroInterface {
    @POST("drugsearch_name")
    fun drugsearchNamePost(
        @Body request: DrugSearchNameRequest //내부에 포함할 데이터
    ) : Call<DrugSearchNameResponse> //반환할 데이터

    @POST("autocomplete")
    fun autocompletePost(
        @Body request: DrugSearchNameRequest
    ) : Call<AutoCompleteResponse>

    @POST("drugsearch_shape")
    fun drugsearchShapePost(
        @Body request: DrugSearchShapeRequest
    ) : Call<DrugSearchShapeResponse>

    @POST("effectinfo")
    fun effectinfoPost(
        @Body requeest:EffectInfoRequest
    ):Call<EffectInfoResponse>
}