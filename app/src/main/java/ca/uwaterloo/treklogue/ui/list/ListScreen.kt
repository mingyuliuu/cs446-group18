package ca.uwaterloo.treklogue.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


// Data model for a landmark
data class Landmark(
    val name: String,
    val dateVisited: String,
    val notes: String,
//    val imageRes: Int // Drawable resource ID
)

/**
 * Composable for the list view that expects [onNextButtonClicked] lambda that expects
 * the selected quantity to save and triggers the navigation to next screen
 */
@Composable
fun ListScreen(
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
//            .background(Color.LightGray)
            .size(100.dp)
            .verticalScroll(rememberScrollState())
    ) {
//        repeat(30) {
//            Text("Item $it", modifier = Modifier.padding(2.dp))
//        }
        Header(onNextButtonClicked)

        // List of landmarks
        val landmarks = listOf(
            Landmark("Eiffel Tower", "10th August 2024", "Some notes about the visit."),
            // Add more landmarks here
        )
        landmarks.forEach { landmark ->
            LandmarkItem(landmark)

        }
    }
}


@Composable
fun LandmarkItem(landmark: Landmark) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
//        elevation = 4.dp
    ) {
        Column {
            Text(landmark.name, style = MaterialTheme.typography.headlineSmall)
            Text(landmark.dateVisited, style = MaterialTheme.typography.bodyMedium)
//            Image(
//                painter = painterResource(id = landmark.imageRes),
//                contentDescription = null, // Provide appropriate content description
//                modifier = Modifier
//                    .height(150.dp)
//                    .fillMaxWidth(),
//                contentScale = ContentScale.Crop
//            )
            Text(landmark.notes, style = MaterialTheme.typography.bodyMedium, maxLines = 3)
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