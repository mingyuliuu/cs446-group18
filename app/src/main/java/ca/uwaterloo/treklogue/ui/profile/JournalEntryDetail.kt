package ca.uwaterloo.treklogue.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockJournalEntry
import ca.uwaterloo.treklogue.ui.theme.Blue100
import ca.uwaterloo.treklogue.ui.theme.Blue200

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
            Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.save))
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {/* TODO: handle save */ }) {
            Text(stringResource(R.string.save), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContentSection(journalEntry: MockJournalEntry) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp, 12.dp, 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            readOnly = true,
            enabled = false,
            value = TextFieldValue(journalEntry.name),
            onValueChange = {},
            label = { Text(stringResource(R.string.name_of_landmark)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            readOnly = true,
            enabled = false,
            value = TextFieldValue(journalEntry.dateVisited),
            onValueChange = {},
            label = { Text(stringResource(R.string.date_of_visit)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = TextFieldValue(journalEntry.description),
            onValueChange = { /* TODO: handle text change */ },
            label = { Text(stringResource(R.string.personal_note)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            journalEntry.images.forEach {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .border(1.dp, Color.Gray, RectangleShape),
                    contentScale = ContentScale.Fit,
                )
            }

            FloatingActionButton(
                onClick = { /* TODO: handle add image */ },
                modifier = Modifier
                    .height(75.dp)
                    .width(75.dp)
                    .align(Alignment.CenterVertically),
                containerColor = Blue100,
                contentColor = Blue200
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }
}
