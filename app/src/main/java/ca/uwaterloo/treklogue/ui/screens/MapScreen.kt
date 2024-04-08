package ca.uwaterloo.treklogue.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.model.Landmark
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.repository.JournalEntries
import ca.uwaterloo.treklogue.data.repository.Landmarks
import ca.uwaterloo.treklogue.ui.composables.LoadingPopup
import ca.uwaterloo.treklogue.ui.composables.MIN_JOURNAL_DISTANCE
import ca.uwaterloo.treklogue.ui.composables.MapMarker
import ca.uwaterloo.treklogue.ui.composables.ProgressBar
import ca.uwaterloo.treklogue.ui.theme.Gray100
import ca.uwaterloo.treklogue.ui.viewModels.JournalEntryViewModel
import ca.uwaterloo.treklogue.ui.viewModels.MapViewModel
import ca.uwaterloo.treklogue.ui.viewModels.UserViewModel
import ca.uwaterloo.treklogue.util.NotificationHelper
import ca.uwaterloo.treklogue.util.distance
import ca.uwaterloo.treklogue.util.getCurrentLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

/**
 * Composable for the map view
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel,
    journalModel: JournalEntryViewModel,
    userViewModel: UserViewModel,
    onAddJournal: () -> Unit,
    onAddLandmark: () -> Unit,
) {
    val defaultCameraPosition =
        CameraPosition.fromLatLngZoom(mapViewModel.state.value.userLocation, 15f)
    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    val context = LocalContext.current

    val notificationHelper = NotificationHelper(context = context, userViewModel = userViewModel)
    notificationHelper.setUpNotificationChannels()

    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    val locationPermissionState = rememberPermissionState(locationPermission)

    val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
    val coarseLocationPermissionState = rememberPermissionState(coarseLocationPermission)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // there is a bug where isGranted is false when only coarse access is given, so location is not updated
        // getting rid of 'if' seems fine though, app will warn but no crash and behaviour seems okay
//            if (isGranted) {
        // Permission granted, update the location
        getCurrentLocation(context) { lat, long, updateCam ->
            mapViewModel.setUserLocation(LatLng(lat, long))

            if (updateCam) {
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(
                        LatLng(lat, long)
                    )
                )
                Log.v(null, "Updating cam...")
            }
            Log.v(null, "Updating location...")
        }
//            }
    }

    LaunchedEffect(Unit) {
        // Move this to a separate thread to prevent it from blocking the Main thread
        withContext(Dispatchers.IO) {
            if (locationPermissionState.status.isGranted || coarseLocationPermissionState.status.isGranted) {
                // Permission already granted, update the location
                getCurrentLocation(context) { lat, long, updateCam ->
                    mapViewModel.setUserLocation(LatLng(lat, long))

                    if (updateCam) {
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLng(
                                LatLng(lat, long)
                            )
                        )
                        Log.v(null, "Updating cam...")
                    }
                    Log.v(null, "Updating location...")
                }
                Log.v(null, "Permission already granted; current location fetched.")
            } else {
                // Request location permission
                requestPermissionLauncher.launch(locationPermission)
                Log.v(null, "Permission requested.")
            }
        }
    }

    Box(modifier) {
        GoogleMapView(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            mapViewModel = mapViewModel,
            journalModel = journalModel,
            onAddJournal = onAddJournal,
            onAddLandmark = onAddLandmark,
            notificationHelper = notificationHelper,
        )

        // allowing user to use the app with default location is probably not a good idea
        LoadingPopup(
            mapViewModel
        )
    }
}

@Composable
fun Landmarks(
    viewModel: MapViewModel,
    landmarksContent: @Composable (landmarks: Landmarks) -> Unit
) {
    when (val landmarksResponse = viewModel.landmarksResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> landmarksContent(landmarksResponse.data)
        is Response.Failure -> print(landmarksResponse.e)
    }
}

@Composable
fun LandmarksJournals(
    viewModel: MapViewModel,
    journalModel: JournalEntryViewModel,
    content: @Composable (landmarks: Landmarks, journalEntries: JournalEntries) -> Unit
) {
    val landmarksResponse = viewModel.landmarksResponse
    var landmarksLoading = true
    var landmarksContent: Landmarks? = null

    val userLandmarksResponse = viewModel.userLandmarksResponse
    var userLandmarksLoading = true
    var userLandmarksContent: Landmarks? = null

    val journalEntriesResponse = journalModel.journalEntryResponse
    var journalsLoading = true
    var journalsContent: JournalEntries? = null

    when (landmarksResponse) {
        is Response.Loading -> landmarksLoading = true
        is Response.Success -> {
            landmarksContent = landmarksResponse.data
            landmarksLoading = false
        }

        is Response.Failure -> print(landmarksResponse.e)
    }

    when (userLandmarksResponse) {
        is Response.Loading -> userLandmarksLoading = true
        is Response.Success -> {
            userLandmarksContent = userLandmarksResponse.data
            userLandmarksLoading = false
        }

        is Response.Failure -> print(userLandmarksResponse.e)
    }

    when (journalEntriesResponse) {
        is Response.Loading -> journalsLoading = true
        is Response.Success -> {
            journalsContent = journalEntriesResponse.data
            journalsLoading = false
        }

        is Response.Failure -> print(journalEntriesResponse.e)
    }

    if (landmarksLoading && journalsLoading && userLandmarksLoading) {
        ProgressBar()
    } else if (userLandmarksContent != null) {
//        landmarksContent += userLandmarksContent!!
        // only considering user landmarks and google api landmarks
        content(userLandmarksContent, journalsContent!!)
    }
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    mapViewModel: MapViewModel,
    journalModel: JournalEntryViewModel,
    onAddJournal: () -> Unit,
    onAddLandmark: () -> Unit,
    notificationHelper: NotificationHelper
) {
    val context = LocalContext.current
    val userLocation = mapViewModel.state.value.userLocation

    val notifiedList by remember {
        mutableStateOf(MutableList<String>(0) { "" })
    }

    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings())
    }
    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.style_json)
        ))
    }



    Box(modifier) {
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            properties = mapProperties,
        ) {
            MapMarker(
                position = userLocation,
                title = "User Location",
                context = LocalContext.current,
                iconResourceId = R.drawable.ic_my_location,
                variant = "large",
            )

            // State for api landmarks
            var landmarks by remember { mutableStateOf<List<Landmark>>(emptyList()) }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    //May need to change API key
                    val apiKey = ""
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

            if (cameraPositionState.position.zoom >= 12.5f) {
                LandmarksJournals(
                    viewModel = mapViewModel,
                    journalModel = journalModel,
                    content = { userLandmarks, journals ->
                        for (landmark in userLandmarks) {
                            val hasVisited = (journals.find { it.landmarkId == landmark.id } != null)
                            val isActive = distance(
                                mapViewModel.state.value.userLocation,
                                landmark
                            ) < MIN_JOURNAL_DISTANCE

                            if (!hasVisited && isActive && notifiedList.find { it == landmark.id } == null) {
                                notificationHelper.showNotification(landmark.name)
                                notifiedList.add(landmark.id)
                            }

                            MapMarker(
                                position = LatLng(landmark.latitude, landmark.longitude),
                                title = landmark.name,
                                context = LocalContext.current,
                                iconResourceId = if (hasVisited) R.drawable.ic_visited_landmark else if (isActive) R.drawable.ic_unvisited_landmark else R.drawable.ic_inactive_landmark,
                                onClick = {
                                    if (isActive) {
                                        journalModel.createJournalEntry(landmark)
                                        onAddJournal()
                                    } else {
                                        Toast.makeText(context, R.string.landmark_distance_too_far, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                        for (landmark in landmarks) {
                            val hasVisited = (journals.find { it.name == landmark.name } != null)
                            val isActive = distance(
                                mapViewModel.state.value.userLocation,
                                landmark
                            ) < MIN_JOURNAL_DISTANCE

                            if (!hasVisited && isActive && notifiedList.find { it == landmark.id } == null) {
                                notificationHelper.showNotification(landmark.name)
                                notifiedList.add(landmark.id)
                            }

                            MapMarker(
                                position = LatLng(landmark.latitude, landmark.longitude),
                                title = landmark.name,
                                context = LocalContext.current,
                                iconResourceId = if (hasVisited) R.drawable.ic_visited_landmark else if (isActive) R.drawable.ic_unvisited_landmark else R.drawable.ic_inactive_landmark,
                                onClick = {
                                    if (isActive) {
                                        journalModel.createJournalEntry(landmark)
                                        onAddJournal()
                                    } else {
                                        Toast.makeText(context, R.string.landmark_distance_too_far, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
        FloatingActionButton(
            modifier =
            Modifier
                .align(Alignment.BottomStart)
                .padding(vertical = 110.dp, horizontal = 13.dp)
                .width(50.dp),
            onClick = {
                onAddLandmark()
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(
                        LatLng(userLocation.latitude, userLocation.longitude)
                    )
                )
            },
            containerColor = Gray100
        ) {
            Icon(
                Icons.Default.Add,
                modifier = Modifier.width(30.dp),
                contentDescription = "Add a landmark"
            )
        }
        FloatingActionButton(
            modifier =
            Modifier
                .align(Alignment.BottomStart)
                .padding(vertical = 40.dp, horizontal = 13.dp)
                .width(50.dp),
            onClick = {
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(
                        LatLng(userLocation.latitude, userLocation.longitude)
                    )
                )
            },
            containerColor = Gray100
        ) {
            Icon(
                painterResource(id = R.drawable.ic_current_location),
                modifier = Modifier.width(30.dp),
                contentDescription = "Re-center map"
            )
        }
    }
}
