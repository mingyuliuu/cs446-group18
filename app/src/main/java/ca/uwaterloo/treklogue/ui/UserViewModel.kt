package ca.uwaterloo.treklogue.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Types of UX events triggered by user actions.
 */
sealed class UserEvent {
    object LogOut : UserEvent()
    class Info(val message: String) : UserEvent()
    class Error(val message: String, val throwable: Throwable) : UserEvent()

}

/**
 * UI representation of a screen state.
 */
data class UserState(
    val notificationEnabled: Boolean
) {
    companion object {
        val initialState = UserState(notificationEnabled = false)
    }
}

class UserViewModel : ViewModel() {

    private val _state: MutableState<UserState> = mutableStateOf(UserState.initialState)
    val state: State<UserState>
        get() = _state

    private val _event: MutableSharedFlow<UserEvent> = MutableSharedFlow()
    val event: Flow<UserEvent>
        get() = _event

    fun toggleNotificationSetting(enabled: Boolean) {
        _state.value = state.value.copy(notificationEnabled = enabled)

        viewModelScope.launch {
            _event.emit(UserEvent.Info("Notification settings updated"))
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _event.emit(UserEvent.LogOut)
        }
    }

    fun error(errorEvent: UserEvent.Error) {
        viewModelScope.launch {
            _event.emit(errorEvent)
        }
    }
}
