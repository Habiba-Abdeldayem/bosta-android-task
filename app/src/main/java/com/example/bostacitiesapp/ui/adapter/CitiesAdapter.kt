package com.example.bostacitiesapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bostacitiesapp.databinding.ItemCityBinding
import com.example.bostacitiesapp.model.City

class CitiesAdapter(
    private val onCityClick: (City) -> Unit
) : ListAdapter<City, CitiesAdapter.CityViewHolder>(CityDiffUtil()) {

    inner class CityViewHolder(private val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val districtsAdapter = DistrictsAdapter().apply {
            setHasStableIds(true)
        }

        init {
            binding.rvDistricts.apply {
                setHasFixedSize(true)
                itemAnimator = null
                layoutManager = LinearLayoutManager(context)
                adapter = districtsAdapter
                setItemViewCacheSize(20) // cache more views
                isNestedScrollingEnabled = false // critical for nested RV
            }
        }

        fun bind(city: City) {
            binding.city = city
            districtsAdapter.submitList(city.districts)
            binding.root.setOnClickListener {
                val newState = !city.isExpanded
                onCityClick(city)
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCityBinding.inflate(layoutInflater, parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = getItem(position)
        holder.bind(city)
    }
}

class CityDiffUtil : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City) = oldItem.cityId == newItem.cityId

    override fun areContentsTheSame(oldItem: City, newItem: City) = oldItem == newItem
}