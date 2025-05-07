package com.example.bostacitiesapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bostacitiesapp.databinding.ItemDistrictBinding
import com.example.bostacitiesapp.model.District

class DistrictsAdapter() :
    ListAdapter<District, DistrictsAdapter.DistrictViewHolder>(DistrictDiffUtil()) {
    inner class DistrictViewHolder(private val binding: ItemDistrictBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(district: District) {
            binding.district = district

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDistrictBinding.inflate(layoutInflater, parent, false)
        return DistrictViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
        val district = getItem(position)
        holder.bind(district)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).districtId.hashCode().toLong() // Stable IDs
    }
}


class DistrictDiffUtil() : DiffUtil.ItemCallback<District>() {
    override fun areItemsTheSame(oldItem: District, newItem: District) =
        oldItem.districtId == newItem.districtId

    override fun areContentsTheSame(oldItem: District, newItem: District) = oldItem == newItem

}