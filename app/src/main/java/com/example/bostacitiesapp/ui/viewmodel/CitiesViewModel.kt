package com.example.bostacitiesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bostacitiesapp.data.api.ApiClient
import com.example.bostacitiesapp.data.repository.CitiesRepository
import com.example.bostacitiesapp.model.ApiResult
import com.example.bostacitiesapp.model.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CitiesViewModel: ViewModel() {
    private val repository = CitiesRepository(apiService =  ApiClient.apiService)
    private val _allCities = MutableStateFlow<List<City>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    private val _citiesState = MutableStateFlow<ApiResult<List<City>>>(ApiResult.Loading)
    val citiesState: StateFlow<ApiResult<List<City>>> = _citiesState

    val filteredCities: StateFlow<List<City>> = combine(
        _allCities,
        _searchQuery
    ) { cities, query ->
        if (query.isBlank()) {
            cities
        } else {
            cities.filter { city ->
                city.cityName.contains(query,ignoreCase = true)
                city.districts.any { district->
                    district.districtName.contains(query, ignoreCase = true)
                }

            }
        }.map { city ->
            // filter districts within matching cities
            if(city.cityName.contains(query,true)) {
                city
            } else {
                city.copy(
                    districts = city.districts.filter {
                        it.districtName.contains(query,true)
                    }
                )
            }
        }

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun loadCities() {
        viewModelScope.launch {
            _citiesState.value = ApiResult.Loading
            _citiesState.value = repository.getCities()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setAllCities(cities: List<City>) {
        _allCities.value = cities
    }
}

//private fun setupViewModel() {
//    val repository = CitiesRepository(ApiClient.apiService)
//    viewModel = ViewModelProvider(this, CitiesViewModelFactory(repository))[CitiesViewModel::class.java]
//}