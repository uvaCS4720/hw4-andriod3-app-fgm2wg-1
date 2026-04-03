package edu.nd.pmcburne.hello

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Data access object for Room database
@Dao
interface PlacemarkDao {
    // Function to get all placemarks
    @Query("SELECT * FROM placemarks")
    fun getAllPlacemarks(): Flow<List<Placemark>>

    // Function to insert placemarks
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(placemarks: List<Placemark>)

    // Function to get number of placemarks
    @Query("SELECT COUNT(*) FROM placemarks")
    suspend fun getCount(): Int
}
