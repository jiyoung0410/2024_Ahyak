package com.example.ahyak.remote

interface DrugSearchNameView {
    fun DrugSearchNameLoading()
    fun DrugSearchNameSuccess(drug_list : List<RESULT>)
    fun DrugSearchNameFailure()
}