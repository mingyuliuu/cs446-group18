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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
                                painter = painterResource(id = drawable.view_icon),
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

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "*By using this app, you agree to our ",
                    modifier = Modifier.fillMaxWidth(USABLE_WIDTH),
                    textAlign = TextAlign.Center,
                    color = Blue400
                )


                    Text(
                        text = "Terms and Conditions",
                        modifier = Modifier
                            .clickable {
                                showPopup = true
                            }
                            .fillMaxWidth(USABLE_WIDTH),
                        textAlign = TextAlign.Center,
                        color = Blue400
                    )


                Spacer(modifier = Modifier.height(90.dp))
                // Text with clarification on Atlas Cloud account vs Device Sync account
//                Text(
//                    text = stringResource(R.string.account_clarification),
//                    modifier = Modifier.fillMaxWidth(USABLE_WIDTH),
//                    textAlign = TextAlign.Center,
//                    fontSize = 12.sp
//                )
//                Spacer(modifier = Modifier.height(30.dp))
            }
            PopupBox(popupWidth = 200F,
                popupHeight = 300F,
                showPopup = showPopup,
                onClickOutside = {showPopup = false},
                content = {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
                            .fillMaxHeight(0.75f)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Treklogue - Terms and Conditions",
                                textAlign = TextAlign.Center,
                                color = Blue400,
                            )
                            Text(text = "Welcome to Treklogue! These Terms and Conditions govern your use of the Treklogue Travel Journal App. By accessing or using Treklogue, you agree to comply with these Terms and Conditions. Please read them carefully.\n" +
                                    "\n" +
                                    "Community Guidelines:\n" +
                                    "\n" +
                                    "Treklogue encourages responsible travel practices. Users are expected to adhere to community guidelines aimed at preserving the environment and respecting local communities when visiting landmarks. This includes but is not limited to:\n" +
                                    "Proper disposal of waste and litter.\n" +
                                    "Respect for local customs, traditions, and cultural sensitivities.\n" +
                                    "Consideration for wildlife and natural habitats.\n" +
                                    "Minimization of environmental impact.\n" +
                                    "Users found violating these guidelines may face penalties, including account suspension or termination.\n" +
                                    "Risk Acknowledgment:\n" +
                                    "\n" +
                                    "Users acknowledge and accept that Treklogue is a self-recording travel journal app. As such, users engage with the app at their own risk.\n" +
                                    "Treklogue does not assume responsibility for any accidents, injuries, or damages that may occur while using the app, including but not limited to physical harm, data loss, or any adverse consequences resulting from user actions.\n" +
                                    "Users are responsible for their own safety and well-being while using Treklogue, including assessing their capabilities and exercising caution in unfamiliar environments.\n" +
                                    "Inclusivity and Accessibility:\n" +
                                    "\n" +
                                    "Treklogue is committed to inclusivity and accessibility. We strive to accommodate the diverse needs and abilities of all users.\n" +
                                    "While we endeavor to create an inclusive platform, users acknowledge that Treklogue may not fully address the needs of every individual or group.\n" +
                                    "Users with specific accessibility requirements are encouraged to provide feedback to help us improve the app's accessibility features.\n" +
                                    "User Conduct:\n" +
                                    "\n" +
                                    "Users are prohibited from engaging in any activity that may:\n" +
                                    "Violate applicable laws or regulations.\n" +
                                    "Infringe upon the rights of others.\n" +
                                    "Cause harm to individuals, communities, or the environment.\n" +
                                    "Users must respect the privacy and intellectual property rights of others.\n" +
                                    "Treklogue reserves the right to remove any content or suspend accounts that violate these Terms and Conditions.\n" +
                                    "Feedback and Suggestions:\n" +
                                    "\n" +
                                    "Treklogue values user feedback and suggestions for improvement. Users are encouraged to provide feedback and report any issues or concerns.\n" +
                                    "By submitting feedback or suggestions, users grant Treklogue the right to use and implement such feedback without any obligation or compensation.\n" +
                                    "Modification of Terms:\n" +
                                    "\n" +
                                    "Treklogue reserves the right to modify these Terms and Conditions at any time. Updated terms will be effective upon posting.\n" +
                                    "Users are responsible for regularly reviewing the Terms and Conditions for any changes.\n" +
                                    "Continued use of Treklogue after the posting of modified terms constitutes acceptance of the changes.\n" +
                                    "Governing Law:\n" +
                                    "\n" +
                                    "These Terms and Conditions shall be governed by and construed in accordance with the laws of [Jurisdiction].\n" +
                                    "Any disputes arising from or relating to these Terms and Conditions shall be subject to the exclusive jurisdiction of the courts of [Jurisdiction].\n" +
                                    "By using Treklogue, you agree to abide by these Terms and Conditions. If you do not agree with any part of these terms, you must not use Treklogue. If you have any questions or concerns regarding these Terms and Conditions, please contact us at [contact email]. Thank you for using Treklogue!Welcome to Treklogue! These Terms and Conditions govern your use of the Treklogue Travel Journal App. By accessing or using Treklogue, you agree to comply with these Terms and Conditions. Please read them carefully.\n" +
                                    "\n" +
                                    "Community Guidelines:\n" +
                                    "\n" +
                                    "Treklogue encourages responsible travel practices. Users are expected to adhere to community guidelines aimed at preserving the environment and respecting local communities when visiting landmarks. This includes but is not limited to:\n" +
                                    "Proper disposal of waste and litter.\n" +
                                    "Respect for local customs, traditions, and cultural sensitivities.\n" +
                                    "Consideration for wildlife and natural habitats.\n" +
                                    "Minimization of environmental impact.\n" +
                                    "Users found violating these guidelines may face penalties, including account suspension or termination.\n" +
                                    "Risk Acknowledgment:\n" +
                                    "\n" +
                                    "Users acknowledge and accept that Treklogue is a self-recording travel journal app. As such, users engage with the app at their own risk.\n" +
                                    "Treklogue does not assume responsibility for any accidents, injuries, or damages that may occur while using the app, including but not limited to physical harm, data loss, or any adverse consequences resulting from user actions.\n" +
                                    "Users are responsible for their own safety and well-being while using Treklogue, including assessing their capabilities and exercising caution in unfamiliar environments.\n" +
                                    "Inclusivity and Accessibility:\n" +
                                    "\n" +
                                    "Treklogue is committed to inclusivity and accessibility. We strive to accommodate the diverse needs and abilities of all users.\n" +
                                    "While we endeavor to create an inclusive platform, users acknowledge that Treklogue may not fully address the needs of every individual or group.\n" +
                                    "Users with specific accessibility requirements are encouraged to provide feedback to help us improve the app's accessibility features.\n" +
                                    "User Conduct:\n" +
                                    "\n" +
                                    "Users are prohibited from engaging in any activity that may:\n" +
                                    "Violate applicable laws or regulations.\n" +
                                    "Infringe upon the rights of others.\n" +
                                    "Cause harm to individuals, communities, or the environment.\n" +
                                    "Users must respect the privacy and intellectual property rights of others.\n" +
                                    "Treklogue reserves the right to remove any content or suspend accounts that violate these Terms and Conditions.\n" +
                                    "Feedback and Suggestions:\n" +
                                    "\n" +
                                    "Treklogue values user feedback and suggestions for improvement. Users are encouraged to provide feedback and report any issues or concerns.\n" +
                                    "By submitting feedback or suggestions, users grant Treklogue the right to use and implement such feedback without any obligation or compensation.\n" +
                                    "Modification of Terms:\n" +
                                    "\n" +
                                    "Treklogue reserves the right to modify these Terms and Conditions at any time. Updated terms will be effective upon posting.\n" +
                                    "Users are responsible for regularly reviewing the Terms and Conditions for any changes.\n" +
                                    "Continued use of Treklogue after the posting of modified terms constitutes acceptance of the changes.\n" +
                                    "Governing Law:\n" +
                                    "\n" +
                                    "These Terms and Conditions shall be governed by and construed in accordance with the laws of Canada.\n" +
                                    "Any disputes arising from or relating to these Terms and Conditions shall be subject to the exclusive jurisdiction of the courts of Canada.\n" +
                                    "By using Treklogue, you agree to abide by these Terms and Conditions. If you do not agree with any part of these terms, you must not use Treklogue. If you have any questions or concerns regarding these Terms and Conditions, please contact us at treklogue@gmail.com. Thank you for using Treklogue!",
                                textAlign = TextAlign.Left,
                                color = Blue400,)

                        }
                    }
                    
                })

        }
    }
}