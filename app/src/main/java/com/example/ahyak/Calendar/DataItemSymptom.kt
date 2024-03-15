package com.example.ahyak.Calendar

import java.io.Serializable

data class DataItemSymptom(
    val sympotmname : String,
    val hospitalname : String,
    val startdate : String,
    val ItemAddPill : ArrayList<DataItemAddPill>
    ){
    data class DataItemAddPill(
        val pillvolume : String,
        val pillname : String,
        var takepill: Boolean = false // 새로운 필드 추가
    )
}
