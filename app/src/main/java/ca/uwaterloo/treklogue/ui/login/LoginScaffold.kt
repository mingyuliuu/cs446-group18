package ca.uwaterloo.treklogue.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.ui.theme.Blue
import ca.uwaterloo.treklogue.ui.theme.BtnBlue

private const val USABLE_WIDTH = 0.8F

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun LoginScaffold(loginViewModel: LoginViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(R.drawable.login_background),
                contentScale = ContentScale.FillBounds
            ),
        color = Color.Transparent // Set transparent color as the background is set using Modifier.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .border(BorderStroke(2.dp, BtnBlue))
            )
            Spacer(modifier = Modifier.weight(3f))
        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .fillMaxWidth()
        ) {
            Column {
                // Email field
                TextField(
                    enabled = loginViewModel.state.value.enabled,
                    modifier = Modifier.fillMaxWidth(USABLE_WIDTH),
                    value = loginViewModel.state.value.email,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    maxLines = 2,
                    onValueChange = {
                        loginViewModel.setEmail(it)
                    },
                    label = { Text(stringResource(R.string.prompt_email)) },
                )

                // Password field
                TextField(
                    enabled = loginViewModel.state.value.enabled,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(USABLE_WIDTH),
                    value = loginViewModel.state.value.password,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    maxLines = 2,
                    onValueChange = {
                        loginViewModel.setPassword(it)
                    },
                    label = { Text(stringResource(R.string.prompt_password)) })

                Spacer(modifier = Modifier.height(40.dp))

                // Login/create account button
                Button(
                    enabled = loginViewModel.state.value.enabled,
                    colors = ButtonDefaults.buttonColors(containerColor = BtnBlue),
                    modifier = Modifier.fillMaxWidth(USABLE_WIDTH),
                    onClick = {
                        val state = loginViewModel.state.value
                        when (state.action) {
                            LoginAction.LOGIN -> loginViewModel.login(
                                state.email,
                                state.password
                            )

                            LoginAction.CREATE_ACCOUNT -> loginViewModel.createAccount(
                                state.email,
                                state.password
                            )
                        }
                    }) {
                    val actionText = when (loginViewModel.state.value.action) {
                        LoginAction.CREATE_ACCOUNT -> stringResource(R.string.create_account)
                        LoginAction.LOGIN -> stringResource(R.string.log_in)
                    }
                    Text(actionText)
                }

                // Switch between login and create user
                TextButton(
                    onClick = {
                        val state = loginViewModel.state.value
                        when (state.action) {
                            LoginAction.LOGIN -> loginViewModel.switchToAction(LoginAction.CREATE_ACCOUNT)
                            LoginAction.CREATE_ACCOUNT -> loginViewModel.switchToAction(LoginAction.LOGIN)
                        }
                    }
                ) {
                    val actionText = when (loginViewModel.state.value.action) {
                        LoginAction.CREATE_ACCOUNT -> stringResource(R.string.already_have_account)
                        LoginAction.LOGIN -> stringResource(R.string.does_not_have_account)
                    }
                    Text(
                        text = actionText,
                        modifier = Modifier.fillMaxWidth(USABLE_WIDTH),
                        textAlign = TextAlign.Center,
                        color = Blue
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
                // Text with clarification on Atlas Cloud account vs Device Sync account
                Text(
                    text = stringResource(R.string.account_clarification),
                    modifier = Modifier.fillMaxWidth(USABLE_WIDTH),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}