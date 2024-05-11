package com.example.ahyak.remote

import com.google.gson.annotations.SerializedName

data class DrugSearchNameRequest(
    //키 값 지정
    @SerializedName("QUERY") val query : String
)

data class DrugSearchNameResponse(
    @SerializedName("RESULT") val result : List<RESULT>
)

data class AutoCompleteResponse(
    @SerializedName("RESULT") val result : List<String>
)

data class RESULT(
    @SerializedName("ITEM_SEQ") val item_seq: Int,
    @SerializedName("ITEM_NAME") val item_name: String,
    @SerializedName("PRINT") val print: String,
)
