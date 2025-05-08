package com.example.bostacitiesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bostacitiesapp.data.repository.CitiesRepository
import com.example.bostacitiesapp.data.repository.CitiesRepositoryInterface
import com.example.bostacitiesapp.model.ApiResult
import com.example.bostacitiesapp.model.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(citiesRepository: CitiesRepositoryInterface): ViewModel() {
    private val repository = citiesRepository
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
                city.cityName.contains(query,ignoreCase = true) ||
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
            val result = repository.getCitiesAndDistricts()
            _citiesState.value = result

            if(result is ApiResult.Success) {
                _allCities.value = result.data
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setAllCities(cities: List<City>) {
        _allCities.value = cities
    }
}

class MainViewModelFactory(private val repository: CitiesRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}