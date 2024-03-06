package ca.uwaterloo.treklogue.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.data.mockModel.MockJournalEntry
import ca.uwaterloo.treklogue.ui.UserViewModel

/**
 * Composable for the profile view
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    journalEntryViewModel: JournalEntryViewModel,
    showJournalDetail: (journalEntry: MockJournalEntry) -> Unit,
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(6),
        ) {
            // TODO: Journal entries + Badges
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
            ) {
                journalEntryViewModel.journalEntries.forEach { journalEntry ->
                    JournalEntryItem(journalEntry, journalEntryViewModel, showJournalDetail)
                }
            }
        }
    }
}