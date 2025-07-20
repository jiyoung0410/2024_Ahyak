package com.example.ahyak.DB

interface HomeStatusView {
    fun AddMedDataLoading()
    fun AddMedDataSuccess(dataResponse: ArrayList<GetAddMedResponse>)
    fun AddMedDataFailure()
}