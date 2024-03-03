package ca.uwaterloo.treklogue.ui.map

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.model.Landmark
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


/**
 * Composable for the map view
 */

val waterlooLocation = LatLng(
    43.4822734, -80.5879188
)

val defaultCameraPosition = CameraPosition.fromLatLngZoom(waterlooLocation, 8f)

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel,
    onDetailClicked: (Any?) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    Box(modifier) {
        GoogleMapView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            cameraPositionState = cameraPositionState,
            landmarks = mapViewModel.state.value.landmarks,
            //onMapLoaded = {}
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
    landmarks: SnapshotStateList<Landmark>
    //onMapLoaded: () -> Unit
) {
    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings(compassEnabled = false))
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    val locationState = rememberMarkerState(
        position = waterlooLocation
    )

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
        properties = mapProperties
    ) {
        Marker(
            state = locationState,
            draggable = true
        )
        for (landmark in landmarks) {
            LandmarkMarker(LatLng(landmark.latitude, landmark.longitude), landmark.name)
        }
    }
}

@Composable
fun LandmarkMarker(position: LatLng, title: String) {
    val markerState = rememberMarkerState(null, position)
    Marker(
        state = markerState,
        title = title,
        snippet = title, // TODO: Can include landmark description in the future
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
    )
}