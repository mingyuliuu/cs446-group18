package ca.uwaterloo.treklogue.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockJournalEntry

@Composable
fun JournalEntryDetail(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    journalEntryViewModel: JournalEntryViewModel
) {
    val journalEntry = journalEntryViewModel.selectedJournalEntry.observeAsState().value

    if (journalEntry != null) {
        Column(modifier) {
            // Top bar section
            TopBar(onBackClicked)

            // Content section
            ContentSection(journalEntry)
        }
    }
}

@Composable
fun TopBar(onBackClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        IconButton(
            onClick = { onBackClicked() },
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {/* TODO: handle save */ }) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
fun ContentSection(journalEntry: MockJournalEntry) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = TextFieldValue(journalEntry.name),
            onValueChange = { /* TODO: handle text change */ },
            label = { Text(stringResource(R.string.name_of_landmark)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = TextFieldValue(journalEntry.dateVisited),
            onValueChange = { /* TODO: handle text change */ },
            label = { Text(stringResource(R.string.date_of_visit)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = TextFieldValue(journalEntry.notes),
            onValueChange = { /* TODO: handle text change */ },
            label = { Text(stringResource(R.string.personal_note)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RectangleShape)
                .clip(RectangleShape)
                .weight(1f)
        ) {
            Image(
                painter = painterResource(id = journalEntry.imageRes),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Fit
            )
            FloatingActionButton(
                onClick = { /* TODO: handle add image */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
