package com.example.instantmechanicassignment.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instantmechanicassignment.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instantmechanicassignment.data.local.PreferenceManager
import com.example.instantmechanicassignment.data.remote.RetrofitInstance
import com.example.instantmechanicassignment.data.repository.MechanicRepositoryImpl
import com.example.instantmechanicassignment.presentation.bookings.BookingsActivity
import com.example.instantmechanicassignment.presentation.detail.GarageDetailActivity
import com.example.instantmechanicassignment.presentation.messages.MessagesActivity
import com.example.instantmechanicassignment.presentation.profile.ProfileActivity
import com.example.instantmechanicassignment.ui.theme.InstantMechanicAssignmentTheme
import com.example.instantmechanicassignment.model.Mechanic
import com.example.instantmechanicassignment.model.Service
import com.example.instantmechanicassignment.model.WorkingHours


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preferenceManager = remember { PreferenceManager(applicationContext) }
            val themeMode by preferenceManager.getThemeMode().collectAsState(initial = "system")

            InstantMechanicAssignmentTheme(themeMode = themeMode) {
                val repository = MechanicRepositoryImpl(RetrofitInstance.apiService)
                val viewModel: HomeViewModel = viewModel(
                    factory = HomeViewModel.Factory(repository)
                )
                
                HomeScreen(
                    viewModel = viewModel,
                    onMechanicClick = { mechanicId ->
                        val intent = Intent(this, GarageDetailActivity::class.java).apply {
                            putExtra("MECHANIC_ID", mechanicId)
                        }
                        startActivity(intent)
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
fun HomeScreen(
    viewModel: HomeViewModel,
    onMechanicClick: (String) -> Unit,
    onNavClick: (Class<*>) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf("") }

    HomeContent(
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChange = { 
            searchQuery = it
            viewModel.search(it)
        },
        selectedService = selectedService,
        onServiceSelected = { 
            selectedService = it
            viewModel.filter(it)
        },
        onMechanicClick = onMechanicClick,
        onNavClick = onNavClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedService: String,
    onServiceSelected: (String) -> Unit,
    onMechanicClick: (String) -> Unit,
    onNavClick: (Class<*>) -> Unit
) {
    val services = listOf("Oil Change", "Brake Repair", "Transmission", "Tires", "Battery", "Diagnostics")

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
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSurface
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
                            .clickable { onNavClick(ProfileActivity::class.java) }
                    ) {
                        // Placeholder for profile image
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already here */ },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
                    label = { Text("Explore") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
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
                    icon = { Icon(Icons.Outlined.Message, contentDescription = "Messages") },
                    label = { Text("Messages") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavClick(ProfileActivity::class.java) },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search and Filter Section
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        placeholder = { Text("Search mechanics, garages...", fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Filters", color = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Service Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(services) { service ->
                        FilterChip(
                            selected = selectedService == service,
                            onClick = { onServiceSelected(service) },
                            label = { Text(service) },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = null
                        )
                    }
                }
            }

            // Mechanic List
            when (uiState) {
                is HomeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is HomeUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.mechanics) { mechanic ->
                            MechanicCard(
                                name = mechanic.garageName,
                                rating = mechanic.rating,
                                reviews = mechanic.reviewCount,
                                distance = "${mechanic.distance} km",
                                location = mechanic.location,
                                services = mechanic.services.map { it.serviceName },
                                isOpen = mechanic.isOpen,
                                onClick = { onMechanicClick(mechanic.mechanicId) }
                            )
                        }
                    }
                }
                is HomeUiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No mechanics found")
                    }
                }
                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${uiState.message}", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun MechanicCard(
    name: String,
    rating: Double,
    reviews: Int,
    distance: String,
    location: String,
    services: List<String>,
    isOpen: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB400),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = " $rating",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = " ($reviews Reviews)",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                }

                if (isOpen) {
                    Surface(
                        color = Color(0xFFE8F5E9).copy(alpha = if (isSystemInDarkTheme()) 0.2f else 1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Open",
                            color = if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF2E7D32),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$distance • $location",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Service Chips in Card
            FlowRow(
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp
            ) {
                services.forEach { service ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = service,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Book Appointment", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Simple FlowRow implementation since standard FlowRow might need experimental annotation or specific version
@Composable
fun FlowRow(
    mainAxisSpacing: androidx.compose.ui.unit.Dp,
    crossAxisSpacing: androidx.compose.ui.unit.Dp,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.layout.Layout(content = content) { measurables, constraints ->
        val placeholders = measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }
        val spacingPx = mainAxisSpacing.roundToPx()
        val crossSpacingPx = crossAxisSpacing.roundToPx()

        val lines = mutableListOf<List<androidx.compose.ui.layout.Placeable>>()
        var currentLine = mutableListOf<androidx.compose.ui.layout.Placeable>()
        var currentLineWidth = 0

        placeholders.forEach { placeable ->
            if (currentLineWidth + placeable.width > constraints.maxWidth && currentLine.isNotEmpty()) {
                lines.add(currentLine)
                currentLine = mutableListOf()
                currentLineWidth = 0
            }
            currentLine.add(placeable)
            currentLineWidth += placeable.width + spacingPx
        }
        if (currentLine.isNotEmpty()) lines.add(currentLine)

        val height = lines.sumOf { it.maxOf { p -> p.height } } + (lines.size - 1).coerceAtLeast(0) * crossSpacingPx
        layout(constraints.maxWidth, height) {
            var y = 0
            lines.forEach { line ->
                var x = 0
                val lineHeight = line.maxOf { it.height }
                line.forEach { placeable ->
                    placeable.place(x, y)
                    x += placeable.width + spacingPx
                }
                y += lineHeight + crossSpacingPx
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val mockWorkingHours = WorkingHours(
        monday = "09:00 - 18:00",
        tuesday = "09:00 - 18:00",
        wednesday = "09:00 - 18:00",
        thursday = "09:00 - 18:00",
        friday = "09:00 - 18:00",
        saturday = "10:00 - 16:00",
        sunday = "Closed"
    )

    val mockMechanics = listOf(
        Mechanic(
            mechanicId = "1",
            garageName = "Expert Auto Care",
            imageUrl = "",
            rating = 4.8,
            reviewCount = 124,
            distance = 2.5,
            location = "Downtown",
            address = "123 Main St",
            phoneNumber = "555-0101",
            isOpen = true,
            openingHours = mockWorkingHours,
            services = listOf(
                Service("1", "Oil Change", "", 50.0, "30 mins"),
                Service("2", "Brake Repair", "", 150.0, "2 hours")
            ),
            latitude = 0.0,
            longitude = 0.0
        ),
        Mechanic(
            mechanicId = "2",
            garageName = "Precision Motors",
            imageUrl = "",
            rating = 4.5,
            reviewCount = 89,
            distance = 4.2,
            location = "Westside",
            address = "456 West Ave",
            phoneNumber = "555-0102",
            isOpen = false,
            openingHours = mockWorkingHours,
            services = listOf(
                Service("3", "Transmission", "", 500.0, "1 day"),
                Service("4", "Tires", "", 100.0, "1 hour")
            ),
            latitude = 0.0,
            longitude = 0.0
        )
    )

    InstantMechanicAssignmentTheme {
        HomeContent(
            uiState = HomeUiState.Success(mockMechanics),
            searchQuery = "",
            onSearchQueryChange = {},
            selectedService = "Oil Change",
            onServiceSelected = {},
            onMechanicClick = {},
            onNavClick = {}
        )
    }
}
