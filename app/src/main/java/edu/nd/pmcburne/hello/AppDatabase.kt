package edu.nd.pmcburne.hello

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Declare class for Room database
@Database(entities = [Placemark::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // Function to get DAO
    abstract fun placemarkDao(): PlacemarkDao

    companion object {
        // Singleton instance of database
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Function to get database
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "campus_maps_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
