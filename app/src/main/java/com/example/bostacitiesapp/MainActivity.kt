package com.example.bostacitiesapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bostacitiesapp.databinding.ActivityMainBinding
import com.example.bostacitiesapp.model.ApiResult
import com.example.bostacitiesapp.ui.adapter.CitiesAdapter
import com.example.bostacitiesapp.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CitiesAdapter
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val app = application as BostaApp
        viewModel = ViewModelProvider(this, app.factory)[MainViewModel::class.java]

        setupRecyclerView()
        setupSearch()
        observeViewModel()
        viewModel.loadCities()
    }

    private fun setupSearch() {
        binding.searchInput.doAfterTextChanged { editable ->
            viewModel.setSearchQuery(editable.toString())
        }
    }

    private fun setupRecyclerView() {
        adapter = CitiesAdapter { city ->
            val newList = adapter.currentList.map {
                if (it.cityId == city.cityId) it.copy(isExpanded = !it.isExpanded) else it
            }
            adapter.submitList(newList)
        }

        binding.rvCities.layoutManager = LinearLayoutManager(this)
        binding.rvCities.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.citiesState.collect { result ->
                        when (result) {
                            is ApiResult.Success -> {
                                binding.progressBar.visibility = View.GONE
//                            adapter.submitList(result.data)
                                viewModel.setAllCities(result.data)
                            }

                            is ApiResult.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error: ${result.exception.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            ApiResult.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                launch {
                    viewModel.filteredCities.collect { filteredList ->
                        adapter.submitList(filteredList)
                    }
                }
            }
        }
    }
}
