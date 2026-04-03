package edu.nd.pmcburne.hello

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Data class for placemark
@Entity(tableName = "placemarks")
data class Placemark(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val tag_list: List<String>,
    val visual_center: VisualCenter
)

// Data class for visual center
data class VisualCenter(
    val latitude: Double,
    val longitude: Double
)

// Converter class for Room database
class Converters {
    // Function to convert list of strings to string
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    // Function to convert string to list of strings
    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    // Function to convert visual center to string
    @TypeConverter
    fun fromVisualCenter(value: VisualCenter): String {
        return Gson().toJson(value)
    }

    // Function to convert string to visual center
    @TypeConverter
    fun toVisualCenter(value: String): VisualCenter {
        return Gson().fromJson(value, VisualCenter::class.java)
    }
}
