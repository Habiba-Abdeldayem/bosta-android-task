package com.example.bostacitiesapp.model

data class District(
    val zoneId: String,
    val zoneName: String,
    val zoneOtherName: String,
    val districtId: String,
    val districtName: String,
    val districtOtherName: String,
    val pickupAvailability: Boolean,
    val dropOffAvailability: Boolean,
    val coverage: String
){
    val isUncovered: Boolean
        get() = !pickupAvailability && !dropOffAvailability
}
