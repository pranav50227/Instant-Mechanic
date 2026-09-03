package com.example.instantmechanicassignment.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instantmechanicassignment.presentation.login.LoginActivity
import com.example.instantmechanicassignment.R
import com.example.instantmechanicassignment.ui.theme.InstantMechanicAssignmentTheme
import kotlinx.coroutines.delay

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instantmechanicassignment.data.local.PreferenceManager
import com.example.instantmechanicassignment.data.remote.RetrofitInstance
import com.example.instantmechanicassignment.data.repository.AuthRepositoryImpl
import com.example.instantmechanicassignment.presentation.home.HomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstantMechanicAssignmentTheme {
                val authRepository = AuthRepositoryImpl(
                    RetrofitInstance.apiService,
                    PreferenceManager(applicationContext)
                )
                val viewModel: SplashViewModel = viewModel(
                    factory = SplashViewModel.Factory(authRepository)
                )
                val state by viewModel.state.collectAsState()

                LaunchedEffect(state) {
                    when (state) {
                        is SplashState.NavigateToHome -> {
                            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                            finish()
                        }
                        is SplashState.NavigateToLogin -> {
                            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                            finish()
                        }
                        else -> {}
                    }
                }

                SplashScreen()
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = colorResource(id = R.color.blue),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Name
            Text(
                text = "InstantMechanic",
                color = colorResource(id = R.color.dark_blue),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Loading Indicator (Three-dot style placeholder)
            LoadingDots()

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Expert assistance, arriving shortly",
                color = colorResource(id = R.color.gray),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LoadingDots() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) {
            Surface(
                modifier = Modifier.size(6.dp),
                shape = RoundedCornerShape(50),
                color = colorResource(id = R.color.gray)
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    InstantMechanicAssignmentTheme {
        SplashScreen()
    }
}
