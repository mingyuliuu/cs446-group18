package ca.uwaterloo.treklogue.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.data.mockModel.MockLandmark


@Composable
fun LandmarkDetail(
    modifier: Modifier = Modifier,
    listViewModel: ListViewModel
) {

    val landmark = listViewModel.selectedLandmark.observeAsState().value

    if (landmark != null) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar section
            TopBar()

            // Content section
            ContentSection(landmark)
        }
    }
}

@Composable
fun TopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        IconButton(
            onClick = { /* TODO: Handle back press */ },
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {/* TODO: handle save */ }) {
            Text("Save")
        }
    }
}

@Composable
fun ContentSection(landmark: MockLandmark) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = TextFieldValue(landmark.name),
            onValueChange = { /* TODO: handle text change */ },
            label = { Text("Name of Landmark") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = TextFieldValue(landmark.dateVisited),
            onValueChange = { /* TODO: handle text change */ },
            label = { Text("Date of Visit") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = TextFieldValue(landmark.notes),
            onValueChange = { /* TODO: handle text change */ },
            label = { Text("Personal Note") },
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
                painter = painterResource(id = landmark.imageRes),
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
    }
}
