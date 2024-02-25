package ca.uwaterloo.treklogue.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Types of UX events triggered by user actions.
 */
sealed class UserEvent(val severity: EventSeverity, val message: String) {
    class ToggleNotificationSetting(severity: EventSeverity, message: String) :
        UserEvent(severity, message)

}

/**
 * Severity of the event.
 */
enum class EventSeverity {
    INFO, ERROR
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

    fun toggleNotificationSetting(enabled: Boolean) {
        _state.value = state.value.copy(notificationEnabled = enabled)

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                // TODO: update notification behavior
            }.onSuccess {
                _event.emit(
                    UserEvent.ToggleNotificationSetting(
                        EventSeverity.INFO,
                        "Notification settings updated"
                    )
                )
            }.onFailure {
                _state.value = state.value.copy(notificationEnabled = !enabled)
                _event.emit(
                    UserEvent.ToggleNotificationSetting(
                        EventSeverity.ERROR,
                        "Failed to update notification settings"
                    )
                )
            }
        }
    }
}
