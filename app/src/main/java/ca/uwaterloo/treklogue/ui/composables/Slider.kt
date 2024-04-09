package ca.uwaterloo.treklogue.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.ui.theme.Blue100
import ca.uwaterloo.treklogue.ui.theme.Blue200
import ca.uwaterloo.treklogue.ui.theme.Gray100
import ca.uwaterloo.treklogue.ui.theme.Gray600

@Composable
fun RatingSlider(journalEntry: MutableState<JournalEntry>) {

    Column(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)) {
        Slider(
            value = journalEntry.value.rating,
            onValueChange = { journalEntry.value = journalEntry.value.copy(rating = it)},
            colors = SliderDefaults.colors(
                thumbColor = Blue200,
                activeTrackColor = Blue100,
                inactiveTrackColor = Gray100,
            ),
            steps = 1,
            valueRange = 0f..100f
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.rating_1),
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelSmall,
                color = Gray600
            )
            Text(
                text = stringResource(R.string.rating_2),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelSmall,
                color = Gray600
            )
            Text(
                text = stringResource(R.string.rating_3),
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelSmall,
                color = Gray600
            )
        }
    }
}