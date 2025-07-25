package com.example.ahyak.DB

interface PrescriptionView {
    fun PrescriptionLoading()
    fun PrescriptionSuccess(prescriptionId: String)
    fun PrescriptionFailure()
    fun PrescriptionModifyLoading()
    fun PrescriptionModifySuccess(prescriptionId: String, prescriptionName: String)
    fun PrescriptionModifyFailure()
}