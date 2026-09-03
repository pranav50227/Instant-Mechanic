package com.example.instantmechanicassignment.presentation.signup

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instantmechanicassignment.R
import com.example.instantmechanicassignment.data.local.PreferenceManager
import com.example.instantmechanicassignment.data.remote.RetrofitInstance
import com.example.instantmechanicassignment.data.repository.AuthRepositoryImpl
import com.example.instantmechanicassignment.presentation.home.HomeActivity
import com.example.instantmechanicassignment.presentation.login.LoginActivity
import com.example.instantmechanicassignment.ui.theme.InstantMechanicAssignmentTheme

class SignupActivity : ComponentActivity() {
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
                val viewModel: SignupViewModel = viewModel(
                    factory = SignupViewModel.Factory(authRepository)
                )

                val fullName by viewModel.fullName.collectAsState()
                val email by viewModel.email.collectAsState()
                val phone by viewModel.phoneNumber.collectAsState()
                val city by viewModel.city.collectAsState()
                val password by viewModel.password.collectAsState()
                val isSignupEnabled by viewModel.isSignupEnabled.collectAsState()
                val signupState by viewModel.signupState.collectAsState()

                LaunchedEffect(signupState) {
                    if (signupState is SignupState.Success) {
                        startActivity(Intent(this@SignupActivity, HomeActivity::class.java))
                        finish()
                    }
                }

                SignupScreen(
                    fullName = fullName,
                    onFullNameChange = viewModel::updateName,
                    email = email,
                    onEmailChange = viewModel::updateEmail,
                    phone = phone,
                    onPhoneChange = viewModel::updatePhone,
                    city = city,
                    onCityChange = viewModel::updateCity,
                    password = password,
                    onPasswordChange = viewModel::updatePassword,
                    isSignupEnabled = isSignupEnabled,
                    signupState = signupState,
                    onSignupClick = { viewModel.register() },
                    onLoginClick = {
                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isSignupEnabled: Boolean,
    signupState: SignupState,
    onSignupClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "InstantMechanic",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Text(
                text = "Sign up to request immediate roadside assistance and mechanic services.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp),
                textAlign = TextAlign.Start
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = onFullNameChange,
                        label = { Text("Full Name") },
                        placeholder = { Text("John Doe") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        label = { Text("Email Address") },
                        placeholder = { Text("john@example.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Phone Number
                    OutlinedTextField(
                        value = phone,
                        onValueChange = onPhoneChange,
                        label = { Text("Phone Number") },
                        placeholder = { Text("(555) 123-4567") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // City
                    OutlinedTextField(
                        value = city,
                        onValueChange = onCityChange,
                        label = { Text("City") },
                        placeholder = { Text("Austin, TX") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Password
                    Column {
                        OutlinedTextField(
                            value = password,
                            onValueChange = onPasswordChange,
                            label = { Text("Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
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
                            shape = RoundedCornerShape(12.dp)
                        )
                        Text(
                            text = "Must be at least 8 characters.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sign Up Button
                    Button(
                        onClick = onSignupClick,
                        enabled = isSignupEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (signupState is SignupState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Sign Up",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }

                    if (signupState is SignupState.ApiError) {
                        Text(
                            text = signupState.message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Prompt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 14.sp
                )
                Text(
                    text = "Log in here",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Terms & Privacy
            Text(
                text = "By signing up, you agree to our Terms of Service and Privacy Policy.",
                color = MaterialTheme.colorScheme.outline,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignupScreenPreview() {
    InstantMechanicAssignmentTheme {
        SignupScreen(
            fullName = "John Doe",
            onFullNameChange = {},
            email = "john.doe@example.com",
            onEmailChange = {},
            phone = "123-456-7890",
            onPhoneChange = {},
            city = "New York",
            onCityChange = {},
            password = "Password123",
            onPasswordChange = {},
            isSignupEnabled = true,
            signupState = SignupState.Idle,
            onSignupClick = {},
            onLoginClick = {}
        )
    }
}
