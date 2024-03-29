package ca.uwaterloo.treklogue.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.repository.AddLandmarkResponse
import ca.uwaterloo.treklogue.data.repository.DeleteLandmarkResponse
import ca.uwaterloo.treklogue.data.repository.LandmarkRepository
import ca.uwaterloo.treklogue.data.repository.LandmarksResponse
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI representation of a screen state.
 */
data class MapState(
    val userLocation: LatLng = LatLng(
        43.4822734, -80.5879188
    ) // Waterloo
) {
    companion object {
        val initialState =
            MapState()
    }
}

@HiltViewModel
class MapViewModel @Inject constructor(
    private val landmarkRepository: LandmarkRepository
) : ViewModel() {

    private val _state: MutableState<MapState> = mutableStateOf(MapState.initialState)
    val state: State<MapState>
        get() = _state

    var landmarksResponse by mutableStateOf<LandmarksResponse>(Response.Loading)
        private set
    var addBookResponse by mutableStateOf<AddLandmarkResponse>(Response.Success(false))
        private set
    var deleteBookResponse by mutableStateOf<DeleteLandmarkResponse>(Response.Success(false))
        private set

    init {
        getLandmarks()
    }

    fun setUserLocation(location: LatLng) {
        _state.value = state.value.copy(userLocation = location)
    }

    private fun getLandmarks() = viewModelScope.launch {
        landmarkRepository.getLandmarkList().collect { response ->
            landmarksResponse = response
        }
    }

    fun addLandmark(name: String, latitude: Double, longitude: Double) = viewModelScope.launch {
        addBookResponse = Response.Loading
        addBookResponse = landmarkRepository.addLandmark(name, latitude, longitude)
    }

    fun deleteLandmark(id: String) = viewModelScope.launch {
        deleteBookResponse = Response.Loading
        deleteBookResponse = landmarkRepository.deleteLandmark(id)
    }
}
