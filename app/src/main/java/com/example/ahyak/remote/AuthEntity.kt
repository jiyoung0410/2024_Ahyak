package com.example.ahyak.remote

import com.google.gson.annotations.SerializedName

data class DrugSearchNameRequest(
    //키 값 지정
    @SerializedName("QUERY") val query : String
)

data class DrugSearchNameResponse(
    @SerializedName("RESULT") val result : List<RESULT>
)

data class DrugSearchShapeRequest(
    //키 값 지정
    @SerializedName("PRINT") val _print: String,
    @SerializedName("DRUG_SHAPE") val _drug_shape:String,
    @SerializedName("COLOR") val _color:String,
    @SerializedName("TYPE") val _type:String,
    @SerializedName("LINE") val _line:String
)

data class DrugSearchShapeResponse(
    @SerializedName("RESULT") val result : List<RESULT>
)

data class RESULT(
    @SerializedName("ITEM_SEQ") val item_seq: Int,
    @SerializedName("ITEM_NAME") val item_name: String,
    @SerializedName("PRINT") val print: String,
)
