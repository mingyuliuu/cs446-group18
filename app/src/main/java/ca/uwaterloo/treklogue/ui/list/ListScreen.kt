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
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R


// Data model for a landmark
data class Landmark(
    val name: String,
    val dateVisited: String,
    val notes: String,
    val imageRes: Int // Drawable resource ID
)

// List of landmarks
val landmarks = listOf(
    Landmark(
        "Eiffel Tower",
        "10th August 2024",
        "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
        R.drawable.eiffel_tower
    ),
    Landmark(
        "Eiffel Tower",
        "10th August 2024",
        "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
        R.drawable.eiffel_tower
    ),
    Landmark(
        "Eiffel Tower",
        "10th August 2024",
        "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
        R.drawable.eiffel_tower
    )
)

/**
 * Composable for the list view that expects [onNextButtonClicked] lambda that expects
 * the selected quantity to save and triggers the navigation to next screen
 */
@Composable
fun ListScreen(
    onNextButtonClicked: (Int) -> Unit,
    onDetailClicked: (landmark: Landmark) -> Unit,
//    viewModel: ListViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .size(100.dp)
            .verticalScroll(rememberScrollState())
    ) {
//        Header(onNextButtonClicked)

        landmarks.forEach { landmark ->
            LandmarkItem(landmark, onDetailClicked)
//            LandmarkItem(landmark, viewModel, onDetailClicked)
        }
    }
}


@Composable
fun LandmarkItem(
    landmark: Landmark,
//    viewModel: ListViewModel,
    onDetailClicked: (landmark: Landmark) -> Unit
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
                        onClick = { onDetailClicked(landmark) },
//                        onClick = { viewModel.selectLandmark(landmark) },
                        modifier = Modifier.widthIn(min = 80.dp)
                    ) {
                        Text("Details")
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

@Composable
fun Header(onNextButtonClicked: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("You are on the list screen")
        Button(
            onClick = { onNextButtonClicked(0) },
            modifier = Modifier.widthIn(min = 250.dp)
        ) {
            Text("Go to profile")
        }
    }
}