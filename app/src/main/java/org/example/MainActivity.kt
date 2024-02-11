package org.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.example.theme.CleandesktopuiTheme
import org.example.controller.UserController
import org.example.model.Badge
import org.example.model.JournalEntry
import org.example.model.Landmark
import org.example.model.User
import org.example.model.UserModel
import org.example.userinterface.UserView
import org.example.userinterface.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creates a realm with default configuration values
        val config = RealmConfiguration.create(
            schema = setOf(Badge::class, JournalEntry::class, Landmark::class, User::class)
        )

        // Open the realm with the configuration object
        val realm = Realm.open(config)
        Log.v(null, "Successfully opened realm: ${realm.configuration.name}")

        // From the starter code:
        val userModel = UserModel()
        val userViewModel = UserViewModel(userModel)
        val userController = UserController(userModel)

        setContent {
            CleandesktopuiTheme {
                UserView(userViewModel, userController)
            }
        }
    }
}
