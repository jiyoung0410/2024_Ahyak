package com.example.ahyak.remote

import com.google.gson.annotations.SerializedName

data class BaseResponse <T>(
    @SerializedName("RESULT") val result : T, //자료형 명시X 아무거나 들어와도 걍 쓰겠다.
)