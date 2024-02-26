package ca.uwaterloo.treklogue.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.ui.UserViewModel

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
        modifier = modifier.padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: move title to topbar
        Text("SETTINGS")
        Divider(
            thickness = 2.dp,
            modifier = Modifier.padding(bottom = 50.dp)
        )
        Column {
//            SettingsGroup(name = R.string.settings_group_profile) {
//            }
            SettingsGroup(
                name = R.string.settings_group_notifications,
                // unfortunately can't do { } notation bc of list
                // copy paste to showcase multiple settings in group
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
                            name = R.string.settings_toggle_notifications,
                            state = viewModel.state.value.notificationEnabled
                        ) {
                            viewModel.toggleNotificationSetting(it)
                        }
                    },
                    {
                        SettingsToggle(
                            name = R.string.settings_toggle_notifications,
                            state = viewModel.state.value.notificationEnabled
                        ) {
                            viewModel.toggleNotificationSetting(it)
                        }
                    },
                )
            )
        }
    }
}
