package ca.uwaterloo.treklogue.ui.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SettingsActionButton(
    @StringRes text: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = stringResource(id = text),
            modifier = Modifier
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(12.dp)
                .weight(1.0F),
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
        )
    }
}