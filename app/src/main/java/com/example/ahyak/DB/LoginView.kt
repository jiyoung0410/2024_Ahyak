package com.example.ahyak.DB

interface LoginView {
    fun SignupLoading()
    fun SignupSuccess()
    fun SignupFailure()
    fun LoginLoading()
    fun LoginSuccess()
    fun LoginFailure()
}