package ca.uwaterloo.treklogue.ui.map

import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import ca.uwaterloo.treklogue.data.model.Landmark
import ca.uwaterloo.treklogue.data.repository.LandmarkRealmSyncRepository
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Types of UX events triggered by user actions.
 */
sealed class MapEvent {

    class Error(val message: String, val throwable: Throwable) : MapEvent()

}

/**
 * UI representation of a screen state.
 */
data class MapState(
    val landmarks: SnapshotStateList<Landmark>
) {
    companion object {
        val initialState = MapState(landmarks = mutableStateListOf())
    }
}

class MapViewModel(
    private val landmarkRepository: LandmarkRealmSyncRepository
) : ViewModel() {

    private val _state: MutableState<MapState> = mutableStateOf(MapState.initialState)
    val state: State<MapState>
        get() = _state

    private val _event: MutableSharedFlow<MapEvent> = MutableSharedFlow()
    val event: Flow<MapEvent>
        get() = _event

    init {
        viewModelScope.launch {
            landmarkRepository.getLandmarkList().collect { event: ResultsChange<Landmark> ->
                when (event) {
                    is InitialResults -> {
                        _state.value.landmarks.clear()
                        _state.value.landmarks.addAll(event.list)
                    }

                    is UpdatedResults -> {
                        if (event.deletions.isNotEmpty() && _state.value.landmarks.isNotEmpty()) {
                            event.deletions.reversed().forEach {
                                _state.value.landmarks.removeAt(it)
                            }
                        }
                        if (event.insertions.isNotEmpty()) {
                            event.insertions.forEach {
                                _state.value.landmarks.add(it, event.list[it])
                            }
                        }
                        if (event.changes.isNotEmpty()) {
                            event.changes.forEach {
                                _state.value.landmarks.removeAt(it)
                                _state.value.landmarks.add(it, event.list[it])
                            }
                        }
                    }

                    else -> Unit // No-op
                }
            }
        }
    }

    fun error(errorEvent: MapEvent.Error) {
        viewModelScope.launch {
            _event.emit(errorEvent)
        }
    }

    companion object {
        fun factory(
            repository: LandmarkRealmSyncRepository,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null
        ): AbstractSavedStateViewModelFactory {
            return object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return MapViewModel(repository) as T
                }
            }
        }
    }
}
