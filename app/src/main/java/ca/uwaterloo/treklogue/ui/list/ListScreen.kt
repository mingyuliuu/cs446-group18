package ca.uwaterloo.treklogue.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockJournalEntry

/**
 * Composable for the list view
 */
@Composable
fun ListScreen(
    onDetailClicked: (landmark: MockJournalEntry) -> Unit,
    modifier: Modifier = Modifier,
    listViewModel: ListViewModel
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        listViewModel.journalEntries.forEach { journalEntry ->
            JournalEntryItem(journalEntry, listViewModel, onDetailClicked)
        }
    }
}

@Composable
fun JournalEntryItem(
    journalEntry: MockJournalEntry,
    listViewModel: ListViewModel,
    onDetailClicked: (landmark: MockJournalEntry) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(journalEntry.name, style = MaterialTheme.typography.headlineSmall)
                        Text(journalEntry.dateVisited, style = MaterialTheme.typography.bodyMedium)
                    }
                    Button(
                        onClick = {
                            listViewModel.selectJournalEntry(journalEntry)
                            onDetailClicked(journalEntry)
                        },
                        modifier = Modifier.widthIn(min = 80.dp)
                    ) {
                        Text(stringResource(R.string.details))
                    }
                }
                Image(
                    painter = painterResource(id = journalEntry.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .height(250.dp)
                        .padding(8.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                Text(journalEntry.notes, style = MaterialTheme.typography.bodyMedium, maxLines = 3)
            }
        }
    }
}
