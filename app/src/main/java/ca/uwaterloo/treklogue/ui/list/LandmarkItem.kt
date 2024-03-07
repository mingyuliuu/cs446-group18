package ca.uwaterloo.treklogue.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockLandmark
import ca.uwaterloo.treklogue.ui.profile.JournalEntryViewModel
import ca.uwaterloo.treklogue.ui.theme.Blue100
import ca.uwaterloo.treklogue.ui.theme.Gray600

@Composable
fun LandmarkItem(
    modifier: Modifier,
    landmark: MockLandmark,
    journalEntryViewModel: JournalEntryViewModel,
    onAddJournal: (landmark: MockLandmark) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Blue100,
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: ICON that shows whether user has visited this landmark

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(landmark.name, style = MaterialTheme.typography.titleMedium)
                Text( /* TODO: Show distance from current location */
                    "(${landmark.latitude}, ${landmark.longitude})",
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray600
                )
            }
            Button(
                contentPadding = PaddingValues(16.dp, 8.dp),
                modifier = Modifier
                    .defaultMinSize(
                        minWidth = 1.dp,
                        minHeight = 1.dp
                    ), // To override default button sizes by Material3
                onClick = {
                    journalEntryViewModel.createJournalEntry(landmark)
                    onAddJournal(landmark)
                },
            ) {
                Text(
                    stringResource(R.string.add_journal_entry),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
