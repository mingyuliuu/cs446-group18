package ca.uwaterloo.treklogue.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.uwaterloo.treklogue.ui.composables.JournalEntryDetail
import ca.uwaterloo.treklogue.ui.composables.ScaffoldBottom
import ca.uwaterloo.treklogue.ui.composables.Screens
import ca.uwaterloo.treklogue.ui.list.ListScreen
import ca.uwaterloo.treklogue.ui.map.MapScreen
import ca.uwaterloo.treklogue.ui.map.MapViewModel
import ca.uwaterloo.treklogue.ui.profile.JournalEntryViewModel
import ca.uwaterloo.treklogue.ui.profile.ProfileScreen
import ca.uwaterloo.treklogue.ui.settings.SettingsScreen

@Composable
fun Router(
    userViewModel: UserViewModel,
    mapViewModel: MapViewModel,
    journalEntryViewModel: JournalEntryViewModel
) {
    val navigationController = rememberNavController()
    val selectedTab = remember {
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
            ScaffoldBottom(
                selectedTab = selectedTab,
                navigationController = navigationController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navigationController,
            startDestination = Screens.Map.screen,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = Screens.Map.screen) {
                MapScreen(
                    modifier = Modifier.fillMaxSize(),
                    mapViewModel,
                )
            }

            composable(route = Screens.List.screen) {
                ListScreen(
                    modifier = Modifier.fillMaxSize(),
                    mapViewModel,
                    journalEntryViewModel,
                    onAddJournal = { _ ->
                        navigationController.navigate(Screens.AddJournal.screen)
                    },
                )
            }

            composable(route = Screens.Profile.screen) {
                ProfileScreen(
                    modifier = Modifier.fillMaxSize(),
                    userViewModel,
                    journalEntryViewModel,
                    showJournalDetail = {
                        navigationController.navigate(Screens.EditJournal.screen)
                    },
                )
            }

            composable(route = Screens.Settings.screen) {
                SettingsScreen(
                    Modifier, userViewModel
                )
            }

            composable(route = Screens.EditJournal.screen) {
                JournalEntryDetail(
                    modifier = Modifier.fillMaxSize(),
                    isEditing = true,
                    onBackClicked = {
                        navigationController.navigate(Screens.Profile.screen)
                    },
                    journalEntryViewModel
                )
            }

            composable(route = Screens.AddJournal.screen) {
                JournalEntryDetail(
                    modifier = Modifier.fillMaxSize(),
                    onBackClicked = {
                        navigationController.navigate(Screens.List.screen)
                    },
                    journalEntryViewModel = journalEntryViewModel
                )
            }
        }
    }
}