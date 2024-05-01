package com.example.ahyak.remote

import android.content.Context
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class AuthService(private val context: Context) {
    private val authService = ApplicationClass.retrofit.create(RetroInterface::class.java)

    private lateinit var drugSearchNameView: DrugSearchNameView

    fun setdrugSearchNameView(drugSearchNameView: DrugSearchNameView){
        this.drugSearchNameView = drugSearchNameView
    }

    fun drugSearchName(query:String){
        drugSearchNameView.DrugSearchNameLoading()
        val request = DrugSearchNameRequest(query)
        authService.drugsearchNamePost(request).enqueue(object : Callback<BaseResponse<DrugSearchNameResponse>>{
            override fun onResponse(
                call : Call<BaseResponse<DrugSearchNameResponse>>,
                response: Response<BaseResponse<DrugSearchNameResponse>>
            ){
                val resp = response.body()

            }
        })
    }

}