package edu.nd.pmcburne.hello

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Data class for UI state
data class MainUIState(
    val placemarks: List<Placemark> = emptyList(),
    val allTags: List<String> = emptyList(),
    val selectedTag: String = "core",
    val isLoading: Boolean = false
)

// ViewModel for main screen to maintain on rotations
class MainViewModel(application: Application) : AndroidViewModel(application) {
    // Private variables for database, service, and UI state
    private val database = AppDatabase.getDatabase(application)
    private val placemarkDao = database.placemarkDao()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.cs.virginia.edu/~wxt4gm/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(PlacemarkService::class.java)

    private val _selectedTag = MutableStateFlow("core")
    private val _isLoading = MutableStateFlow(false)

    // StateFlow for UI state
    val uiState: StateFlow<MainUIState> = combine(
        placemarkDao.getAllPlacemarks(),
        _selectedTag,
        _isLoading
    ) { placemarks, selectedTag, isLoading ->
        val allTags = placemarks.flatMap { it.tag_list }.distinct().sorted()
        val filteredPlacemarks = placemarks.filter { it.tag_list.contains(selectedTag) }
        MainUIState(
            placemarks = filteredPlacemarks,
            allTags = allTags,
            selectedTag = selectedTag,
            isLoading = isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUIState()
    )

    // Initialize view model
    init {
        refreshData()
    }

    // Function to refresh data
    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val remotePlacemarks = service.getPlacemarks()
                placemarkDao.insertAll(remotePlacemarks)
            } catch (e: Exception) {} finally {
                _isLoading.value = false
            }
        }
    }

    // Function to select tag
    fun selectTag(tag: String) {
        _selectedTag.value = tag
    }
}
