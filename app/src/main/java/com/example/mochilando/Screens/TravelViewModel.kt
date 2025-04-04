package com.example.mochilando.Screens

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

data class Trip(
    val destination: String,
    val travelType: String,
    val startDate: String,
    val endDate: String,
    val budget: String
)

class TravelViewModel : ViewModel() {
    val trips = mutableStateListOf<Trip>()

    fun addTrip(trip: Trip) {
        trips.add(trip)
    }
}
