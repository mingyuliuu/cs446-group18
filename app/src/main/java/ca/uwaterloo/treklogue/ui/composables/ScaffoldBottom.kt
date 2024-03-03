package ca.uwaterloo.treklogue.ui.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.uwaterloo.treklogue.ui.theme.Blue

@Composable
fun ScaffoldBottom(
    selectedTab: MutableState<ImageVector>,
    navigationController: NavHostController,
) {
    BottomAppBar(
        containerColor = Blue
    ) {
        IconButton(
            onClick = {
                selectedTab.value = Icons.Default.LocationOn
                navigationController.navigate(Screens.Map.screen) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if (selectedTab.value == Icons.Default.LocationOn) Color.White else Color.DarkGray
            )
        }

        IconButton(
            onClick = {
                selectedTab.value = Icons.Default.List
                navigationController.navigate(Screens.List.screen) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.List,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if (selectedTab.value == Icons.Default.List) Color.White else Color.DarkGray
            )
        }

        IconButton(
            onClick = {
                selectedTab.value = Icons.Default.AccountCircle
                navigationController.navigate(Screens.Profile.screen) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if (selectedTab.value == Icons.Default.AccountCircle) Color.White else Color.DarkGray
            )
        }

        IconButton(
            onClick = {
                selectedTab.value = Icons.Default.Settings
                navigationController.navigate(Screens.Settings.screen) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if (selectedTab.value == Icons.Default.Settings) Color.White else Color.DarkGray
            )
        }
    }
}
