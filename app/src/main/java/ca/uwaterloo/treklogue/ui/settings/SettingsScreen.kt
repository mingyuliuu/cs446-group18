package ca.uwaterloo.treklogue.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.controller.UserController
import ca.uwaterloo.treklogue.ui.UserViewModel
import ca.uwaterloo.treklogue.ui.ViewEvent

/**
 * Composable for the settings screen
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    userController: UserController
) {
    val viewModel by remember { mutableStateOf(userViewModel) }
    val controller by remember { mutableStateOf(userController) }

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
                            state = viewModel.toggleChecked
                        ) {
                            controller.invoke(ViewEvent.ToggleEvent, it)
                        }
                    },
                    {
                        SettingsToggle(
                            name = R.string.settings_toggle_notifications,
                            state = viewModel.toggleChecked
                        ) {
                            controller.invoke(ViewEvent.ToggleEvent, it)
                        }
                    },
                    {
                        SettingsToggle(
                            name = R.string.settings_toggle_notifications,
                            state = viewModel.toggleChecked
                        ) {
                            controller.invoke(ViewEvent.ToggleEvent, it)
                        }
                    },
                )
            )
        }
    }
}
