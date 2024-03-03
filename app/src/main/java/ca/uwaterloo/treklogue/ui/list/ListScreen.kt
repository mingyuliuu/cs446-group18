package ca.uwaterloo.treklogue.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import ca.uwaterloo.treklogue.data.mockModel.MockLandmark

// Mock: List of landmarks
val landmarks = listOf(
    MockLandmark(
        "Eiffel Tower",
        "10th August 2024",
        "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
        R.drawable.img_eiffel_tower
    ),
    MockLandmark(
        "Eiffel Tower",
        "10th August 2024",
        "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
        R.drawable.img_eiffel_tower
    ),
    MockLandmark(
        "Eiffel Tower",
        "10th August 2024",
        "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
        R.drawable.img_eiffel_tower
    )
)

/**
 * Composable for the list view
 */
@Composable
fun ListScreen(
    onDetailClicked: (landmark: MockLandmark) -> Unit,
    modifier: Modifier = Modifier,
    listViewModel: ListViewModel
) {
    Column(
        modifier = modifier
            .size(100.dp)
            .verticalScroll(rememberScrollState())
    ) {
        landmarks.forEach { landmark ->
//            LandmarkItem(landmark, onDetailClicked)
            LandmarkItem(landmark, listViewModel, onDetailClicked)
        }
    }
}

@Composable
fun LandmarkItem(
    landmark: MockLandmark,
    listViewModel: ListViewModel,
    onDetailClicked: (landmark: MockLandmark) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f) // This makes the Column fill the available space, pushing the Button to the end
                    ) {
                        Text(landmark.name, style = MaterialTheme.typography.headlineSmall)
                        Text(landmark.dateVisited, style = MaterialTheme.typography.bodyMedium)
                    }
                    Button(
                        onClick = {
                            listViewModel.selectLandmark(landmark)
                            onDetailClicked(landmark) },
                        modifier = Modifier.widthIn(min = 80.dp)
                    ) {
                        Text(stringResource(R.string.details))
                    }
                }
                Image(
                    painter = painterResource(id = landmark.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                Text(landmark.notes, style = MaterialTheme.typography.bodyMedium, maxLines = 3)
            }
        }
    }
}
