package com.example.bostacitiesapp.model

data class City(
    val cityId:String,
    val cityName:String,
    val cityOtherName:String,
    val cityCode:String,
    val dropOffAvailability: Boolean,
    val pickupAvailability: Boolean,
    val districts: List<District>,
    var isExpanded: Boolean = false
) {
    val isUncovered: Boolean
        get() = districts.isEmpty()
}