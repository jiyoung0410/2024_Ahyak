package com.example.ahyak.remote

import android.content.Context
import android.util.Log
import com.example.ahyak.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService(private val context: Context) {
    private val authService = ApplicationClass.retrofit?.create(RetroInterface::class.java)

    private lateinit var drugSearchNameView: DrugSearchNameView
    private lateinit var autoCompleteView: AutoCompleteView
    private lateinit var drugSearchShapeView : DrugSearchShapeView
    private lateinit var effectInfoView: EffectInfoView

    fun setdrugSearchNameView(drugSearchNameView: DrugSearchNameView){
        this.drugSearchNameView = drugSearchNameView
    }

    fun setautoCompleteView(autoCompleteView: AutoCompleteView) {
        this.autoCompleteView = autoCompleteView
    }

    fun setdrugSearchShapeView(drugSearchShapeView: DrugSearchShapeView){
        this.drugSearchShapeView = drugSearchShapeView
    }

    fun seteffectInfoView(effectInfoView: EffectInfoView){
        this.effectInfoView = effectInfoView
    }

    fun drugSearchName(query:String){
        drugSearchNameView.DrugSearchNameLoading()
        val request = DrugSearchNameRequest(query)
        authService?.drugsearchNamePost(request)?.enqueue(object : Callback<DrugSearchNameResponse>{
            override fun onResponse(
                call : Call<DrugSearchNameResponse>,
                response: Response<DrugSearchNameResponse>
            ){
                val resp = response.body()
                Log.d("name response success",resp.toString())
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
                Log.d("name Failed",t.toString())
            }
        })
    }

    fun drugSearchShape(_print:String, _drug_shape:String, _color:String, _type:String, _line:String){
        drugSearchShapeView.DrugSearchShapeLoading()
        val request = DrugSearchShapeRequest(_print, _drug_shape, _color, _type, _line)
        authService?.drugsearchShapePost(request)?.enqueue(object : Callback<DrugSearchShapeResponse>{
            override fun onResponse(
                call: Call<DrugSearchShapeResponse>,
                response: Response<DrugSearchShapeResponse>
            ) {
                val resp = response.body()
                Log.d("shape response success", resp.toString())
                when(resp!!.result){
                    null -> drugSearchShapeView.DrugSearchShapeFailure()
                    else ->{
                        val Response = resp.result
                        val drugresult = Response
                        drugSearchShapeView.DrugSearchShapeSuccess(drugresult)
                    }
                }
            }

            override fun onFailure(call: Call<DrugSearchShapeResponse>, t: Throwable) {
                Log.d("shape Failed",t.toString())
            }

        })
    }

    fun effectInfo(query:String){
        effectInfoView.EffectInfoLoading()
        val request = EffectInfoRequest(query)
        authService?.effectinfoPost(request)?.enqueue(object : Callback<EffectInfoResponse> {
            override fun onResponse(
                call: Call<EffectInfoResponse>,
                response: Response<EffectInfoResponse>
            ) {
                val resp = response.body()
                Log.d("effectInfo response seuccess", resp.toString())
                when(resp!!.effectreuslt){
                    null -> effectInfoView.EffectInfoFailure()
                    else ->{
                        val Response = resp.effectreuslt
                        val effectresult2 = Response
                        effectInfoView.EffectInfoSuccess(effectresult2)
                    }
                }
            }

            override fun onFailure(call: Call<EffectInfoResponse>, t: Throwable) {
                Log.d("effect Failed",t.toString())
            }
        })
    }

    fun autoComplete(query: String) {
        autoCompleteView.AutoCompleteLoading()
        val request = DrugSearchNameRequest(query)
        authService?.autocompletePost(request)?.enqueue(object : Callback<AutoCompleteResponse>{
            override fun onResponse(
                call : Call<AutoCompleteResponse>,
                response: Response<AutoCompleteResponse>
            ){
                val resp = response.body()
                Log.d("response success",resp.toString())
                when(resp!!.result){
                    null -> autoCompleteView.AutoCompleteFailure()
                    else -> {
                        val Response = resp.result // WeightCheckResponse 클래스에 따라 result에 응답 데이터가 있을 것이라 가정합니다
                        val drugresult = Response
                        autoCompleteView.AutoCompleteSuccess(drugresult)
                    }
                }
            }

            override fun onFailure(call: Call<AutoCompleteResponse>, t: Throwable) {
                Log.d("Failed",t.toString())
            }
        })
    }

}