package ca.uwaterloo.treklogue

import android.app.Application
import android.util.Log
import ca.uwaterloo.treklogue.data.model.Badge
import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.data.model.Landmark
import ca.uwaterloo.treklogue.data.model.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration

lateinit var app: App

// global Kotlin extension that resolves to the short version
// of the name of the current class. Used for labelling logs.
inline fun <reified T> T.TAG(): String = T::class.java.simpleName

/*
*  Sets up the App and enables Realm-specific logging in debug mode.
*/
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Creates a realm with default configuration values
        val config = RealmConfiguration.create(
            schema = setOf(Badge::class, JournalEntry::class, Landmark::class, User::class)
        )

        // Open the realm with the configuration object
        val realm = Realm.open(config)
        Log.v(null, "Successfully opened realm: ${realm.configuration.name}")

        app = App.create(
            AppConfiguration.Builder(getString(R.string.realm_app_id))
                .baseUrl(getString(R.string.realm_base_url))
                .build()
        )
        Log.v(TAG(), "Initialized the App configuration for: ${app.configuration.appId}")
        // If you're getting this app code by cloning the repository at
        // https://github.com/mongodb/template-app-kotlin-todo,
        // it does not contain the data explorer link. Download the
        // app template from the Atlas UI to view a link to your data.
        Log.v(
            TAG(),
            "To see your data in Atlas, follow this link:" + getString(R.string.realm_data_explorer_link)
        )
    }
}
