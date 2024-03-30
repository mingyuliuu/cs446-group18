package ca.uwaterloo.treklogue.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ca.uwaterloo.treklogue.R

/**
 * Composable that displays the topBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTop(
    navigateHome: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean,
    ) {
    TopAppBar(
        // I think the nav logo is sufficient
        title = { "" },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigateHome) {
                Icon(
                    // eventually replace with app icon?
                    imageVector = Icons.Filled.Home,
                    contentDescription = stringResource(R.string.home_button)
                )
            }
        }, actions = {

            if (canNavigateBack) {
                IconButton(
                    onClick = navigateUp,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = null
                    )
                }
                // i don't think we need a logout button on screen at all times
                // maybe copy how IG and other apps do it: logout from settings
//            IconButton(
//                onClick = {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        runCatching {
//                            app.currentUser?.logOut()
//                        }.onSuccess {
////                            viewModel.logOut() // TODO: Fix login view model
//                        }.onFailure {
//                            // TODO: log error
//                        }
//                    }
//                },
//                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_baseline_logout_24_white),
//                    contentDescription = null
//                )
//            }
            }
        }
    )
}