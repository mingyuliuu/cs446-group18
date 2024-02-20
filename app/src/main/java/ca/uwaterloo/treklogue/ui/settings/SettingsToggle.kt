package ca.uwaterloo.treklogue.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun SettingsToggle(
    @StringRes name: Int,
    state: State<Boolean>,
    onClick: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = name),
            style = MaterialTheme.typography.bodyMedium,
        )
        Switch(
            checked = state.value,
            onCheckedChange = onClick
        )
    }
}