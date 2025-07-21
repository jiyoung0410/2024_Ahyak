package com.example.ahyak.DB

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @DELETE("auth/withdraw")
    fun deleteUser(
//        @Header("Authorization") token: String
    ) : Call<BaseResponse<MessageResponse>>

    //Medication
    @DELETE("/prescription")
    fun deletePrescription(
        @Query("prescription_id") prescriptionId: String
    ) : Call<BaseResponse<MessageResponse>>

    //처방 등록
    @POST("prescription")
    fun registPrescription(
        @Body request: RegistPresRequest
    ) : Call<BaseResponse<RegistPresResponse>>

    //Daily Status - 조회
    @GET("/dailyStatus")
    fun getDailyStatus(
        @Query("date") date: String
    ): Call<BaseResponse<DailyStatusResponse>>

    //추가 약 등록
    @POST("/additionalMeds")
    fun additionMedRegi(
        @Body request: AdditionMedRegiRequest
    ): Call<BaseResponse<AdditionMedDataWrapper<AdditionMedRegiResponse>>>

    @GET("/additionalMeds")
    fun getAddMed(
        @Query("date") date: String
    ): Call<BaseResponse<AdditionMedDataWrapper<ArrayList<GetAddMedResponse>>>>

    //Daily Status - 등록
    @POST("/dailyStatus")
    fun postDailyStatus(
        @Body request: SymptomRequest
    ): Call<DailyStatusResponse>

    //Medicine > 약 정보 조회(모양으로 저장한 약 조회)
    @GET("/medicine")
    fun getMedicines(): Call<BaseResponse<MedicineResponse>>

    //PostMedicine
    @POST("/medicine")
    fun postMedicine(
        @Body request: PostMedicineRequest
    ): Call<PostMedicineResponse>

}