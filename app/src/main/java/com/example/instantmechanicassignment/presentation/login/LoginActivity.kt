package com.example.instantmechanicassignment.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instantmechanicassignment.MainActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instantmechanicassignment.R
import com.example.instantmechanicassignment.data.local.PreferenceManager
import com.example.instantmechanicassignment.data.remote.RetrofitInstance
import com.example.instantmechanicassignment.data.repository.AuthRepositoryImpl
import com.example.instantmechanicassignment.presentation.home.HomeActivity
import com.example.instantmechanicassignment.presentation.signup.SignupActivity
import com.example.instantmechanicassignment.ui.theme.InstantMechanicAssignmentTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preferenceManager = remember { PreferenceManager(applicationContext) }
            val themeMode by preferenceManager.getThemeMode().collectAsState(initial = "system")

            InstantMechanicAssignmentTheme(themeMode = themeMode) {
                val authRepository = AuthRepositoryImpl(
                    RetrofitInstance.apiService,
                    PreferenceManager(applicationContext)
                )
                val viewModel: LoginViewModel = viewModel(
                    factory = LoginViewModel.Factory(authRepository)
                )
                val email by viewModel.email.collectAsState()
                val password by viewModel.password.collectAsState()
                val isLoginEnabled by viewModel.isLoginEnabled.collectAsState()
                val loginState by viewModel.loginState.collectAsState()

                LaunchedEffect(loginState) {
                    if (loginState is LoginState.Success) {
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }
                }

                LoginScreen(
                    email = email,
                    onEmailChange = viewModel::onEmailChanged,
                    password = password,
                    onPasswordChange = viewModel::onPasswordChanged,
                    isLoginEnabled = isLoginEnabled,
                    loginState = loginState,
                    onLoginClick = { viewModel.login() },
                    onSignUpClick = {
                        startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoginEnabled: Boolean,
    loginState: LoginState,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // App Name
            Text(
                text = "InstantMechanic",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Subtitle
            Text(
                text = "Expert assistance, arriving shortly.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        label = { Text("Email Address") },
                        placeholder = { Text("name@example.com") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Forgot Password (Above password field, right aligned)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Forgot Password?",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickable { /* Forgot Password Logic */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                        },
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Default.Visibility
                            else Icons.Default.VisibilityOff

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = null)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login Button
                    Button(
                        onClick = onLoginClick,
                        enabled = isLoginEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (loginState is LoginState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Login",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (loginState is LoginState.Error) {
                        Text(
                            text = loginState.message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                Text(
                    text = " OR ",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 12.sp
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Signup Prompt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    InstantMechanicAssignmentTheme {
        LoginScreen(
            email = "john@example.com",
            onEmailChange = {},
            password = "password",
            onPasswordChange = {},
            isLoginEnabled = true,
            loginState = LoginState.Idle,
            onLoginClick = {},
            onSignUpClick = {}
        )
    }
}
