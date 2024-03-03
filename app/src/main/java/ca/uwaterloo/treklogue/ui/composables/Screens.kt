package ca.uwaterloo.treklogue.ui.composables

sealed class Screens(val screen: String) {
    data object Map : Screens("Map")
    data object List : Screens("List")
    data object JournalDetail : Screens("JournalDetail")
    data object Profile : Screens("Profile")
    data object Settings : Screens("Settings")
    data object JournalEntry : Screens("JournalEntry")
}