package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.EarnViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(viewModel: EarnViewModel) {
    val users by viewModel.usersList.collectAsState()
    val withdrawals by viewModel.withdrawalsList.collectAsState()
    val settings by viewModel.adminSettings.collectAsState()
    val adConfig = settings.adConfig

    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskReward by remember { mutableStateOf("100") }
    var minWithdrawalInput by remember { mutableStateOf(settings.minWithdrawalAmount.toString()) }

    // Ad config state fields
    var pubId by remember { mutableStateOf(adConfig.publisherId) }
    var rewardedUnitId by remember { mutableStateOf(adConfig.rewardedAdUnitId) }
    var isAdEnabled by remember { mutableStateOf(adConfig.isAdEnabled) }
    var rewardedAvailable by remember { mutableStateOf(adConfig.rewardedAdsAvailable) }
    var rewardedPointsStr by remember { mutableStateOf(adConfig.rewardedPoints.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Control Panel") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo("home") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Administrator Access", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                            Text(text = "Manage users, withdrawals, ad monetization, and tasks.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }

            // Ad Management Section
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Ad Network & Monetization Management", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = pubId,
                            onValueChange = { pubId = it },
                            label = { Text("AdSense / AdMob Publisher ID") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = rewardedUnitId,
                            onValueChange = { rewardedUnitId = it },
                            label = { Text("Rewarded Ad Unit ID") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = rewardedPointsStr,
                            onValueChange = { rewardedPointsStr = it },
                            label = { Text("Points per Rewarded Ad View") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Enable Ads & AdSense")
                            Switch(checked = isAdEnabled, onCheckedChange = { isAdEnabled = it })
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Rewarded Ads Available")
                            Switch(checked = rewardedAvailable, onCheckedChange = { rewardedAvailable = it })
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val pts = rewardedPointsStr.toIntOrNull() ?: 50
                                viewModel.updateAdConfig(
                                    adConfig.copy(
                                        publisherId = pubId,
                                        rewardedAdUnitId = rewardedUnitId,
                                        isAdEnabled = isAdEnabled,
                                        rewardedAdsAvailable = rewardedAvailable,
                                        rewardedPoints = pts
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Ad Configuration")
                        }
                    }
                }
            }

            // Settings section
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "System Configuration", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = minWithdrawalInput,
                            onValueChange = { minWithdrawalInput = it },
                            label = { Text("Minimum Withdrawal Points") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val minW = minWithdrawalInput.toIntOrNull() ?: settings.minWithdrawalAmount
                                viewModel.updateAdminSettings(settings.copy(minWithdrawalAmount = minW))
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Settings")
                        }
                    }
                }
            }

            // Create Task section
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Add New Reward Task", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = newTaskTitle,
                            onValueChange = { newTaskTitle = it },
                            label = { Text("Task Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newTaskReward,
                            onValueChange = { newTaskReward = it },
                            label = { Text("Reward Points") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (newTaskTitle.isNotBlank()) {
                                    val r = newTaskReward.toIntOrNull() ?: 100
                                    viewModel.addNewTask(newTaskTitle, "Partner offer task", r, "App")
                                    newTaskTitle = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Publish Task")
                        }
                    }
                }
            }

            // Manage Withdrawals
            item {
                Text(text = "Manage Withdrawal Requests", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            items(withdrawals) { w ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "User: ${w.userName} • UPI: ${w.upiId}", fontWeight = FontWeight.Bold)
                        Text(text = "Amount: ${w.amount} pts • Status: ${w.status}", fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { viewModel.updateWithdrawalStatus(w.id, "Approved") }) {
                                Text("Approve")
                            }
                            Button(
                                onClick = { viewModel.updateWithdrawalStatus(w.id, "Paid") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                            ) {
                                Text("Mark Paid")
                            }
                            OutlinedButton(onClick = { viewModel.updateWithdrawalStatus(w.id, "Rejected") }) {
                                Text("Reject")
                            }
                        }
                    }
                }
            }

            // Manage Users
            item {
                Text(text = "Manage Users & Suspensions", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            items(users) { u ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = u.name, fontWeight = FontWeight.Bold)
                            Text(text = u.email + if (u.isSuspended) " [SUSPENDED]" else "", fontSize = 12.sp, color = if (u.isSuspended) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Button(
                            onClick = { viewModel.toggleUserSuspension(u.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (u.isSuspended) Color(0xFF10B981) else Color(0xFFEF4444)
                            )
                        ) {
                            Text(if (u.isSuspended) "Unsuspend" else "Suspend")
                        }
                    }
                }
            }
        }
    }
}
