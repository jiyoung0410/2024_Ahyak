package com.example.ahyak.DB

interface PrescriptionView {
    fun PrescriptionLoading()
    fun PrescriptionSuccess(prescriptionId: String)
    fun PrescriptionFailure()
}