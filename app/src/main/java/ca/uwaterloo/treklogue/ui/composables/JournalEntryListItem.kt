package ca.uwaterloo.treklogue.ui.composables

import android.net.Uri
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.ui.theme.Blue100
import ca.uwaterloo.treklogue.ui.theme.Gray600
import ca.uwaterloo.treklogue.ui.viewModels.JournalEntryViewModel
import coil.compose.AsyncImage

@Composable
fun JournalEntryListItem(
    modifier: Modifier,
    journalEntry: JournalEntry,
    showJournalDetail: (journalEntry: JournalEntry) -> Unit,
    journalEntryViewModel: JournalEntryViewModel
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title, date and edit button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        journalEntry.name,
                        style = MaterialTheme.typography.titleMedium,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        journalEntry.visitedAt,
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
                        journalEntryViewModel.selectJournalEntry(journalEntry)
                        showJournalDetail(journalEntry)
                    },
                ) {
                    Text(
                        stringResource(R.string.edit),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            // (Optional) Horizontally scrollable list of images
            if (journalEntry.photos.size > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    journalEntry.photos.forEach {
                        AsyncImage(
                            model = Uri.parse(it),
                            contentDescription = null,
                            modifier = Modifier
                                .height(150.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            // (Optional) description
            if (journalEntry.description != "") {
                Text(
                    journalEntry.description,
                    style = MaterialTheme.typography.bodySmall,
//                    maxLines = 3
                )
            }
        }
    }
}
