package com.example.ahyak.DB

interface PrescriptionItemView {
    fun DelPrescriptionLoading()
    fun DelPrescriptionSuccess(prescription: PrescriptionEntity)
    fun DelPrescriptionFailure()
}