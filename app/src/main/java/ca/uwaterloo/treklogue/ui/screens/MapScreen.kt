package ca.uwaterloo.treklogue.ui.screens

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.repository.Landmarks
import ca.uwaterloo.treklogue.ui.composables.MapMarker
import ca.uwaterloo.treklogue.ui.composables.ProgressBar
import ca.uwaterloo.treklogue.ui.theme.Gray100
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
    mapViewModel: MapViewModel
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
            if (isGranted) {
                // Permission granted, update the location
                getCurrentLocation(context, { lat, long ->
                    mapViewModel.setUserLocation(LatLng(lat, long))

                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLng(
                            LatLng(lat, long)
                        )
                    )
                })
                Log.v(null, "Updating location...")
            }
        }
    )

    LaunchedEffect(Unit) {
        // Move this to a separate thread to prevent it from blocking the Main thread
        withContext(Dispatchers.IO) {
            if (locationPermissionState.status.isGranted || coarseLocationPermissionState.status.isGranted) {
                // Permission already granted, update the location
                getCurrentLocation(context, { lat, long ->
                    mapViewModel.setUserLocation(LatLng(lat, long))

                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLng(
                            LatLng(lat, long)
                        )
                    )
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
            userLocation = mapViewModel.state.value.userLocation,
            mapViewModel
        )

        // allowing user to use the app with default location is probably not a good idea
        if (mapViewModel.state.value.userLocation == LatLng(
                43.4822734, -80.5879188)) {
            Box(modifier = Modifier.fillMaxSize().background(Gray100)) {
                Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
                    Text(text =
                    if (!locationPermissionState.status.isGranted && !coarseLocationPermissionState.status.isGranted)
                        "Please allow location permissions..."
                    else "Retrieving initial location...", style = MaterialTheme.typography.headlineLarge)
                    Spacer(Modifier.width(16.dp))
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun Landmarks(
    viewModel: MapViewModel,
    landmarksContent: @Composable (landmarks: Landmarks) -> Unit
) {
    when(val landmarksResponse = viewModel.landmarksResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> landmarksContent(landmarksResponse.data)
        is Response.Failure -> print(landmarksResponse.e)
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    userLocation: LatLng,
    mapViewModel: MapViewModel
) {
    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings())
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

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

        Landmarks(
            viewModel = mapViewModel,
            landmarksContent = { landmarks ->
                for (landmark in landmarks) {
                    MapMarker(
                        position = LatLng(landmark.latitude, landmark.longitude),
                        title = landmark.name,
                        context = LocalContext.current,
//                iconResourceId = if (hasVisited) R.drawable.ic_unvisited_landmark else R.drawable.ic_visited_landmark,
                        iconResourceId = R.drawable.ic_unvisited_landmark
                    )
                }
            }
        )
    }
}
