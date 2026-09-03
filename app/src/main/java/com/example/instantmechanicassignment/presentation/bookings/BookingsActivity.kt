package com.example.instantmechanicassignment.presentation.bookings

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instantmechanicassignment.R
import com.example.instantmechanicassignment.data.local.PreferenceManager
import com.example.instantmechanicassignment.data.remote.RetrofitInstance
import com.example.instantmechanicassignment.data.repository.BookingRepositoryImpl
import com.example.instantmechanicassignment.model.Booking
import com.example.instantmechanicassignment.presentation.home.HomeActivity
import com.example.instantmechanicassignment.presentation.messages.MessagesActivity
import com.example.instantmechanicassignment.presentation.profile.ProfileActivity
import com.example.instantmechanicassignment.ui.theme.InstantMechanicAssignmentTheme
import androidx.compose.ui.tooling.preview.Preview
import com.example.instantmechanicassignment.model.*

class BookingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preferenceManager = remember { PreferenceManager(applicationContext) }
            val themeMode by preferenceManager.getThemeMode().collectAsState(initial = "system")

            InstantMechanicAssignmentTheme(themeMode = themeMode) {
                val repository = BookingRepositoryImpl(RetrofitInstance.apiService)
                val viewModel: BookingViewModel = viewModel(
                    factory = BookingViewModel.Factory(repository)
                )
                val uiState by viewModel.uiState.collectAsState()

                BookingsScreen(
                    uiState = uiState,
                    onBookingClick = {
                        // In a real app, passing booking ID would be here
                    },
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
fun BookingsScreen(
    uiState: BookingUiState,
    onBookingClick: () -> Unit,
    onNavClick: (Class<*>) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Upcoming", "History")

    Scaffold(
        topBar = {
            TopAppBar(
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
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.shadow(2.dp)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Explore, contentDescription = "Explore") },
                    label = { Text("Explore") },
                    selected = false,
                    onClick = { onNavClick(HomeActivity::class.java) }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Bookings"
                        )
                    },
                    label = { Text("Bookings") },
                    selected = true,
                    onClick = { /* Already here */ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Outlined.Chat, contentDescription = "Messages") },
                    label = { Text("Messages") },
                    selected = false,
                    onClick = { onNavClick(MessagesActivity::class.java) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { onNavClick(ProfileActivity::class.java) }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "My Bookings",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                color = MaterialTheme.colorScheme.primary
            )

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                divider = { HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        }
                    )
                }
            }

            when (val state = uiState) {
                is BookingUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is BookingUiState.Success -> {
                    val filteredBookings = if (selectedTab == 0) {
                        state.bookings.filter { it.bookingStatus == "Confirmed" || it.bookingStatus == "Pending" }
                    } else {
                        state.bookings.filter { it.bookingStatus == "Completed" || it.bookingStatus == "Cancelled" }
                    }

                    if (filteredBookings.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No ${tabs[selectedTab].lowercase()} bookings")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredBookings) { booking ->
                                BookingCard(booking, onClick = onBookingClick)
                            }
                        }
                    }
                }
                is BookingUiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No bookings found")
                    }
                }
                is BookingUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}


@Composable
fun BookingCard(booking: Booking, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = booking.mechanic.garageName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = booking.mechanic.services.firstOrNull()?.serviceName ?: "General Service",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                Surface(
                    color = when(booking.bookingStatus) {
                        "Confirmed" -> MaterialTheme.colorScheme.primary
                        "Pending" -> Color(0xFFFFA000)
                        "Completed" -> Color(0xFF4CAF50)
                        "Cancelled" -> Color(0xFFF44336)
                        else -> MaterialTheme.colorScheme.outline
                    },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = booking.bookingStatus,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = booking.appointmentDate,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = booking.appointmentTime,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Details", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingsScreenPreview() {
    InstantMechanicAssignmentTheme {
        val mockWorkingHours = WorkingHours(
            monday = "9:00 AM - 6:00 PM",
            tuesday = "9:00 AM - 6:00 PM",
            wednesday = "9:00 AM - 6:00 PM",
            thursday = "9:00 AM - 6:00 PM",
            friday = "9:00 AM - 6:00 PM",
            saturday = "9:00 AM - 1:00 PM",
            sunday = "Closed"
        )
        val mockMechanic = Mechanic(
            mechanicId = "1",
            garageName = "Perfect Garage",
            imageUrl = "",
            rating = 4.5,
            reviewCount = 100,
            distance = 2.5,
            location = "New York",
            address = "123 Main St",
            phoneNumber = "123-456-7890",
            isOpen = true,
            openingHours = mockWorkingHours,
            services = listOf(Service("1", "Oil Change", "", 50.0, "1 hour")),
            latitude = 0.0,
            longitude = 0.0
        )
        val mockVehicle = Vehicle(
            vehicleId = "1",
            registrationNumber = "ABC-1234",
            company = "Toyota",
            model = "Camry",
            manufactureYear = 2020,
            fuelType = "Gasoline",
            color = "Silver"
        )
        val mockBooking = Booking(
            bookingId = "1",
            requestId = "req1",
            mechanic = mockMechanic,
            vehicle = mockVehicle,
            appointmentDate = "2023-10-27",
            appointmentTime = "10:00 AM",
            estimatedCost = 50.0,
            bookingStatus = "Confirmed"
        )
        
        BookingsScreen(
            uiState = BookingUiState.Success(listOf(mockBooking)),
            onBookingClick = {},
            onNavClick = {}
        )
    }
}
