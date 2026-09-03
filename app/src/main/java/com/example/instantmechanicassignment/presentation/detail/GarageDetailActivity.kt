package com.example.instantmechanicassignment.presentation.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instantmechanicassignment.R
import com.example.instantmechanicassignment.data.remote.RetrofitInstance
import com.example.instantmechanicassignment.data.repository.MechanicRepositoryImpl
import com.example.instantmechanicassignment.model.Mechanic
import com.example.instantmechanicassignment.model.Service
import com.example.instantmechanicassignment.model.WorkingHours
import com.example.instantmechanicassignment.presentation.request.RequestServiceActivity
import com.example.instantmechanicassignment.ui.theme.InstantMechanicAssignmentTheme

class GarageDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mechanicId = intent.getStringExtra("MECHANIC_ID") ?: "M101"
        
        setContent {
            InstantMechanicAssignmentTheme {
                val repository = MechanicRepositoryImpl(RetrofitInstance.apiService)
                val viewModel: MechanicDetailsViewModel = viewModel(
                    factory = MechanicDetailsViewModel.Factory(repository)
                )
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(mechanicId) {
                    viewModel.loadMechanic(mechanicId)
                }

                when (val state = uiState) {
                    is MechanicDetailsUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = colorResource(id = R.color.blue))
                        }
                    }
                    is MechanicDetailsUiState.Success -> {
                        GarageDetailScreen(
                            mechanic = state.mechanic,
                            onBackClick = { finish() },
                            onRequestServiceClick = {
                                startActivity(Intent(this@GarageDetailActivity, RequestServiceActivity::class.java))
                            },
                            onCallClick = { viewModel.handleCall(state.mechanic.phoneNumber) },
                            onDirectionsClick = { viewModel.handleDirections(state.mechanic.latitude, state.mechanic.longitude) }
                        )
                    }
                    is MechanicDetailsUiState.Error -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = state.message, color = Color.Red)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.retry(mechanicId) }) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

val sampleWorkingHours = WorkingHours(
    monday = "8:00 AM - 6:00 PM",
    tuesday = "8:00 AM - 6:00 PM",
    wednesday = "8:00 AM - 6:00 PM",
    thursday = "8:00 AM - 6:00 PM",
    friday = "8:00 AM - 6:00 PM",
    saturday = "9:00 AM - 2:00 PM",
    sunday = "Closed"
)

val sampleServices = listOf(
    Service("S01", "Oil Change", "Build", 1200.0, "45 mins"),
    Service("S02", "Tire Rotation", "Settings", 800.0, "30 mins"),
    Service("S03", "Brake Repair", "Handyman", 2500.0, "2 hours"),
    Service("S04", "General Diagnostics", "Search", 1500.0, "1 hour")
)

val sampleMechanic = Mechanic(
    mechanicId = "M101",
    garageName = "Apex Auto Care",
    imageUrl = "",
    rating = 4.8,
    reviewCount = 124,
    distance = 1.2,
    location = "Downtown",
    address = "123 Mechanics Way, Auto District, Cityville, ST 12345",
    phoneNumber = "+91 9988776655",
    isOpen = true,
    openingHours = sampleWorkingHours,
    services = sampleServices,
    latitude = 12.9716,
    longitude = 77.5946
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GarageDetailScreen(
    mechanic: Mechanic,
    onBackClick: () -> Unit,
    onRequestServiceClick: () -> Unit,
    onCallClick: () -> Unit,
    onDirectionsClick: () -> Unit
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
                            color = colorResource(id = R.color.dark_blue),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Button(
                    onClick = onRequestServiceClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800) // Orange color
                    )
                ) {
                    Text(
                        text = "Request Service",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF8F9FA))
        ) {
            // Garage Image Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
            ) {
                // Placeholder for actual image
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center),
                    tint = Color.Gray
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Garage Information Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = mechanic.garageName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.dark_blue)
                    )
                    Surface(
                        color = if (mechanic.isOpen) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (mechanic.isOpen) "Open Now" else "Closed",
                            color = if (mechanic.isOpen) Color(0xFF2E7D32) else Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB400),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = " ${mechanic.rating}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = " (${mechanic.reviewCount} Reviews)",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = mechanic.address,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Contact Section
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onCallClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.blue)
                        )
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(mechanic.phoneNumber)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    OutlinedButton(
                        onClick = onDirectionsClick,
                        modifier = Modifier
                            .weight(0.4f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, colorResource(id = R.color.blue))
                    ) {
                        Icon(
                            Icons.Default.Navigation,
                            contentDescription = "Directions",
                            tint = colorResource(id = R.color.blue)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Available Services Card
                Text(
                    text = "Available Services",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        mechanic.services.forEachIndexed { index, service ->
                            val icon = when(service.icon) {
                                "Build" -> Icons.Default.Build
                                "Settings" -> Icons.Default.Settings
                                "Handyman" -> Icons.Default.Handyman
                                "Search" -> Icons.Default.Search
                                else -> Icons.Default.Build
                            }
                            ServiceRow(icon, service.serviceName)
                            if (index < mechanic.services.size - 1) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFEEEEEE))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Working Hours Card
                Text(
                    text = "Working Hours",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        WorkingHourRow("Monday", mechanic.openingHours.monday)
                        WorkingHourRow("Tuesday", mechanic.openingHours.tuesday)
                        WorkingHourRow("Wednesday", mechanic.openingHours.wednesday)
                        WorkingHourRow("Thursday", mechanic.openingHours.thursday)
                        WorkingHourRow("Friday", mechanic.openingHours.friday)
                        WorkingHourRow("Saturday", mechanic.openingHours.saturday)
                        WorkingHourRow("Sunday", mechanic.openingHours.sunday, isClosed = mechanic.openingHours.sunday == "Closed")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Map Preview
                Text(
                    text = "Location",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                ) {
                    // Placeholder for Map
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center),
                        tint = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ServiceRow(icon: ImageVector, name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorResource(id = R.color.blue),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = name, fontSize = 16.sp)
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun WorkingHourRow(day: String, hours: String, isClosed: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = day, color = Color.Gray, fontSize = 14.sp)
        Text(
            text = hours,
            color = if (isClosed) Color.Red else Color.Black,
            fontSize = 14.sp,
            fontWeight = if (isClosed) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GarageDetailScreenPreview() {
    InstantMechanicAssignmentTheme {
        GarageDetailScreen(
            mechanic = sampleMechanic,
            onBackClick = {},
            onRequestServiceClick = {},
            onCallClick = {},
            onDirectionsClick = {}
        )
    }
}
