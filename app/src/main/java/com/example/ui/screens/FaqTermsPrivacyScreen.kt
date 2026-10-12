package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.EarnViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqTermsPrivacyScreen(viewModel: EarnViewModel, type: String) {
    val title = when (type) {
        "faq" -> "FAQ & Help Center"
        "terms" -> "Terms & Conditions"
        else -> "Privacy Policy"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo("profile") }) {
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
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        val content = when (type) {
                            "faq" -> "Q: How do I earn points?\nA: Complete tasks, surveys, watch videos, and maintain daily streaks.\n\nQ: How do I withdraw my points?\nA: Go to Wallet, tap Withdraw, enter your UPI ID and points amount. Minimum withdrawal applies.\n\nQ: Are earnings real?\nA: EarnView operates in Demo Mode with verified reward simulations and secure tracking."
                            "terms" -> "1. Acceptance of Terms: By using EarnView, you agree to comply with all rules and guidelines.\n2. Demo Mode: All earnings and tasks are simulated in demo mode.\n3. Account Integrity: Users cannot manipulate points or create duplicate accounts."
                            else -> "1. Information Security: We protect your privacy and personal account data.\n2. Data Usage: Information is used solely for managing your rewards wallet and streak progress.\n3. Third-party Links: Sponsor tasks link to secure partner external apps."
                        }

                        Text(text = content, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
