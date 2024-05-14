package com.example.ahyak.remote

interface AutoCompleteView {
    fun AutoCompleteLoading()
    fun AutoCompleteSuccess(drug_list : List<String>)
    fun AutoCompleteFailure()
}