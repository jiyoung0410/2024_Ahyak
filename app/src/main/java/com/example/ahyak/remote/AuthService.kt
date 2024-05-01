package com.example.ahyak.remote

import android.content.Context
import android.util.Log
import com.example.ahyak.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService(private val context: Context) {
    private val authService = ApplicationClass.retrofit.create(RetroInterface::class.java)

    private lateinit var drugSearchNameView: DrugSearchNameView

    fun setdrugSearchNameView(drugSearchNameView: DrugSearchNameView){
        this.drugSearchNameView = drugSearchNameView
    }

    fun drugSearchName(query:String){
        drugSearchNameView.DrugSearchNameLoading()
        val request = DrugSearchNameRequest(query)
        authService.drugsearchNamePost(request).enqueue(object : Callback<DrugSearchNameResponse>{
            override fun onResponse(
                call : Call<DrugSearchNameResponse>,
                response: Response<DrugSearchNameResponse>
            ){
                val resp = response.body()
                Log.d("response success",resp.toString())
                when(resp!!.result){
                    null -> drugSearchNameView.DrugSearchNameFailure()
                    else -> {
                        val Response = resp.result // WeightCheckResponse 클래스에 따라 result에 응답 데이터가 있을 것이라 가정합니다
                        val drugresult = Response
                        drugSearchNameView.DrugSearchNameSuccess(drugresult)
                    }
                }
            }
            override fun onFailure(call: Call<DrugSearchNameResponse>, t: Throwable) {
                Log.d("Failed",t.toString())
            }
        })
    }

}