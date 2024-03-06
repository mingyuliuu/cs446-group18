package ca.uwaterloo.treklogue.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.ui.theme.Gray600

@Composable
fun SettingsGroup(
    @StringRes name: Int,
    content: List<@Composable (ColumnScope.() -> Unit)>
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp, 4.dp)
        ) {
            Text(
                stringResource(id = name).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = Gray600,
                modifier = Modifier.padding(top = 4.dp)
            )

            content.forEachIndexed { idx, item ->
                item()
                if (idx < content.size - 1) Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}
