package ca.uwaterloo.treklogue.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ca.uwaterloo.treklogue.app
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.ui.login.LoginViewModel

/**
 * Composable for the profile view that expects [onNextButtonClicked] lambda that expects
 * the selected quantity to save and triggers the navigation to next screen
 */
@Composable
fun ProfileScreen(
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("you are profile")
            if (app.currentUser != null) {
                Text(app.currentUser!!.id)
            } else {
                Text("Logged out")
            }
            IconButton(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        runCatching {
                            app.currentUser?.logOut()
                        }.onSuccess {
                            loginViewModel.logOut() // TODO: Fix login view model
                        }.onFailure {
                            // TODO: log error
                        }
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Black)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_logout_24_white),
                    contentDescription = null
                )
            }
            Button(
                onClick = { onNextButtonClicked(0) },
                modifier = modifier.widthIn(min = 250.dp)
            ) {
                Text("go to settings")
            }
        }
    }
}