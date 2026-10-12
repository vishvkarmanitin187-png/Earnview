package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.EarnViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: EarnViewModel) {
    val user by viewModel.currentUser.collectAsState()
    val currentRoute by viewModel.currentScreen.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile & Settings") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            BottomNav(viewModel = viewModel, currentRoute = currentRoute)
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
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.name.take(1),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = user.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = user.email, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "Referral Code: ${user.referralCode}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            if (user.isAdmin) {
                item {
                    ProfileMenuRow(title = "Admin Panel Control", icon = Icons.Default.AdminPanelSettings, tint = MaterialTheme.colorScheme.error) {
                        viewModel.navigateTo("admin")
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProfileMenuRow(title = "Daily Streak Calendar", icon = Icons.Default.LocalFireDepartment) {
                        viewModel.navigateTo("streak")
                    }
                    ProfileMenuRow(title = "Rewarded Ads & Monetization", icon = Icons.Default.PlayCircle) {
                        viewModel.navigateTo("rewarded_ads")
                    }
                    ProfileMenuRow(title = "Achievements & Badges", icon = Icons.Default.EmojiEvents) {
                        viewModel.navigateTo("achievements")
                    }
                    ProfileMenuRow(title = "Weekly Leaderboard", icon = Icons.Default.Leaderboard) {
                        viewModel.navigateTo("leaderboard")
                    }
                    ProfileMenuRow(title = "Invite Friends & Referral", icon = Icons.Default.Share) {
                        viewModel.navigateTo("referral")
                    }
                    ProfileMenuRow(title = "Withdrawal Requests", icon = Icons.Default.AccountBalanceWallet) {
                        viewModel.navigateTo("withdrawal")
                    }
                    ProfileMenuRow(title = "FAQ & Help Center", icon = Icons.Default.Help) {
                        viewModel.navigateTo("faq")
                    }
                    ProfileMenuRow(title = "Terms & Conditions", icon = Icons.Default.Description) {
                        viewModel.navigateTo("terms")
                    }
                    ProfileMenuRow(title = "Privacy Policy", icon = Icons.Default.PrivacyTip) {
                        viewModel.navigateTo("privacy")
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.logout() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout from Account")
                }
            }
        }
    }
}

@Composable
fun ProfileMenuRow(title: String, icon: ImageVector, tint: Color = MaterialTheme.colorScheme.primary, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = tint)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
