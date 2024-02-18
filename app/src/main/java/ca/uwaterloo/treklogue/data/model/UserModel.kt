package ca.uwaterloo.treklogue.data.model

import ca.uwaterloo.treklogue.util.IPresenter

class UserModel(user: User) : IPresenter() {
    var user: User = user
        set(value) {
            field = value
            notifySubscribers()
        }

    // test
    var toggleChecked: Boolean = false
        set(value) {
            field = value
            notifySubscribers()
        }

}