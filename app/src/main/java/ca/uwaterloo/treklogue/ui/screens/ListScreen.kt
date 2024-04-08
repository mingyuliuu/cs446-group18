package ca.uwaterloo.treklogue.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.BuildConfig
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.model.Landmark
import ca.uwaterloo.treklogue.ui.composables.LandmarkListItem
import ca.uwaterloo.treklogue.ui.composables.LoadingPopup
import ca.uwaterloo.treklogue.ui.composables.LoadingWithText
import ca.uwaterloo.treklogue.ui.composables.MIN_LIST_DISTANCE
import ca.uwaterloo.treklogue.ui.composables.TabSectionHeader
import ca.uwaterloo.treklogue.ui.theme.Gray100
import ca.uwaterloo.treklogue.ui.viewModels.JournalEntryViewModel
import ca.uwaterloo.treklogue.ui.viewModels.MapViewModel
import ca.uwaterloo.treklogue.util.distance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    onAddJournal: () -> Unit,
    mapViewModel: MapViewModel,
    journalEntryViewModel: JournalEntryViewModel
) {
    val apiKey = BuildConfig.MAPS_API_KEY

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
            val userLocation = mapViewModel.state.value.userLocation

            // State for landmarks
            // NOTE: We only consider api landmarks on this screen
            var landmarks by remember { mutableStateOf<List<Landmark>>(emptyList()) }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val radius = 5000 // 5km in meters
                    val type = "tourist_attraction"
                    val nearbySearchUrl =
                        "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                                "location=${userLocation.latitude},${userLocation.longitude}&" +
                                "radius=$radius&" +
                                "type=$type&" +
                                "key=$apiKey"
                    val response = URL(nearbySearchUrl).readText()
                    val jsonObject = JSONObject(response)
                    val resultsArray = jsonObject.getJSONArray("results")

                    val landmarksList = mutableListOf<Landmark>()

                    // Iterate over each place in the results array and extract the name
                    for (i in 0 until resultsArray.length()) {
                        val placeObject = resultsArray.getJSONObject(i)
                        val name = placeObject.getString("name")
                        val locationObject =
                            placeObject.getJSONObject("geometry").getJSONObject("location")
                        val lat = locationObject.getDouble("lat")
                        val lng = locationObject.getDouble("lng")
                        val newLandmark = Landmark(i.toString(), name, lat, lng)

                        if (!landmarksList.any { it.name == name }) {
                            landmarksList.add(newLandmark)
                        }
                    }

                    // Update the state of landmarks
                    landmarks = landmarksList
                } catch (e: Exception) {
                    Log.e("ListScreen", "Error fetching nearby places: ${e.message}")
                }
            }

            Log.d("////", landmarks.toString())

            if (landmarks.isEmpty()) {
                LoadingWithText(stringResource(R.string.loading_landmarks))
            } else {
                // Display landmarks using the state value
                Landmarks(
                    viewModel = mapViewModel,
                    landmarksContent = {
                        landmarks.sortedBy {
                            distance(mapViewModel.state.value.userLocation, it)
                        }.forEachIndexed { idx, landmark ->
                            val dist = distance(mapViewModel.state.value.userLocation, landmark)

                            if (dist < MIN_LIST_DISTANCE) {
                                LandmarkListItem(
                                    Modifier.padding(
                                        top = if (idx == 0) 4.dp else 0.dp,
                                        bottom = if (idx == landmarks.size - 1) 12.dp else 0.dp
                                    ),
                                    landmark,
                                    dist,
                                    onAddJournal,
                                    journalEntryViewModel
                                )
                            }
                        }
                    }
                )
            }
        }
        LoadingPopup(mapViewModel = mapViewModel)
    }
}