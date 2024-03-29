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
import androidx.hilt.navigation.compose.hiltViewModel
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockLandmark
import ca.uwaterloo.treklogue.ui.composables.LandmarkListItem
import ca.uwaterloo.treklogue.ui.composables.TabSectionHeader
import ca.uwaterloo.treklogue.ui.theme.Gray100
import ca.uwaterloo.treklogue.ui.viewModels.MapViewModel
import ca.uwaterloo.treklogue.util.distance

/**
 * Composable for the list view
 */
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    onAddJournal: (landmark: MockLandmark) -> Unit,
    mapViewModel: MapViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier.background(color = Gray100),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TabSectionHeader(R.string.nearby_landmarks)

        Column(
            modifier = modifier
                .padding(12.dp, 0.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Landmarks(
                landmarksContent = { landmarks ->
                    landmarks.sortedBy {
                        distance(mapViewModel.state.value.userLocation, it)
                    }.forEachIndexed { idx, landmark ->
                        val dist = distance(mapViewModel.state.value.userLocation, landmark)

                        LandmarkListItem(
                            Modifier.padding(
                                top = if (idx == 0) 4.dp else 0.dp,
                                bottom = if (idx == landmarks.size - 1) 12.dp else 0.dp
                            ),
                            landmark,
                            dist,
                            onAddJournal
                        )
                    }
                }
            )
        }
    }
}