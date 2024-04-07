package ca.uwaterloo.treklogue.ui.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.repository.AddLandmarkResponse
import ca.uwaterloo.treklogue.data.repository.AddUserLandmarkResponse
import ca.uwaterloo.treklogue.data.repository.DeleteLandmarkResponse
import ca.uwaterloo.treklogue.data.repository.LandmarkRepository
import ca.uwaterloo.treklogue.data.repository.LandmarksResponse
import ca.uwaterloo.treklogue.data.repository.UserLandmarksResponse
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
    var userLandmarksResponse by mutableStateOf<UserLandmarksResponse>(Response.Loading)
        private set
    var addLandmarkResponse by mutableStateOf<AddLandmarkResponse>(Response.Success(false))
        private set
    var addUserLandmarkResponse by mutableStateOf<AddUserLandmarkResponse>(Response.Success(false))
        private set
    var deleteLandmarkResponse by mutableStateOf<DeleteLandmarkResponse>(Response.Success(false))
        private set

    init {
        getLandmarks()
        getUserLandmarks()
    }

    fun getUserLocation(): LatLng {
        return state.value.userLocation
    }
    fun setUserLocation(location: LatLng) {
        Log.v(null, "Setting user location: $location")
        _state.value = state.value.copy(userLocation = location)
    }

    private fun getLandmarks() = viewModelScope.launch {
        landmarkRepository.getLandmarkList().collect { response ->
            landmarksResponse = response
        }
    }
    private fun getUserLandmarks() = viewModelScope.launch {
        landmarkRepository.getUserLandmarkList().collect { response ->
            userLandmarksResponse = response
        }
    }

    fun addLandmark(name: String, latitude: Double, longitude: Double) = viewModelScope.launch {
        addLandmarkResponse = Response.Loading
        addLandmarkResponse = landmarkRepository.addLandmark(name, latitude, longitude)
    }

    fun addUserLandmark(name: String, latitude: Double, longitude: Double) = viewModelScope.launch {
        addUserLandmarkResponse = Response.Loading
        addUserLandmarkResponse = landmarkRepository.addUserLandmark(name, latitude, longitude)
    }

    fun deleteLandmark(id: String) = viewModelScope.launch {
        deleteLandmarkResponse = Response.Loading
        deleteLandmarkResponse = landmarkRepository.deleteLandmark(id)
    }
}
