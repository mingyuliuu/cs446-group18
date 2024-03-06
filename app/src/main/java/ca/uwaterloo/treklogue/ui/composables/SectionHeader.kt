package ca.uwaterloo.treklogue.ui.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SectionHeader(
    @StringRes text: Int,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(0.dp, 12.dp),
            textAlign = TextAlign.Center
        )
    }
}
