@file:OptIn(ExperimentalMaterial3Api::class)

package ca.uwaterloo.treklogue

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import ca.uwaterloo.treklogue.ui.Router
import ca.uwaterloo.treklogue.ui.login.LoginActivity
import ca.uwaterloo.treklogue.ui.login.LoginEvent
import ca.uwaterloo.treklogue.ui.login.LoginViewModel
import ca.uwaterloo.treklogue.ui.map.MapScreen
import ca.uwaterloo.treklogue.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            // Subscribe to navigation and message-logging events
            loginViewModel.event
                .collect { event ->
                    when (event) {
                        is LoginEvent.LogOutAndExit -> {
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else -> {
                            // SKIP
                        }
                    }
                }
        }

        setContent {
            MyApplicationTheme {
                Router()
            }
        }
        /*setContent {
            MapScreen(
                /*onNextButtonClicked = {
                navController.navigate(Screen.Profile.name)
            },*/
                modifier = Modifier.fillMaxSize()
            )
        }*/
    }
}
