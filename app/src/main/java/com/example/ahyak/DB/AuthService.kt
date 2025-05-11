package com.example.ahyak.DB

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.ahyak.DB.RetroInterface
import com.example.ahyak.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthService(private val context: Context) {
    private val authService = ApplicationClass.retrofit.create(RetroInterface::class.java)

    private lateinit var loginView: LoginView
    private lateinit var prescriptionView: PrescriptionView

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun setPrescriptionView(prescriptionView: PrescriptionView) {
        this.prescriptionView = prescriptionView
    }

    fun signup(nickname: String, email: String) {
        loginView.SignupLoading()
        val request = SignupRequest(nickname,email)
        authService.signup(request)
            .enqueue(object : Callback<BaseResponse<MessageResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<MessageResponse>>,
                    response: Response<BaseResponse<MessageResponse>>
                ) {
                    val resp = response.body()
                    Log.d("Signup response body", resp.toString())
                    //saveJwt(resp!!.result.jwt)
                    if (resp!!.status == "success") {
                        loginView.SignupSuccess()
                        login(nickname,email)
                    } else {
                        loginView.SignupFailure()
                    }

//                    val token = getJwt()
//                    if (token == null) {
//                        Log.d("Signup response token", "getJwt 빔")
//                    } else {
//                        Log.d("Signup response token", token)
//                    }
                }

                override fun onFailure(call: Call<BaseResponse<MessageResponse>>, t: Throwable) {
                    Log.d("Signup Failure",t.toString())
                }
            })
    }

    fun login(nickname: String, email: String) {
        loginView.LoginLoading()
        val request = SignupRequest(nickname,email)
        authService.login(request)
            .enqueue(object : Callback<BaseResponse<LoginResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<LoginResponse>>,
                    response: Response<BaseResponse<LoginResponse>>
                ) {
                    val resp = response.body()
                    Log.d("Login response body", resp.toString())
                    if (response.isSuccessful) {
                        Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                        Log.d("로그인 정보",resp.toString())
                        val accessToken = resp?.data?.accessToken
                        val refreshToken = resp?.data?.refreshToken
                        //saveJwt(resp!!.result.jwt)와 같은 토큰 저장 필요
                        saveTokens(accessToken!!,refreshToken!!)
                        Log.d("accessToken", getAccessToken().toString())
                        loginView.LoginSuccess()
                    } else {
                        //서버에서 응답을 했으나, 성공적인 응답이 아닌 경우
                        val gson = Gson()
                        val type = object : TypeToken<BaseResponse<LoginResponse>>() {}.type
                        val errorResponse: BaseResponse<LoginResponse>? =
                            gson.fromJson(response.errorBody()?.charStream(), type)
                        Log.d("로그인 실패 정보", errorResponse.toString())

                        if (errorResponse?.status != "success") {
                            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "success를 반환했으나 로그인이 안 됨", Toast.LENGTH_SHORT).show()
                        }
                        loginView.LoginFailure()
                        //Log.d("로그인 실패 코드", errorResponse?.code.toString())
                    }

//                    val token = getJwt()
//                    if (token == null) {
//                        Log.d("Signup response token", "getJwt 빔")
//                    } else {
//                        Log.d("Signup response token", token)
//                    }
                }

                override fun onFailure(call: Call<BaseResponse<LoginResponse>>, t: Throwable) {
                    Log.d("Login Failure",t.toString())
                }
            })
    }

    fun registPrescription(name: String, hospital: String, startDate: String, endDate: String) {
        prescriptionView.PrescriptionLoading()
        val request = RegistPresRequest(name,hospital,startDate,endDate)
        authService.registPrescription(request)
            .enqueue(object : Callback<BaseResponse<RegistPresResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<RegistPresResponse>>,
                    response: Response<BaseResponse<RegistPresResponse>>
                ) {
                    val resp = response.body()
                    Log.d("Prescription Register response body", resp.toString())
                    //saveJwt(resp!!.result.jwt)
                    if (resp!!.status == "success") {
                        prescriptionView.PrescriptionSuccess()
                    } else {
                        prescriptionView.PrescriptionFailure()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<RegistPresResponse>>, t: Throwable) {
                    Log.d("Prescription Register Failure",t.toString())
                }
            })
    }
}