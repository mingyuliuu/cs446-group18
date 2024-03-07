package ca.uwaterloo.treklogue.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.app
import ca.uwaterloo.treklogue.ui.UserEvent
import ca.uwaterloo.treklogue.ui.UserViewModel
import ca.uwaterloo.treklogue.ui.composables.TabSectionHeader
import ca.uwaterloo.treklogue.ui.theme.Gray100
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Composable for the settings screen
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
) {
    val viewModel by remember { mutableStateOf(userViewModel) }

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
                        SettingsToggle(
                            name = R.string.settings_toggle_location,
                            state = viewModel.state.value.locationEnabled
                        ) {
                            viewModel.toggleLocationSetting(it)
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
                        icon = R.drawable.ic_baseline_logout_24_white,
                    ) {
                        CoroutineScope(Dispatchers.IO).launch {
                            runCatching {
                                app.currentUser?.logOut()
                            }.onSuccess {
                                userViewModel.logOut()
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
