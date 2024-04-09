package ca.uwaterloo.treklogue.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.R.drawable
import ca.uwaterloo.treklogue.ui.composables.PopupBox
import ca.uwaterloo.treklogue.ui.theme.Blue200
import ca.uwaterloo.treklogue.ui.theme.Blue400
import ca.uwaterloo.treklogue.ui.viewModels.LoginAction
import ca.uwaterloo.treklogue.ui.viewModels.LoginViewModel

private const val USABLE_WIDTH = 0.8F

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun LoginScreen(loginViewModel: LoginViewModel) {
    var passwordVisible by remember { mutableStateOf(false) }
    var showPopup by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(drawable.img_login_bg),
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
                    .border(BorderStroke(2.dp, Blue200))
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
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = drawable.email_icon),
                            contentDescription = "Email",
                            tint = Color.Gray,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                )

                // Password field
                TextField(
                    enabled = loginViewModel.state.value.enabled,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                    label = { Text(stringResource(R.string.prompt_password)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = drawable.password_icon),
                            contentDescription = "Password",
                            tint = Color.Gray,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            Icon(
                                painter = painterResource(id = if (passwordVisible) drawable.view_icon else drawable.hide_view_icon),
                                contentDescription = "View",
                                tint = Color.Gray,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    })

                Spacer(modifier = Modifier.height(40.dp))

                // Login/create account button
                Button(
                    enabled = loginViewModel.state.value.enabled,
                    colors = ButtonDefaults.buttonColors(containerColor = Blue200),
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
                        color = Blue400
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = stringResource(R.string.agreement),
                    modifier = Modifier.fillMaxWidth(USABLE_WIDTH),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = stringResource(R.string.terms_and_conditions_click),
                    modifier = Modifier
                        .clickable {
                            showPopup = true
                        }
                        .fillMaxWidth(USABLE_WIDTH),
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                )

                Spacer(modifier = Modifier.height(80.dp))
            }

            PopupBox(
                widthFraction = 0.8f,
                heightFraction = 0.8f,
                showPopup = showPopup,
                onClickOutside = { showPopup = false },
                content = {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.terms_and_conditions_title),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            color = Blue400,
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(R.string.terms_and_conditions),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Left,
                        )
                    }
                }
            )
        }
    }
}