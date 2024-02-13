package org.example.userinterface.components

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.example.userinterface.View

@Composable
fun ScaffoldBottom(
    currentView: View,
    navigateHome: () -> Unit,
    navigateProfile: () -> Unit,
) {
    // red = currentView
    val homeBackground = if (currentView === View.Map) Color.Red else Color.Blue
    val profileBackground = if (currentView === View.Profile) Color.Red else Color.Blue

    BottomAppBar(
        actions = {
            IconButton(
                onClick = navigateHome,
                modifier = Modifier.background(homeBackground)
            ) {
                Icon(Icons.Filled.Check, contentDescription = "Localized description")
            }
            IconButton(
                onClick = navigateProfile,
                modifier = Modifier.background(profileBackground)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Localized description",
                )
            }
        },
    )
}