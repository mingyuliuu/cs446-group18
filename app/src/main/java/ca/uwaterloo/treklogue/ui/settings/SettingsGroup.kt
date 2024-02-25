package ca.uwaterloo.treklogue.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingsGroup(
    @StringRes name: Int,
    content: List<@Composable (ColumnScope.() -> Unit)>
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            stringResource(id = name),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                content.forEach() {
                    it()
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}