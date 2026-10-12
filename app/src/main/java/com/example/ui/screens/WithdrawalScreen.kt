package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.EarnViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawalScreen(viewModel: EarnViewModel) {
    val user by viewModel.currentUser.collectAsState()
    val withdrawals by viewModel.withdrawalsList.collectAsState()
    val settings by viewModel.adminSettings.collectAsState()

    var name by remember { mutableStateOf(user.name) }
    var upiId by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Withdraw Rewards") },
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Available Balance: ${user.points} Points", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Minimum withdrawal required: ${settings.minWithdrawalAmount} points", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Request Payout (UPI / Bank)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Account Holder Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = upiId,
                            onValueChange = { upiId = it },
                            label = { Text("UPI ID (e.g. name@paytm / ybl)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = amountStr,
                            onValueChange = { amountStr = it },
                            label = { Text("Points to Withdraw") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (message.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = message, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val amount = amountStr.toIntOrNull() ?: 0
                                if (name.isBlank() || upiId.isBlank() || amount <= 0) {
                                    message = "Please enter valid withdrawal details."
                                } else if (amount < settings.minWithdrawalAmount) {
                                    message = "Minimum withdrawal is ${settings.minWithdrawalAmount} points."
                                } else if (amount > user.points) {
                                    message = "Insufficient points balance."
                                } else {
                                    val success = viewModel.requestWithdrawal(upiId, amount)
                                    if (success) {
                                        message = "Withdrawal request submitted successfully!"
                                        upiId = ""
                                        amountStr = ""
                                    } else {
                                        message = "Withdrawal failed."
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Submit Withdrawal Request", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Text(text = "Withdrawal History & Status", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            items(withdrawals) { w ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "UPI: ${w.upiId}", fontWeight = FontWeight.Bold)
                            Text(
                                text = w.status,
                                fontWeight = FontWeight.Bold,
                                color = when (w.status) {
                                    "Paid", "Approved" -> Color(0xFF10B981)
                                    "Rejected" -> Color(0xFFEF4444)
                                    else -> Color(0xFFF59E0B)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Amount: ${w.amount} pts • Requested: ${w.requestDate}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
