package ca.uwaterloo.treklogue.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val screen: String) {
    data object Map : Screens("Map")
    data object Lists : Screens("Lists")
    data object Profile : Screens("Profile")
    data object Settings : Screens("Settings")
}