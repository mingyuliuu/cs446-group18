package ca.uwaterloo.treklogue.ui.composables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.ui.theme.Blue100
import ca.uwaterloo.treklogue.ui.theme.Blue200
import ca.uwaterloo.treklogue.ui.viewModels.JournalEntryViewModel
import coil.compose.AsyncImage
import io.realm.kotlin.ext.copyFromRealm

@Composable
fun JournalEntryDetail(
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
    onBackClicked: () -> Unit,
    journalEntryViewModel: JournalEntryViewModel
) {
    val journalEntry = journalEntryViewModel.selectedJournalEntry.observeAsState().value
    val editedJournalEntry = remember { mutableStateOf(journalEntry!!) }

    if (journalEntry != null) {
        Column(modifier) {
            // Top bar section
            TopBar(onBackClicked, editedJournalEntry, journalEntryViewModel)

            // Content section
            ContentSection(isEditing, editedJournalEntry)
        }
    }
}

@Composable
fun TopBar(
    onBackClicked: () -> Unit,
    editedJournalEntry: MutableState<JournalEntry>,
    journalEntryViewModel: JournalEntryViewModel
) {
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
        Button(onClick = {
            // TODO: Logic for updating existing journal entry
            journalEntryViewModel.saveJournalEntry(
                editedJournalEntry.value.landmark!!,
                editedJournalEntry.value.visitedAt,
                editedJournalEntry.value.photos,
                editedJournalEntry.value.description
            )
        }) {
            Text(stringResource(R.string.save), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContentSection(isEditing: Boolean, editedJournalEntry: MutableState<JournalEntry>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp, 12.dp, 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            readOnly = isEditing,
            enabled = !isEditing,
            value = TextFieldValue(editedJournalEntry.value.landmark?.name ?: "Landmark"),
            onValueChange = {},
            label = { Text(stringResource(R.string.name_of_landmark)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            readOnly = isEditing,
            enabled = !isEditing,
            value = TextFieldValue(editedJournalEntry.value.visitedAt),
            onValueChange = {},
            label = { Text(stringResource(R.string.date_of_visit)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            readOnly = false,
            enabled = true,
            value = editedJournalEntry.value.description,
            onValueChange = {
                editedJournalEntry.value.description = it
            },
            label = { Text(stringResource(R.string.personal_note)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
        )

//        var selectedImageUri by remember {
//            mutableStateOf<List<Uri?>>(emptyList())
//        }
//
//        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.PickVisualMedia(),
//            onResult = { uri -> selectedImageUri = selectedImageUri + uri }
//        )

        var selectedImageUri by remember {
            mutableStateOf<List<Uri?>>(emptyList())
        }

        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                selectedImageUri = selectedImageUri + uri
                editedJournalEntry.value.photos.add(uri.toString())
            }
        )

        FormSectionHeader(text = R.string.personal_photos)
        Box(
            modifier = Modifier
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                editedJournalEntry.value.photos.forEach { str ->
                    val uri: Uri = Uri.parse(str)
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .height(150.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            FloatingActionButton(
                onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                modifier = Modifier
                    .height(75.dp)
                    .width(75.dp)
                    .align(Alignment.CenterStart),
                containerColor = Blue100,
                contentColor = Blue200
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }

    FormSectionHeader(text = R.string.personal_rating)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
    ) {
        RatingSlider()
    }
}
