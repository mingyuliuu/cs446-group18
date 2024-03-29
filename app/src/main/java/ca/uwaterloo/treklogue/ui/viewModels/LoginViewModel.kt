package ca.uwaterloo.treklogue.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ca.uwaterloo.treklogue.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.exceptions.ConnectionException
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Types of UX events triggered by user actions.
 */
sealed class LoginEvent(val severity: EventSeverity, val message: String) {
    class GoToMap(severity: EventSeverity, message: String) : LoginEvent(severity, message)
    class ShowMessage(severity: EventSeverity, message: String) : LoginEvent(severity, message)

}

/**
 * Severity of the event.
 */
enum class EventSeverity {
    INFO, ERROR
}

/**
 * Users can either create accounts or log in with an existing one.
 */
enum class LoginAction {
    LOGIN, CREATE_ACCOUNT
}

/**
 * UI representation of a screen state.
 */
data class LoginState(
    val action: LoginAction,
    val email: String = "",
    val password: String = "",
    val enabled: Boolean = true
) {
    companion object {
        /**
         * Initial UI state of the login screen.
         */
        val initialState = LoginState(action = LoginAction.LOGIN)
    }
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state: MutableState<LoginState> = mutableStateOf(LoginState.initialState)
    val state: State<LoginState>
        get() = _state

    private val _event: MutableSharedFlow<LoginEvent> = MutableSharedFlow()
    val event: Flow<LoginEvent>
        get() = _event

    fun switchToAction(loginAction: LoginAction) {
        _state.value = state.value.copy(action = loginAction)
    }

    fun setEmail(email: String) {
        _state.value = state.value.copy(email = email)
    }

    fun setPassword(password: String) {
        _state.value = state.value.copy(password = password)
    }

    fun createAccount(email: String, password: String) {
        _state.value = state.value.copy(enabled = false)

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                repository.createAccount(email, password)
            }.onSuccess {
                _event.emit(
                    LoginEvent.ShowMessage(
                        EventSeverity.INFO,
                        "User created successfully."
                    )
                )
                login(email, password)
            }.onFailure { ex: Throwable ->
                _state.value = state.value.copy(enabled = true)
                val message = when (ex) {
                    is UserAlreadyExistsException -> "Failed to register. User already exists."
                    else -> "Failed to register: ${ex.message}"
                }
                _event.emit(LoginEvent.ShowMessage(EventSeverity.ERROR, message))
            }
        }
    }

    fun login(email: String, password: String, fromCreation: Boolean = false) {
        if (!fromCreation) {
            _state.value = state.value.copy(enabled = false)
        }

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                repository.login(email, password)
            }.onSuccess {
                _event.emit(LoginEvent.GoToMap(EventSeverity.INFO, "User logged in successfully."))
            }.onFailure { ex: Throwable ->
                _state.value = state.value.copy(enabled = true)
                val message = when (ex) {
                    is InvalidCredentialsException -> "Invalid username or password. Check your credentials and try again."
                    is ConnectionException -> "Could not connect to the authentication provider. Check your internet connection and try again."
                    else -> "Error: $ex"
                }
                _event.emit(LoginEvent.ShowMessage(EventSeverity.ERROR, message))
            }
        }
    }

    fun logout() {
        repository.logout()
    }

}
