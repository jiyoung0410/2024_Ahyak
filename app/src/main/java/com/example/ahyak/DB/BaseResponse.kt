package com.example.ahyak.DB

import com.google.gson.annotations.SerializedName

data class BaseResponse <T>(
    @SerializedName("status") val status : String,
    @SerializedName("data") val data : T
)