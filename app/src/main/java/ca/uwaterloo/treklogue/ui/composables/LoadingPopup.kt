package ca.uwaterloo.treklogue.ui.composables

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.ui.theme.Gray100
import ca.uwaterloo.treklogue.ui.viewModels.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng

/**
 * Composable for loading screen
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoadingPopup(
    mapViewModel: MapViewModel,
) {
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    val locationPermissionState = rememberPermissionState(locationPermission)

    val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
    val coarseLocationPermissionState = rememberPermissionState(coarseLocationPermission)

    if (mapViewModel.state.value.userLocation == LatLng(
            43.4822734, -80.5879188)
    ) {
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