package com.example.instantmechanicassignment.presentation.request

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instantmechanicassignment.R
import com.example.instantmechanicassignment.data.local.PreferenceManager
import com.example.instantmechanicassignment.ui.theme.InstantMechanicAssignmentTheme

class RequestServiceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preferenceManager = remember { PreferenceManager(applicationContext) }
            val themeMode by preferenceManager.getThemeMode().collectAsState(initial = "system")

            InstantMechanicAssignmentTheme(themeMode = themeMode) {
                RequestServiceScreen(onBackClick = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestServiceScreen(onBackClick: () -> Unit) {
    var licensePlate by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf("Diagnostic") }
    var preferredDate by remember { mutableStateOf("") }
    var preferredTime by remember { mutableStateOf("") }
    var problemDescription by remember { mutableStateOf("") }

    val serviceTypes = listOf("Diagnostic", "Oil Change", "Brakes", "Battery", "Tires", "Other")

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
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Help */ }) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Help")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Request Service",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Provide details about your vehicle issue to schedule a repair or maintenance session.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Vehicle Details
                    Text(
                        text = "Vehicle Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    OutlinedTextField(
                        value = licensePlate,
                        onValueChange = { licensePlate = it },
                        label = { Text("License Plate Number") },
                        placeholder = { Text("ABC-1234") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Service Type
                    Text(
                        text = "Service Type",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Select primary service category",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Using a simple Column with Rows for the grid to keep it scrollable within the Column
                    Column {
                        val rows = serviceTypes.chunked(3)
                        rows.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { service ->
                                    FilterChip(
                                        modifier = Modifier.weight(1f),
                                        selected = selectedService == service,
                                        onClick = { selectedService = service },
                                        label = {
                                            Text(
                                                text = service,
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            )
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            labelColor = MaterialTheme.colorScheme.onSurface
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = selectedService == service,
                                            borderColor = MaterialTheme.colorScheme.outline,
                                            selectedBorderColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                                // Fill empty slots in the last row if needed
                                if (rowItems.size < 3) {
                                    repeat(3 - rowItems.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Schedule Appointment
                    Text(
                        text = "Schedule Appointment",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = preferredDate,
                            onValueChange = { preferredDate = it },
                            label = { Text("Preferred Date") },
                            placeholder = { Text("dd-mm-yyyy") },
                            trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = preferredTime,
                            onValueChange = { preferredTime = it },
                            label = { Text("Preferred Time") },
                            placeholder = { Text("--:--") },
                            trailingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Problem Description
                    Text(
                        text = "Problem Description",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    OutlinedTextField(
                        value = problemDescription,
                        onValueChange = { problemDescription = it },
                        placeholder = { Text("Please describe the symptoms, noises, or issues you are experiencing...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Submit Button
                    Button(
                        onClick = { /* Submit Request */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Submit Request",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RequestServiceScreenPreview() {
    InstantMechanicAssignmentTheme {
        RequestServiceScreen(onBackClick = {})
    }
}
