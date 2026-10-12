package com.example.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.viewmodel.EarnViewModel

@Composable
fun BottomNav(viewModel: EarnViewModel, currentRoute: String) {
    val items = listOf(
        Triple("home", "Home", Icons.Default.Home),
        Triple("earn", "Earn", Icons.Default.Star),
        Triple("missions", "Missions", Icons.Default.Assignment),
        Triple("wallet", "Wallet", Icons.Default.Wallet),
        Triple("profile", "Profile", Icons.Default.Person)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        items.forEach { (route, label, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                selected = currentRoute == route,
                onClick = { viewModel.navigateTo(route) }
            )
        }
    }
}
