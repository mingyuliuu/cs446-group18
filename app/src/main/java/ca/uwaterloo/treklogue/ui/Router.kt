package ca.uwaterloo.treklogue.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ca.uwaterloo.treklogue.R
//import ca.uwaterloo.treklogue.ui.composables.ScaffoldBottom
import ca.uwaterloo.treklogue.ui.composables.ScaffoldTop
import ca.uwaterloo.treklogue.ui.composables.Screens
import ca.uwaterloo.treklogue.ui.map.MapScreen
import ca.uwaterloo.treklogue.ui.profile.ProfileScreen
import ca.uwaterloo.treklogue.ui.settings.SettingsScreen
import ca.uwaterloo.treklogue.ui.theme.Blue
import ca.uwaterloo.treklogue.ui.theme.Purple700

/**
 * enum values that represent the screens in the app
 * maybe move this into data?
 */
/*
enum class Screen(@StringRes val title: Int) {
    Map(title = R.string.home_name),
    Profile(title = R.string.profile_name),
    Settings(title = R.string.settings_name),
}
 */

// possibly move this
enum class ViewEvent {
    UsernameEvent,
    PasswordEvent,
    ToggleEvent,
}

@Composable
fun Router(
    userViewModel: UserViewModel,
) {
    /*
    val navController: NavHostController = rememberNavController()
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = Screens.valueOf(
        // default should probably be login once that's made
        backStackEntry?.destination?.route ?: Screen.Map.name
    )
     */
    val navigationController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.LocationOn)
    }
    Scaffold(
        /*
        topBar = {
            ScaffoldTop(
                // may be good to wrap the home/default in a val
                navigateHome = { navController.navigate(Screen.Map.name) },
                navigateUp = { navController.navigateUp() },
                canNavigateBack = currentScreen == Screen.Settings
            )
        },
         */
        bottomBar = {
            BottomAppBar(
                containerColor = Blue
            ) {
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.LocationOn
                        navigationController.navigate(Screens.Map.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.LocationOn) Color.White else Color.DarkGray
                    )
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.List
                        navigationController.navigate(Screens.Lists.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.List) Color.White else Color.DarkGray
                    )
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.AccountCircle
                        navigationController.navigate(Screens.Profile.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.AccountCircle) Color.White else Color.DarkGray
                    )
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Settings
                        navigationController.navigate(Screens.Settings.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Settings) Color.White else Color.DarkGray
                    )
                }
            }

//            ScaffoldBottom(
//                currentScreen = currentScreen,
//                navigateHome = { navController.navigate(Screen.Map.name) },
//                navigateProfile = { navController.navigate(Screen.Profile.name) },
//                )
        },
    ) { innerPadding ->
        NavHost(
            navController = navigationController,
            startDestination = Screens.Map.screen,
            modifier = Modifier
                .fillMaxSize()

                //This messes up the scrolling for google maps
                //.verticalScroll(rememberScrollState())

                .padding(innerPadding)
        ) {
            // add routes here
            composable(route = Screens.Map.screen) {
                MapScreen(
                    onNextButtonClicked = {
                        navigationController.navigate(Screens.Lists.screen)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable(route = Screens.Lists.screen) {
                //ADDING LISTVIEW
            }

            composable(route = Screens.Profile.screen) {
                ProfileScreen(
                    onNextButtonClicked = {
                        navigationController.navigate(Screens.Settings.screen)
                    },
                    modifier = Modifier.fillMaxSize(),
                    userViewModel
                )
            }
            composable(route = Screens.Settings.screen) {
                SettingsScreen(
                    Modifier, userViewModel
                )
            }
        }
    }
}