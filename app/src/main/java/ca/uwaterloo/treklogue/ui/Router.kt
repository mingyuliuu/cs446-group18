package ca.uwaterloo.treklogue.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.ui.composables.ScaffoldBottom
import ca.uwaterloo.treklogue.ui.composables.ScaffoldTop
import ca.uwaterloo.treklogue.ui.map.MapScreen
import ca.uwaterloo.treklogue.ui.profile.ProfileScreen
import ca.uwaterloo.treklogue.ui.list.ListScreen
import ca.uwaterloo.treklogue.ui.list.LandmarkDetail
import ca.uwaterloo.treklogue.ui.list.ListViewModel

/**
 * enum values that represent the screens in the app
 */
enum class Screen(@StringRes val title: Int) {
    Map(title = R.string.home_name),
    Profile(title = R.string.profile_name),
    List(title = R.string.list_name),
    Landmark(title = R.string.landmark_name),
}

@Composable
fun Router(
    navController: NavHostController = rememberNavController()
) {
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
                navigateProfile = { navController.navigate(Screen.Profile.name) },
                navigateList = { navController.navigate(Screen.List.name) }
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
                        navController.navigate(Screen.Profile.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = Screen.Profile.name) {
                ProfileScreen(
                    onNavigateToMap = {
                        navController.navigate(Screen.Map.name)
                    },
                    onNavigateToList = {
                        navController.navigate(Screen.List.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = Screen.List.name) {
                val listViewModel: ListViewModel = viewModel()
                ListScreen(
                    onNextButtonClicked = {
                        navController.navigate(Screen.Profile.name)
                    },
//                    onDetailClicked = {
//                        navController.navigate(Screen.Landmark.name)
//                    },
                    onDetailClicked = { landmark ->
                        listViewModel.selectLandmark(landmark)
                        // TODO: change the route to landmark detail
                        navController.navigate(Screen.Map.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}