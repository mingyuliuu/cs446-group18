package ca.uwaterloo.treklogue.data.model

import ca.uwaterloo.treklogue.util.IPresenter

// TODO: DELETE THIS - This is from clean ui starter code.
//  I left it here as a reference for creating models.
class UserModel : IPresenter() {
    var firstname: String = ""
        set(value) {
            field = value
            notifySubscribers()
        }

    var lastname: String = ""
        set(value) {
            field = value
            notifySubscribers()
        }
}