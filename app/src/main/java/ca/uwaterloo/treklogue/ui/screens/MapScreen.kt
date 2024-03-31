package ca.uwaterloo.treklogue.ui.screens

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.repository.Landmarks
import ca.uwaterloo.treklogue.data.repository.JournalEntries
import ca.uwaterloo.treklogue.ui.composables.LoadingPopup
import ca.uwaterloo.treklogue.ui.composables.MapMarker
import ca.uwaterloo.treklogue.ui.composables.ProgressBar
import ca.uwaterloo.treklogue.ui.theme.Gray100
import ca.uwaterloo.treklogue.ui.viewModels.JournalEntryViewModel
import ca.uwaterloo.treklogue.ui.viewModels.MapViewModel
import ca.uwaterloo.treklogue.util.getCurrentLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Composable for the map view
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel,
    journalModel: JournalEntryViewModel,
) {
    val defaultCameraPosition =
        CameraPosition.fromLatLngZoom(mapViewModel.state.value.userLocation, 12f)
    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    val context = LocalContext.current

    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    val locationPermissionState = rememberPermissionState(locationPermission)

    val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
    val coarseLocationPermissionState = rememberPermissionState(coarseLocationPermission)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            // there is a bug where isGranted is false when only coarse access is given, so location is not updated
            // getting rid of 'if' seems fine though, app will warn but no crash and behaviour seems okay
//            if (isGranted) {
                // Permission granted, update the location
                getCurrentLocation(context, { lat, long, updateCam ->
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
                })
//            }
        }
    )

    LaunchedEffect(Unit) {
        // Move this to a separate thread to prevent it from blocking the Main thread
        withContext(Dispatchers.IO) {
            if (locationPermissionState.status.isGranted || coarseLocationPermissionState.status.isGranted) {
                // Permission already granted, update the location
                getCurrentLocation(context, { lat, long, updateCam ->
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
                })
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
    var landmarksContent : Landmarks? = null

    val journalEntriesResponse = journalModel.journalEntryResponse
    var journalsLoading = true
    var journalsContent : JournalEntries? = null


    when (landmarksResponse) {
        is Response.Loading -> landmarksLoading = true
        is Response.Success -> {
            landmarksContent = landmarksResponse.data
            landmarksLoading = false
        }
        is Response.Failure -> print(landmarksResponse.e)
    }

    when (journalEntriesResponse) {
        is Response.Loading -> journalsLoading = true
        is Response.Success -> {
            journalsContent = journalEntriesResponse.data
            journalsLoading = false
        }
        is Response.Failure -> print(journalEntriesResponse.e)
    }

    if (landmarksLoading && journalsLoading) {
        ProgressBar()
    } else {
        content(landmarksContent!!, journalsContent!!)
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    mapViewModel: MapViewModel,
    journalModel: JournalEntryViewModel
) {
    val userLocation = mapViewModel.state.value.userLocation

    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings())
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
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
                variant = "large"
            )

            LandmarksJournals(
                viewModel = mapViewModel,
                journalModel = journalModel,
                content = { landmarks, journals ->
                    for (landmark in landmarks) {
                        // TODO: this needs to be replaced with IDs
                        val hasVisited = (journals.find { it.name == landmark.id } != null)
                        MapMarker(
                            position = LatLng(landmark.latitude, landmark.longitude),
                            title = landmark.name,
                            context = LocalContext.current,
                            iconResourceId = if (hasVisited)  R.drawable.ic_visited_landmark else R.drawable.ic_unvisited_landmark,
                        )
                    }
                }
            )
        }
        FloatingActionButton(
            modifier =
            Modifier
                .align(Alignment.BottomStart)
                .padding(vertical = 40.dp, horizontal = 13.dp)
                .width(50.dp),
            onClick = {
                cameraPositionState.move(CameraUpdateFactory.newLatLng(
                    LatLng(userLocation.latitude, userLocation.longitude)))
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
