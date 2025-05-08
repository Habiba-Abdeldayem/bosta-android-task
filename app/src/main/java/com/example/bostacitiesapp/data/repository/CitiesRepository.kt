package com.example.bostacitiesapp.data.repository

import com.example.bostacitiesapp.data.api.BostaApiService
import com.example.bostacitiesapp.model.ApiResult
import com.example.bostacitiesapp.model.City

class CitiesRepository(private val apiService: BostaApiService): CitiesRepositoryInterface {

    override suspend fun getCitiesAndDistricts(): ApiResult<List<City>> {
        return try {
            val response = apiService.getAllDistricts()
            if(response.success) {
                ApiResult.Success(response.apiData)
            } else {
                ApiResult.Error(ApiException(response.message))
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}
class ApiException(message: String) : Exception(message)
