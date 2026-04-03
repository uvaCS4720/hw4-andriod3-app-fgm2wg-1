package edu.nd.pmcburne.hello

import retrofit2.http.GET

// Interface for Retrofit service
interface PlacemarkService {
    // Function to get placemarks from API
    @GET("placemarks.json")
    suspend fun getPlacemarks(): List<Placemark>
}
