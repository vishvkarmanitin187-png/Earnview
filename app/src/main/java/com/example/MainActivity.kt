package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.EarnViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: EarnViewModel = viewModel()
        val currentScreen by viewModel.currentScreen.collectAsState()

        when (currentScreen) {
          "welcome" -> WelcomeScreen(viewModel)
          "login" -> LoginScreen(viewModel)
          "signup" -> SignUpScreen(viewModel)
          "home" -> HomeScreen(viewModel)
          "earn" -> EarnScreen(viewModel)
          "missions" -> MissionsScreen(viewModel)
          "wallet" -> WalletScreen(viewModel)
          "profile" -> ProfileScreen(viewModel)
          "streak" -> StreakScreen(viewModel)
          "rewarded_ads" -> RewardedAdsScreen(viewModel)
          "achievements" -> AchievementsScreen(viewModel)
          "leaderboard" -> LeaderboardScreen(viewModel)
          "withdrawal" -> WithdrawalScreen(viewModel)
          "referral" -> ReferralScreen(viewModel)
          "admin" -> AdminPanelScreen(viewModel)
          "faq" -> FaqTermsPrivacyScreen(viewModel, "faq")
          "terms" -> FaqTermsPrivacyScreen(viewModel, "terms")
          "privacy" -> FaqTermsPrivacyScreen(viewModel, "privacy")
          else -> HomeScreen(viewModel)
        }
      }
    }
  }
}
