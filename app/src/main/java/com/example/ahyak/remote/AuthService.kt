package com.example.ahyak.remote

import android.content.Context
import com.example.ahyak.ApplicationClass
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class AuthService(private val context: Context) {
    private val authService = ApplicationClass.retrofit.create(RetroInterface::class.java)

    private lateinit var drugSearchNameView: DrugSearchNameView

    fun setdrugSearchNameView(drugSearchNameView: DrugSearchNameView){
        this.drugSearchNameView = drugSearchNameView
    }

    fun drugSearchName(query:String){
        drugSearchNameView.DrugSearchNameLoading()
        val request = DrugSearchNameRequest(query)
        authService.drugsearchNamePost(request).enqueue(object : Callback<DrugSearchNameResponse> {
            override fun onResponse(
                call : Call<DrugSearchNameResponse>,
                response: Response<DrugSearchNameResponse>
            ){
                val resp = response.body()
                // onResponse 처리
            }

            override fun onFailure(call: Call<DrugSearchNameResponse>, t: Throwable) {
                // onFailure 처리
            }
        })
    }

}