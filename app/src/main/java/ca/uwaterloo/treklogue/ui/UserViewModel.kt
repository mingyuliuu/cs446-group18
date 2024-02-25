package ca.uwaterloo.treklogue.ui

import androidx.compose.runtime.mutableStateOf
import ca.uwaterloo.treklogue.data.model.UserModel
import ca.uwaterloo.treklogue.util.ISubscriber

// I see there's a VM thing in the android library but I'm not sure if that counts for the course reqs
class UserViewModel(val model: UserModel) : ISubscriber {
    var name = mutableStateOf("")
    var toggleChecked = mutableStateOf(false)
    init {
        model.subscribe(this)
    }

    // TODO: event parameter
    override fun update() {
        // TODO: fix with real data
//        name.value = model.user.name
        toggleChecked.value = model.toggleChecked
    }
}