@file:OptIn(ExperimentalMaterial3Api::class)

package ca.uwaterloo.treklogue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.lifecycleScope
import ca.uwaterloo.treklogue.ui.Router
import ca.uwaterloo.treklogue.ui.theme.MyApplicationTheme
import ca.uwaterloo.treklogue.ui.viewModels.UserEvent
import ca.uwaterloo.treklogue.ui.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            userViewModel.event
                .collect { userEvent ->
                    when (userEvent) {
                        UserEvent.LogOut -> {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }

                        is UserEvent.Info ->
                            Log.e(TAG(), userEvent.message)

                        is UserEvent.Error ->
                            Log.e(TAG(), "${userEvent.message}: ${userEvent.throwable.message}")
                    }
                }
        }

        setContent {
            MyApplicationTheme {
                Router(userViewModel)
            }
        }
    }

}
