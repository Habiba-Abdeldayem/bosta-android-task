package com.example.bostacitiesapp

import com.example.bostacitiesapp.data.repository.CitiesRepositoryInterface
import com.example.bostacitiesapp.model.ApiResult
import com.example.bostacitiesapp.model.City
import com.example.bostacitiesapp.model.District
import com.example.bostacitiesapp.ui.viewmodel.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var repository: CitiesRepositoryInterface
    private lateinit var viewModel: MainViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mockk()
        viewModel = MainViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test successful cities fetch`() = runTest {
        // Arrange
        val fakeCities = listOf(getFakeCity(), getFakeCity(name = "Alexandria"))
        coEvery { repository.getCitiesAndDistricts() } returns ApiResult.Success(fakeCities)

        // Act
        viewModel.loadCities()
        advanceUntilIdle() // wait until coroutine in viewmodel finish

        // Assert
        val result = viewModel.citiesState.value
        assert(result is ApiResult.Success)
        assert((result as ApiResult.Success).data == fakeCities)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}

fun getFakeCity(
    id: String = "1",
    name: String = "Cairo",
    districts: List<District> = emptyList()
): City {
    return City(
        cityId = id,
        cityName = name,
        cityOtherName = "Other $name",
        cityCode = "Code$id",
        dropOffAvailability = true,
        pickupAvailability = true,
        districts = districts
    )
}
