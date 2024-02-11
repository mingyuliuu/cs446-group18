package org.example.userinterface

import androidx.compose.runtime.mutableStateOf

class UserViewModel(val model: User) : ISubscriber {
    var firstname = mutableStateOf("")
    var lastname = mutableStateOf("")

    init {
        model.subscribe(this)
    }

    override fun update() {
        firstname.value = model.firstname
        lastname.value = model.lastname
    }
}