package ca.uwaterloo.treklogue.ui.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Composable for the settings screen
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    var setting1 by remember { mutableStateOf(true) }

    Column(
        modifier = modifier.padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SETTINGS")
        Divider(
            thickness = 2.dp,
            modifier = Modifier.padding(bottom = 50.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().border(2.dp, Color.Gray).padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("setting 1")
            Switch(checked = setting1, onCheckedChange = {setting1 = it})
        }
    }
}