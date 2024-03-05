package ca.uwaterloo.treklogue.ui.map

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.model.Landmark
import ca.uwaterloo.treklogue.util.getCurrentLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
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
    onDetailClicked: (Any?) -> Unit,
) {
    val defaultLocation = LatLng(
        43.4822734, -80.5879188
    ) // Waterloo
    val currentLocation = remember {
        mutableStateOf(
            defaultLocation
        )
    }

    val defaultCameraPosition = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    val context = LocalContext.current

    // TODO: handle coarse location access as well
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    val locationPermissionState = rememberPermissionState(locationPermission)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permission granted, update the location
                getCurrentLocation(context, { lat, long ->
                    currentLocation.value = LatLng(lat, long)
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
            if (locationPermissionState.status.isGranted) {
                // Permission already granted, update the location
                getCurrentLocation(context, { lat, long ->
                    currentLocation.value = LatLng(lat, long)
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
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            userLocation = currentLocation.value,
            landmarks = mapViewModel.state.value.landmarks,
        )
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            shape = RoundedCornerShape(12),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            onClick = { onDetailClicked("") },
        ) {
            Icon(
                painterResource(id = R.drawable.ic_add),
                contentDescription = "Add new journal entry"
            )
        }
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    userLocation: LatLng,
    landmarks: SnapshotStateList<Landmark>
) {
    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings())
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    for (landmark in landmarks) {
        Log.v(
            null,
            "LANDMARK: " + landmark.name + " " + landmark.latitude + " " + landmark.longitude
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings,
        properties = mapProperties,
    ) {
        Marker(
            state = MarkerState(position = userLocation),
            title = "User Location",
        )
        for (landmark in landmarks) {
            LandmarkMarker(
                LatLng(landmark.latitude, landmark.longitude),
                landmark.name,
                true
            )
        }
    }
}

@Composable
fun LandmarkMarker(position: LatLng, title: String, hasVisited: Boolean) {
    val markerState = rememberMarkerState(null, position)

    Marker(
        state = markerState,
        title = title,
        snippet = title, // TODO: Can include landmark description in the future
        icon = if (hasVisited) BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE) else BitmapDescriptorFactory.defaultMarker(
            BitmapDescriptorFactory.HUE_YELLOW
        ),
        alpha = if (hasVisited) 1F else 0.8F
    )
}