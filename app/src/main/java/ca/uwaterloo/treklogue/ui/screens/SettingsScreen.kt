package ca.uwaterloo.treklogue.ui.screens

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.ui.composables.SettingsGroup
import ca.uwaterloo.treklogue.ui.composables.SettingsToggle
import ca.uwaterloo.treklogue.ui.theme.Blue100
import ca.uwaterloo.treklogue.ui.theme.Gray100
import ca.uwaterloo.treklogue.ui.viewModels.LoginViewModel
import ca.uwaterloo.treklogue.ui.viewModels.UserEvent
import ca.uwaterloo.treklogue.ui.viewModels.UserViewModel
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
    onLocationToggle: () -> Unit,
    loginViewModel: LoginViewModel
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
                // Permission granted, update the location by going to map screen
                onLocationToggle()
            }
        }
    )

    viewModel.toggleLocationSetting(locationPermissionState.status.isGranted || coarseLocationPermissionState.status.isGranted)
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Gray100)
    ){
            // Top bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = Blue100)
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.immigrant),
                        contentDescription = "UserProfile top left image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(120.dp)
                            .height(150.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.trip),
                        contentDescription = "UserProfile top right image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(130.dp)
                            .height(150.dp)
                    )
                }
            }

        // User profile
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-70).dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(140.dp)
                    .border(width = 1.dp, color = Color(0xFF797878), shape = CircleShape)
                    .background(color = Color(0xFFD9D9D9), shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.traveler),
                    contentDescription = "User profile picture",
                    modifier = Modifier
                        .size(90.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .offset(y = (-70).dp)
        ) {
            loginViewModel.currentUser?.let {
                it.email?.let { email ->
                    Text(
                        text = email,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        textAlign = Center
                    )
                }
            }
        }



        Box(
            modifier = Modifier
                .background(color = Gray100)
                .offset(y = (-70).dp)
        ) {
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
        }
        Box(
            modifier = Modifier
                .background(color = Gray100)
                .fillMaxWidth()
                .padding(10.dp)
                .height(150.dp)
                .offset(y = (-70).dp)
        ) {
            var feedback by remember { mutableStateOf("") }
            OutlinedTextField(
                value = feedback,
                onValueChange = { feedback = it },
                maxLines = 4,
                label = { Text(stringResource(id = R.string.feedback)) },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {

                })
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-70).dp)
                .graphicsLayer {
                    translationY = 40f
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        runCatching {
                            userViewModel.logOut()
                        }.onSuccess {
                            Log.v(null, "Logged out successfully.")
                        }.onFailure {
                            userViewModel.error(UserEvent.Error("Log out failed", it))
                        }
                    }
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Logout",
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(fontSize = 20.sp),
                )
            }
        }
    }
}
