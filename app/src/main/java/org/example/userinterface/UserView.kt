package org.example.userinterface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.example.controller.UserController
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.example.R
import org.example.userinterface.components.ScaffoldBottom
import org.example.userinterface.components.ScaffoldTop
import org.example.userinterface.screens.MapScreen
import org.example.userinterface.screens.ProfileScreen


/**
 * enum values that represent the screens in the app
 */
enum class Screen(@StringRes val title: Int) {
    Map(title = R.string.home_name),
    Profile(title = R.string.profile_name)
}

enum class ViewEvent {
    FirstNameEvent,
    LastNameEvent,
    UppercaseEvent,
    LowercaseEvent,
    ResetEvent
}

@Composable
fun UserView(
    userViewModel: UserViewModel,
    userController: UserController,
    navController: NavHostController = rememberNavController()
) {
    val viewModel by remember { mutableStateOf(userViewModel) }
    val controller by remember { mutableStateOf(userController) }

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = Screen.valueOf(
        // default should probably be login once that's made
        backStackEntry?.destination?.route ?: Screen.Map.name
    )

    Scaffold(
        topBar = {
            ScaffoldTop(
                // may be good to wrap the home/default in a val
                navigateHome = { navController.navigate(Screen.Map.name) }
            )
        },
        bottomBar = {
            ScaffoldBottom(
                currentScreen = currentScreen,
                navigateHome = { navController.navigate(Screen.Map.name) },
                navigateProfile = { navController.navigate(Screen.Profile.name) }
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Map.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            // add routes here
            composable(route = Screen.Map.name) {
                MapScreen(
                    onNextButtonClicked = {
                        controller.invoke(ViewEvent.UppercaseEvent, it) // just an example
                        navController.navigate(Screen.Profile.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = Screen.Profile.name) {
                ProfileScreen(
                    onNextButtonClicked = {
                        controller.invoke(ViewEvent.UppercaseEvent, it)
                        navController.navigate(Screen.Map.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}