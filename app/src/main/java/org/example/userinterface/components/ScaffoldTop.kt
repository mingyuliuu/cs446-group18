package org.example.userinterface.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.example.R

/**
 * Composable that displays the topBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTop(
    navigateHome: () -> Unit,
    modifier: Modifier = Modifier
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
        }
    )
}