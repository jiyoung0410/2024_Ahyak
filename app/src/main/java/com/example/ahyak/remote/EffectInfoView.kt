package com.example.ahyak.remote

interface EffectInfoView {
    fun EffectInfoLoading()
    fun EffectInfoSuccess(effectresult: List<EffectInfoResponseResult>)
    fun EffectInfoFailure()
}