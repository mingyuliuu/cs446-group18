package ca.uwaterloo.treklogue.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
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
import ca.uwaterloo.treklogue.ui.composables.LandmarkAdd
import ca.uwaterloo.treklogue.ui.composables.ScaffoldBottom
import ca.uwaterloo.treklogue.ui.screens.ListScreen
import ca.uwaterloo.treklogue.ui.screens.MapScreen
import ca.uwaterloo.treklogue.ui.screens.ProfileScreen
import ca.uwaterloo.treklogue.ui.screens.Screens
import ca.uwaterloo.treklogue.ui.screens.SettingsScreen
import ca.uwaterloo.treklogue.ui.viewModels.JournalEntryViewModel
import ca.uwaterloo.treklogue.ui.viewModels.LoginViewModel
import ca.uwaterloo.treklogue.ui.viewModels.MapViewModel
import ca.uwaterloo.treklogue.ui.viewModels.UserViewModel

@Composable
fun Router(
    userViewModel: UserViewModel,
    mapViewModel: MapViewModel,
    journalEntryViewModel: JournalEntryViewModel,
    loginViewModel: LoginViewModel
) {
    val navigationController = rememberNavController()
    val selectedTab = remember {
        mutableStateOf(Icons.Default.LocationOn)
    }
    val journalBack = remember {
        mutableStateOf(Screens.Profile.screen)
    }

    Scaffold(
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
                    mapViewModel = mapViewModel,
                    journalModel = journalEntryViewModel,
                    userViewModel = userViewModel,
                    onAddJournal = {
                        journalBack.value = Screens.Map.screen
                        navigationController.navigate(Screens.AddJournal.screen)
                    },
                    onAddLandmark = {
                        navigationController.navigate(Screens.AddLandmark.screen)
                    },
                )
            }

            composable(route = Screens.List.screen) {
                ListScreen(
                    modifier = Modifier.fillMaxSize(),
                    onAddJournal = {
                        journalBack.value = Screens.List.screen
                        navigationController.navigate(Screens.AddJournal.screen)
                    },
                    mapViewModel,
                    journalEntryViewModel
                )
            }

            composable(route = Screens.Profile.screen) {
                ProfileScreen(
                    modifier = Modifier.fillMaxSize(),
                    showJournalDetail = {
                        journalBack.value = Screens.Profile.screen
                        navigationController.navigate(Screens.EditJournal.screen)
                    },
                    journalEntryViewModel
                )
            }

            composable(route = Screens.Settings.screen) {
                SettingsScreen(
                    Modifier,
                    userViewModel,
                    onLocationToggle = {
                        selectedTab.value = Icons.Default.LocationOn
                        navigationController.navigate(Screens.Map.screen)
                    },
                    loginViewModel
                )
            }

            composable(route = Screens.EditJournal.screen) {
                JournalEntryDetail(
                    modifier = Modifier.fillMaxSize(),
                    isEditing = true,
                    onSaveClicked = {
                        selectedTab.value = Icons.Default.AccountCircle
                        navigationController.navigate(Screens.Profile.screen)
                    },
                    onBackClicked = {
                        if (journalBack.value == Screens.Profile.screen) {
                            selectedTab.value = Icons.Default.AccountCircle
                            navigationController.navigate(Screens.Profile.screen)
                        } else if (journalBack.value == Screens.Map.screen) {
                            selectedTab.value = Icons.Default.LocationOn
                            navigationController.navigate(Screens.Map.screen)
                        } else if (journalBack.value == Screens.List.screen) {
                            selectedTab.value = Icons.Default.List
                            navigationController.navigate(Screens.List.screen)
                        }
                    },
                    journalEntryViewModel
                )
            }

            composable(route = Screens.AddJournal.screen) {
                JournalEntryDetail(
                    modifier = Modifier.fillMaxSize(),
                    onSaveClicked = {
                        selectedTab.value = Icons.Default.AccountCircle
                        navigationController.navigate(Screens.Profile.screen)
                    },
                    onBackClicked = {
                        if (journalBack.value == Screens.Profile.screen) {
                            selectedTab.value = Icons.Default.AccountCircle
                            navigationController.navigate(Screens.Profile.screen)
                        } else if (journalBack.value == Screens.Map.screen) {
                            selectedTab.value = Icons.Default.LocationOn
                            navigationController.navigate(Screens.Map.screen)
                        } else if (journalBack.value == Screens.List.screen) {
                            selectedTab.value = Icons.Default.List
                            navigationController.navigate(Screens.List.screen)
                        }
                    },
                    journalEntryViewModel = journalEntryViewModel
                )
            }

            composable(route = Screens.AddLandmark.screen) {
                LandmarkAdd(
                    modifier = Modifier.fillMaxSize(),
                    onBackClicked = {
                        navigationController.navigate(Screens.Map.screen)
                    },
                    mapViewModel = mapViewModel,
                )
            }
        }
    }
}