package ca.uwaterloo.treklogue.controller

import ca.uwaterloo.treklogue.data.model.UserModel
import ca.uwaterloo.treklogue.ui.ViewEvent

class UserController(val model: UserModel) {
    // we can cast `Any` later since each event has an associated type
    fun invoke(event: ViewEvent, value: Any?) {
        when(event) {
            ViewEvent.ToggleEvent -> model.toggleChecked = value as Boolean
            ViewEvent.UsernameEvent -> TODO()
            ViewEvent.PasswordEvent -> TODO()
        }
    }
}