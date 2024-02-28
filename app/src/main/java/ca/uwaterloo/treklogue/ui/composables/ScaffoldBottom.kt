package ca.uwaterloo.treklogue.ui.composables

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
/*
import ca.uwaterloo.treklogue.ui.Screen

@Composable
fun ScaffoldBottom(
    currentScreen: Screen,
    navigateHome: () -> Unit,
    navigateProfile: () -> Unit,
    navigateList: () -> Unit,
) {
    // red = currentScreen
    // fix this so it isn't so hard-coded
    val homeBackground = if (currentScreen === Screen.Map) Color.Red else Color.Blue
<<<<<<< HEAD
    val profileBackground = if (currentScreen === Screen.Profile) Color.Red else Color.Blue
    val listBackground = if (currentScreen === Screen.List) Color.Red else Color.Blue
=======
    val profileBackground = if (currentScreen === Screen.Profile || currentScreen === Screen.Settings) Color.Red else Color.Blue
>>>>>>> origin/main

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
                    Icons.Filled.AccountCircle,
                    contentDescription = "Localized description",
                )
            }
            IconButton(
                onClick = navigateList,
                modifier = Modifier.background(listBackground)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Localized description",
                )
            }
        },
    )
}

 */