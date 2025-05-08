package com.example.bostacitiesapp.data.repository

import com.example.bostacitiesapp.model.ApiResult
import com.example.bostacitiesapp.model.City

interface CitiesRepositoryInterface {
    suspend fun getCitiesAndDistricts(): ApiResult<List<City>>
}