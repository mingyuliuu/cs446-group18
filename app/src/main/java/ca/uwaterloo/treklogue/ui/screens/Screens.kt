package ca.uwaterloo.treklogue.ui.screens

sealed class Screens(val screen: String) {
    data object Map : Screens("Map")
    data object List : Screens("List")
    data object Profile : Screens("Profile")
    data object Settings : Screens("Settings")
    data object EditJournal : Screens("EditJournal")
    data object AddJournal : Screens("AddJournal")
    data object AddLandmark : Screens("AddLandmark")
}