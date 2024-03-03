package ca.uwaterloo.treklogue.ui.composables

sealed class Screens(val screen: String) {
    data object Map : Screens("Map")
    data object List : Screens("List")
    data object Profile : Screens("Profile")
    data object Settings : Screens("Settings")
}