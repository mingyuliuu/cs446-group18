package ca.uwaterloo.treklogue.ui.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.ui.viewModels.MapViewModel
import android.content.Context


@Composable
fun LandmarkAdd(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    mapViewModel: MapViewModel
) {
    Column(modifier) {
        val landmarkName = remember { mutableStateOf("") }
        // Top bar section
        TopBarLandmark(onBackClicked, mapViewModel, landmarkName, LocalContext.current)

        // Content section
        ContentSectionLandmark(landmarkName)
    }
}

@Composable
fun TopBarLandmark(
    onBackClicked: () -> Unit,
    mapViewModel: MapViewModel,
    landmarkName: MutableState<String>,
    context: Context,
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
        Spacer(modifier = Modifier.width(10.dp))
        Button(onClick = {
            if (landmarkName.value != "") {
                mapViewModel.addUserLandmark(
                    landmarkName.value,
                    mapViewModel.getUserLocation().latitude,
                    mapViewModel.getUserLocation().longitude,
                )
                onBackClicked()
            } else {
                Toast.makeText(context, R.string.landmark_set_name_reminder, Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(stringResource(R.string.save), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun ContentSectionLandmark(landmarkName: MutableState<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp, 12.dp, 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            readOnly = false,
            enabled = true,
            value = landmarkName.value,
            onValueChange = {landmarkName.value = it},
            label = { Text(stringResource(R.string.name_of_landmark)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}