package com.example.bostacitiesapp

import android.app.Application
import com.example.bostacitiesapp.data.api.ApiClient
import com.example.bostacitiesapp.data.repository.CitiesRepository
import com.example.bostacitiesapp.ui.viewmodel.MainViewModelFactory

class BostaApp: Application() {
    lateinit var repository: CitiesRepository
    lateinit var factory: MainViewModelFactory

    override fun onCreate() {
        super.onCreate()
        repository = CitiesRepository(ApiClient.apiService)
        factory = MainViewModelFactory(repository)
    }
}