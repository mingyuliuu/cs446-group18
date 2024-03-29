package ca.uwaterloo.treklogue.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.repository.JournalEntries
import ca.uwaterloo.treklogue.ui.composables.JournalEntryListItem
import ca.uwaterloo.treklogue.ui.composables.ProgressBar
import ca.uwaterloo.treklogue.ui.composables.TabSectionHeader
import ca.uwaterloo.treklogue.ui.theme.Gray100
import ca.uwaterloo.treklogue.ui.viewModels.JournalEntryViewModel

/**
 * Composable for the profile view
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    showJournalDetail: (journalEntry: JournalEntry) -> Unit,
    journalEntryViewModel: JournalEntryViewModel
) {
    Column(
        modifier = modifier.background(color = Gray100),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TabSectionHeader(R.string.my_journal_entries)

        Column(
            modifier = modifier
                .padding(12.dp, 0.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            JournalEntries(
                viewModel = journalEntryViewModel,
                journalEntriesContent = { journalEntries ->
                    journalEntries.forEachIndexed { idx, journalEntry ->
                        JournalEntryListItem(
                            Modifier.padding(
                                top = if (idx == 0) 4.dp else 0.dp,
                                bottom = if (idx == journalEntries.size - 1) 12.dp else 0.dp
                            ),
                            journalEntry, showJournalDetail, journalEntryViewModel
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun JournalEntries(
    viewModel: JournalEntryViewModel,
    journalEntriesContent: @Composable (journalEntries: JournalEntries) -> Unit
) {
    when (val journalEntriesResponse = viewModel.journalEntryResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> journalEntriesContent(journalEntriesResponse.data)
        is Response.Failure -> print(journalEntriesResponse.e)
    }
}