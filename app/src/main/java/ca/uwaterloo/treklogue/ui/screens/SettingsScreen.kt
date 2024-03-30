package ca.uwaterloo.treklogue.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.ui.viewModels.UserEvent
import ca.uwaterloo.treklogue.ui.viewModels.UserViewModel
import ca.uwaterloo.treklogue.ui.composables.TabSectionHeader
import ca.uwaterloo.treklogue.ui.composables.SettingsActionButton
import ca.uwaterloo.treklogue.ui.composables.SettingsGroup
import ca.uwaterloo.treklogue.ui.composables.SettingsToggle
import ca.uwaterloo.treklogue.ui.theme.Gray100
import ca.uwaterloo.treklogue.util.getCurrentLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Composable for the settings screen
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
) {
    val viewModel by remember { mutableStateOf(userViewModel) }
    var toRevoke by remember { mutableStateOf(false) }

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
                getCurrentLocation(context, { _, _ -> })
            }
        }
    )

    viewModel.toggleLocationSetting(locationPermissionState.status.isGranted || coarseLocationPermissionState.status.isGranted)

    Column(
        modifier = modifier.background(color = Gray100),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Settings Title
        TabSectionHeader(R.string.settings_name)

        // Settings Content
        Column {
            SettingsGroup(
                name = R.string.settings_group_permissions,
                content =
                listOf(
                    {
                        SettingsToggle(
                            name = R.string.settings_toggle_notifications,
                            state = viewModel.state.value.notificationEnabled
                        ) {
                            viewModel.toggleNotificationSetting(it)
                        }
                    },
                    {
                        // this is a bit awkward because apparently from Android 11,
                        // you can only request and get denied twice before getting locked out of sending requests

                        // this also combines coarse/precise location perms (on when at least coarse)

                        // note that you need to close app for perms to be revoked, hence locking user out of toggle
                        SettingsToggle(
                            enabled = !toRevoke,
                            name = if (toRevoke) R.string.settings_toggle_location_revoke else R.string.settings_toggle_location,
                            state = viewModel.state.value.locationEnabled,
                        ) {
                            if (it) {
                                requestPermissionLauncher.launch(locationPermission)
                            } else {
                                context.revokeSelfPermissionOnKill(Manifest.permission.ACCESS_FINE_LOCATION)
                                context.revokeSelfPermissionOnKill(Manifest.permission.ACCESS_COARSE_LOCATION)
                                toRevoke = true
                            }
                        }
                    },
                )
            )
            SettingsGroup(
                name = R.string.settings_group_profile,
                content =
                listOf {
                    SettingsActionButton(
                        text = R.string.log_out,
                        icon = R.drawable.ic_logout,
                    ) {
                        CoroutineScope(Dispatchers.IO).launch {
                            runCatching {
                                userViewModel.logOut()
                            }.onSuccess {
                                Log.v(null, "Logged out successfully.")
                            }.onFailure {
                                userViewModel.error(UserEvent.Error("Log out failed", it))
                            }
                        }
                    }
                }
            )
        }
    }
}
