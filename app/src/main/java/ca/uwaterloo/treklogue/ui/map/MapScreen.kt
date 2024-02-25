package ca.uwaterloo.treklogue.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

/**
 * Composable for the map view that expects [onNextButtonClicked] lambda that expects
 * the selected quantity to save and triggers the navigation to next screen
 */

val waterlooLocation = LatLng(
    43.4822734, -80.5879188
)

val defaultCameraPosition = CameraPosition.fromLatLngZoom(waterlooLocation, 8f)

@Composable
fun MapScreen(
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
            GoogleMapView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp),
                cameraPositionState = cameraPositionState,
                //onMapLoaded = {}
            )

            Button(
                onClick = { onNextButtonClicked(0) },
                modifier = modifier.widthIn(min = 250.dp)
            ) {
                Text("go to profile")
            }

    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
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
    }
}