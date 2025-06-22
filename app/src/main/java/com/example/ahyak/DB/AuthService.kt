package com.example.ahyak.DB

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.ahyak.DB.RetroInterface
import com.example.ahyak.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthService(private val context: Context) {
    private val authService = ApplicationClass.retrofit.create(RetroInterface::class.java)

    private lateinit var loginView: LoginView
    private lateinit var logoutView: LogoutView
    private lateinit var prescriptionView: PrescriptionView
    private lateinit var additionalMediView: AdditionalMediView

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun setLogoutView(logoutView: LogoutView) {
        this.logoutView = logoutView
    }

    fun setPrescriptionView(prescriptionView: PrescriptionView) {
        this.prescriptionView = prescriptionView
    }

    fun setAddtionalMediView(additionalMediView: AdditionalMediView) {
        this.additionalMediView = additionalMediView
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
                        Log.d("Login Test", "Login Test") //25-05-05 Test
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
                        Log.d("accessT, refreshT", "$accessToken, $refreshToken")
                        //saveJwt(resp!!.result.jwt)와 같은 토큰 저장 필요
                        saveTokens(accessToken!!,refreshToken!!)
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

    fun deleteUser() {
        logoutView.deleteUserLoading()
//        val accessToken = getAccessToken()
//        val authHeader = "Bearer $accessToken"
//        Log.d("accessToken 확인", authHeader)
        authService.deleteUser()
            .enqueue(object : Callback<BaseResponse<MessageResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<MessageResponse>>,
                    response: Response<BaseResponse<MessageResponse>>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
                        Log.d("회원 탈퇴 응답", response.body()?.toString() ?: "응답 없음")
                        removeTokens()
                        logoutView.deleteUserSuccess()
                    } else {
                        val errorMsg = response.errorBody()?.string()
                        Log.e("회원 탈퇴 실패", "HTTP ${response.code()}: $errorMsg")
                        Toast.makeText(context, "회원 탈퇴 실패", Toast.LENGTH_SHORT).show()
                        logoutView.deleteUserFailure()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<MessageResponse>>, t: Throwable) {
                    Log.e("회원 탈퇴 실패", "에러: ${t.localizedMessage}")
                    Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    loginView.LoginFailure()
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

    //Daily Status - 조회
    fun getDailyStatus(date: String, callback: DailyStatusCallback) {
        authService.getDailyStatus(date)
            .enqueue(object : Callback<BaseResponse<DailyStatusResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<DailyStatusResponse>>,
                    response: Response<BaseResponse<DailyStatusResponse>>
                ) {
                    if (response.isSuccessful) {
                        val symptomData = response.body()?.data
                        if (symptomData != null) {
                            callback.onDailyStatusSuccess(symptomData)
                        } else {
                            callback.onDailyStatusFailure("데이터가 없습니다.")
                        }
                    } else {
                        callback.onDailyStatusFailure("응답 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<DailyStatusResponse>>, t: Throwable) {
                    callback.onDailyStatusFailure("API 호출 실패: ${t.message}")
                }
            })
    }

    fun additionMedRegi(name: String, dose: String, unit: String, date: String) {
        additionalMediView.AdditionalMediLoading()
        val request = AdditionMedRegiRequest(name,dose,unit,date)
        authService.additionMedRegi(request)
            .enqueue(object : Callback<BaseResponse<AdditionMedDataWrapper>> {
                override fun onResponse(
                    call: Call<BaseResponse<AdditionMedDataWrapper>>,
                    response: Response<BaseResponse<AdditionMedDataWrapper>>
                ) {
                    val resp = response.body()
                    Log.d("Additional Medicine Register response body", resp.toString())
                    //saveJwt(resp!!.result.jwt)
                    if (resp!!.status == "success") {
                        additionalMediView.AdditionalMediSuccess()
                    } else {
                        additionalMediView.AdditionalMediFailure()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<AdditionMedDataWrapper>>, t: Throwable) {
                    Log.d("Additional Medicine Register Failure",t.toString())
                }
            })
    }
}

// DailyStatusCallback.kt
interface DailyStatusCallback {
    fun onDailyStatusSuccess(data: DailyStatusResponse)
    fun onDailyStatusFailure(message: String)
}