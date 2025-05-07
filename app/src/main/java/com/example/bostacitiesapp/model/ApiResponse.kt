package com.example.bostacitiesapp.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T> (
    val success: Boolean,
    val message: String,
    @SerializedName("data") val apiData: T
)