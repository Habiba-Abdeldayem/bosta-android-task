package com.example.bostacitiesapp.data.api

import com.example.bostacitiesapp.model.ApiResponse
import com.example.bostacitiesapp.model.City
import retrofit2.http.GET
import retrofit2.http.Query

interface BostaApiService {
    @GET("cities/getAllDistricts")
    suspend fun getAllDistricts(
        @Query("countryId") countryId:String = "60e4482c7cb7d4bc4849c4d5"
    ) : ApiResponse<List<City>>
}