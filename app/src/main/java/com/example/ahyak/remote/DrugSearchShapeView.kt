package com.example.ahyak.remote

interface DrugSearchShapeView {
    fun DrugSearchShapeLoading()
    fun DrugSearchShapeSuccess(drug_list : List<RESULT>)
    fun DrugSearchShapeFailure()
}