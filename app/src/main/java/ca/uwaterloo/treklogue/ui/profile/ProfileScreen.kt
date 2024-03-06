package ca.uwaterloo.treklogue.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockJournalEntry
import ca.uwaterloo.treklogue.ui.UserViewModel
import ca.uwaterloo.treklogue.ui.composables.SectionHeader
import ca.uwaterloo.treklogue.ui.theme.Gray100

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
        modifier = modifier.background(color = Gray100),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionHeader(R.string.my_badges)

        // TODO: Badges (horizontally scrollable list?)
        Spacer(modifier = Modifier.height(30.dp))

        SectionHeader(R.string.my_journal_entries)

        Column(
            modifier = modifier
                .padding(12.dp, 0.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            journalEntryViewModel.journalEntries.forEachIndexed { idx, journalEntry ->
                JournalEntryItem(
                    Modifier.padding(
                        top = if (idx == 0) 4.dp else 0.dp,
                        bottom = if (idx == journalEntryViewModel.journalEntries.size - 1) 12.dp else 0.dp
                    ),
                    journalEntry, journalEntryViewModel, showJournalDetail
                )
            }
        }
    }
}