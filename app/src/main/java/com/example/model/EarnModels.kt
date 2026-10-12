package com.example.model

data class User(
    val id: String = "user_1",
    val name: String = "Alex Morgan",
    val email: String = "alex@earnview.demo",
    val points: Int = 1250,
    val streakCount: Int = 5,
    val level: Int = 3,
    val isAdmin: Boolean = false,
    val isSuspended: Boolean = false,
    val referralCode: String = "EARN2026",
    val referredBy: String = ""
)

data class TaskItem(
    val id: String,
    val title: String,
    val description: String,
    val rewardPoints: Int,
    val category: String, // "App", "Video", "Survey", "Quiz"
    val isCompleted: Boolean = false,
    val actionUrl: String = ""
)

data class Mission(
    val id: String,
    val title: String,
    val description: String,
    val rewardPoints: Int,
    val targetCount: Int,
    val currentCount: Int,
    val isClaimed: Boolean = false
)

data class Transaction(
    val id: String,
    val title: String,
    val points: Int,
    val timestamp: String,
    val type: String, // "earned", "withdrawn", "bonus", "ad_reward"
    val status: String // "Completed", "Pending", "Rejected"
)

data class WithdrawalRequest(
    val id: String,
    val userId: String,
    val userName: String,
    val upiId: String,
    val amount: Int,
    val pointsDeducted: Int,
    val status: String, // "Pending", "Approved", "Paid", "Rejected"
    val requestDate: String
)

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val points: Int,
    val avatarInitial: String,
    val isCurrentUser: Boolean = false
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean,
    val progress: Int,
    val maxProgress: Int,
    val rewardPoints: Int
)

data class Referral(
    val id: String,
    val referredUserName: String,
    val signupDate: String,
    val pointsEarned: Int
)

data class AdNetworkConfig(
    val adNetworkName: String = "Google AdSense / AdMob",
    val publisherId: String = "",
    val adUnitId: String = "",
    val bannerAdUnitId: String = "",
    val rewardedAdUnitId: String = "",
    val isAdEnabled: Boolean = false,
    val rewardedAdsAvailable: Boolean = false,
    val rewardedPoints: Int = 50
)

data class AdminSettings(
    val minWithdrawalAmount: Int = 500,
    val dailyTaskLimit: Int = 15,
    val rewardMultiplier: Float = 1.0f,
    val maintenanceMode: Boolean = false,
    val adConfig: AdNetworkConfig = AdNetworkConfig()
)
