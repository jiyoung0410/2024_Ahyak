package com.example.ahyak.DB

import android.content.Context
import com.example.ahyak.remote.RetroInterface

class AuthService(private val context: Context) {
    private val authService = ApplicationClass.retrofit.create(RetroInterface::class.java)
}