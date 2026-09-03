package com.example.instantmechanicassignment.presentation.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.instantmechanicassignment.presentation.bookings.BookingsActivity
import com.example.instantmechanicassignment.presentation.home.HomeActivity
import com.example.instantmechanicassignment.presentation.messages.MessagesActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instantmechanicassignment.R
import com.example.instantmechanicassignment.data.local.PreferenceManager
import com.example.instantmechanicassignment.data.remote.RetrofitInstance
import com.example.instantmechanicassignment.data.repository.AuthRepositoryImpl
import com.example.instantmechanicassignment.model.User
import com.example.instantmechanicassignment.presentation.login.LoginActivity
import com.example.instantmechanicassignment.ui.theme.InstantMechanicAssignmentTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preferenceManager = remember { PreferenceManager(applicationContext) }
            val authRepository = AuthRepositoryImpl(
                RetrofitInstance.apiService,
                preferenceManager
            )
            val viewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModel.Factory(authRepository, preferenceManager)
            )

            val themeMode by viewModel.themeMode.collectAsState()

            InstantMechanicAssignmentTheme(themeMode = themeMode) {
                val uiState by viewModel.uiState.collectAsState()
                val navigationEvent by viewModel.navigationEvent.collectAsState()

                LaunchedEffect(navigationEvent) {
                    when (navigationEvent) {
                        is ProfileNavigationEvent.NavigateToLogin -> {
                            val intent = Intent(this@ProfileActivity, LoginActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                            finish()
                        }
                        null -> {}
                    }
                }

                ProfileScreen(
                    uiState = uiState,
                    themeMode = themeMode,
                    onThemeChange = { viewModel.setThemeMode(it) },
                    onLogoutClick = { viewModel.logout() },
                    onNavClick = { activityClass ->
                        startActivity(Intent(this, activityClass))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    themeMode: String,
    onThemeChange: (String) -> Unit,
    onLogoutClick: () -> Unit,
    onNavClick: (Class<*>) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "InstantMechanic",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavClick(HomeActivity::class.java) },
                    icon = { Icon(Icons.Outlined.Explore, contentDescription = "Explore") },
                    label = { Text("Explore") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavClick(BookingsActivity::class.java) },
                    icon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = "Bookings") },
                    label = { Text("Bookings") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavClick(MessagesActivity::class.java) },
                    icon = { Icon(Icons.AutoMirrored.Outlined.Message, contentDescription = "Messages") },
                    label = { Text("Messages") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already here */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { paddingValues ->
        when (uiState) {
            is ProfileUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ProfileUiState.Success -> {
                ProfileContent(
                    user = uiState.user,
                    themeMode = themeMode,
                    onThemeChange = onThemeChange,
                    paddingValues = paddingValues,
                    onLogoutClick = onLogoutClick
                )
            }
            is ProfileUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    user: User,
    themeMode: String,
    onThemeChange: (String) -> Unit,
    paddingValues: PaddingValues,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        // Profile Section
        ProfileHeaderSection(user)

        Spacer(modifier = Modifier.height(24.dp))

        // My Account Section
        SettingsSection(
            title = "MY ACCOUNT",
            items = listOf(
                SettingsItemData(Icons.Default.Person, "Personal Information"),
                SettingsItemData(Icons.Default.DirectionsCar, "My Vehicles"),
                SettingsItemData(Icons.Default.CreditCard, "Payment Methods")
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Theme Section
        ThemeSelectionSection(themeMode, onThemeChange)

        Spacer(modifier = Modifier.height(24.dp))

        // App Settings Section
        SettingsSection(
            title = "APP SETTINGS",
            items = listOf(
                SettingsItemData(Icons.Default.Notifications, "Notifications"),
                SettingsItemData(Icons.Default.Lock, "Privacy"),
                SettingsItemData(Icons.AutoMirrored.Outlined.HelpOutline, "Help & Support")
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Logout Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, Color.Red),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Logout",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ThemeSelectionSection(currentMode: String, onThemeChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "THEME",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                ThemeRow("Light", currentMode == "light") { onThemeChange("light") }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                ThemeRow("Dark", currentMode == "dark") { onThemeChange("dark") }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                ThemeRow("System Default", currentMode == "system") { onThemeChange("system") }
            }
        }
    }
}

@Composable
fun ThemeRow(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        RadioButton(selected = isSelected, onClick = onClick)
    }
}

@Composable
fun ProfileHeaderSection(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(60.dp).align(Alignment.Center),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = user.fullName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = user.email,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

data class SettingsItemData(
    val icon: ImageVector,
    val title: String
)

@Composable
fun SettingsSection(title: String, items: List<SettingsItemData>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingsRow(item)
                    if (index < items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRow(item: SettingsItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    InstantMechanicAssignmentTheme {
        ProfileScreen(
            uiState = ProfileUiState.Success(
                User(
                    userId = "1",
                    fullName = "Pranav Yadav",
                    email = "pranav@example.com",
                    phoneNumber = "123-456-7890",
                    city = "New Delhi",
                    profileImage = "",
                    createdAt = "2023-01-01"
                )
            ),
            themeMode = "system",
            onThemeChange = {},
            onLogoutClick = {},
            onNavClick = {}
        )
    }
}
