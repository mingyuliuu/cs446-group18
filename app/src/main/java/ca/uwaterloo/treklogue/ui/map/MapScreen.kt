package ca.uwaterloo.treklogue.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockLandmark
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
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
    val waterlooLocation = LatLng(
        43.4822734, -80.5879188
    )

    val defaultCameraPosition = CameraPosition.fromLatLngZoom(waterlooLocation, 8f)

    var lastLocation: Location

    var latLng = remember {
        mutableStateOf(
            LatLng(
                40.0, -70.0
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    // TODO: handle coarse location access as well
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val context = LocalContext.current

    val fusedLocationClient: FusedLocationProviderClient = remember(context) {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val locationRequest = LocationRequest.Builder(10000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (location in p0.locations) {
                latLng.value = LatLng(location.latitude, location.longitude)
            }
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                // this is just to set an initial location
                // ideally, i would like to just make the auto-updater run at startup, but idk how
                fusedLocationClient.lastLocation.addOnCompleteListener(context.mainExecutor) { task ->
                    if (task.isSuccessful) {
                        lastLocation = task.result
                        latLng.value = LatLng(lastLocation.latitude, lastLocation.longitude)
                        cameraPositionState.move(CameraUpdateFactory.newLatLng(latLng.value))
                    }
                }

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                // TODO: handle error
            }
        } else {
            // TODO: handle permission denial
        }
    }

    LaunchedEffect(locationPermissionState) {
        if (!locationPermissionState.status.isGranted) {
            if (locationPermissionState.status.shouldShowRationale) {
                // show a message
            }
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // compiler doesn't recognize locationPermissionState as a way of checking perms and complains about it
            // so I just used a method it would recognize. Ideally, don't do this
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }
        }
    }

    val mockLandmarks: List<MockLandmark> = listOf(
        MockLandmark(
            "University of Waterloo DC library", 43.472403, -80.541979, true
        ),
        MockLandmark(
            "Lazaridis School of Business and Economics", 43.475046, -80.529481, false
        ),
        MockLandmark(
            "Toronto Union Station", 43.644601, -79.380525, false
        ),
        MockLandmark(
            "Stratford Shakespearean Garden", 43.371913, -80.985196, true
        )
    )

    Box(modifier) {
        GoogleMapView(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            userLocation = latLng.value,
//            landmarks = mapViewModel.state.value.landmarks,
            landmarks = mockLandmarks
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
//    landmarks: SnapshotStateList<Landmark>
    landmarks: List<MockLandmark>
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
                landmark.hasVisited
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