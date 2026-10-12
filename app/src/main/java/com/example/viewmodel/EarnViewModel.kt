package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EarnViewModel : ViewModel() {

    // Current navigation route or screen state
    private val _currentScreen = MutableStateFlow("welcome")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Authentication state
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Current User
    private val _currentUser = MutableStateFlow(
        User(
            id = "user_1",
            name = "Alex Morgan",
            email = "alex@earnview.demo",
            points = 1250,
            streakCount = 5,
            level = 3,
            isAdmin = false,
            isSuspended = false,
            referralCode = "EARN2026"
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // All Users (for admin panel)
    private val _usersList = MutableStateFlow(
        listOf(
            User("user_1", "Alex Morgan", "alex@earnview.demo", 1250, 5, 3, false, false, "EARN2026"),
            User("user_2", "Sarah Connor", "sarah@earnview.demo", 3400, 12, 5, false, false, "SARAH99"),
            User("user_3", "John Wick", "john@earnview.demo", 850, 2, 2, false, true, "WICK88"),
            User("admin_1", "Admin Boss", "admin@earnview.demo", 9999, 30, 10, true, false, "ADMINKEY")
        )
    )
    val usersList: StateFlow<List<User>> = _usersList.asStateFlow()

    // Tasks list
    private val _tasksList = MutableStateFlow(
        listOf(
            TaskItem("t1", "Install Finance App", "Download & open finance app for 2 mins", 150, "App", false),
            TaskItem("t2", "Watch Reward Video", "Watch short promotional clip", 50, "Video", false),
            TaskItem("t3", "Quick Opinion Survey", "Complete 3-minute lifestyle survey", 100, "Survey", false),
            TaskItem("t4", "Play Puzzle Level 5", "Reach level 5 in partner puzzle game", 250, "App", false),
            TaskItem("t5", "Daily Trivia Quiz", "Answer 5 correct general knowledge questions", 80, "Quiz", false),
            TaskItem("t6", "Visit Partner Website", "Browse sponsor page for 30 seconds", 40, "Survey", false)
        )
    )
    val tasksList: StateFlow<List<TaskItem>> = _tasksList.asStateFlow()

    // Missions list
    private val _missionsList = MutableStateFlow(
        listOf(
            Mission("m1", "Complete 3 Tasks", "Finish any 3 tasks today", 100, 3, 1, false),
            Mission("m2", "Refer a Friend", "Invite a friend using your referral code", 200, 1, 0, false),
            Mission("m3", "Daily Streak Check-in", "Check in today to maintain streak", 50, 1, 1, true),
            Mission("m4", "Earn 500 Points", "Accumulate 500 points across tasks", 150, 500, 250, false)
        )
    )
    val missionsList: StateFlow<List<Mission>> = _missionsList.asStateFlow()

    // Transactions list
    private val _transactionsList = MutableStateFlow(
        listOf(
            Transaction("tx1", "Daily Login Bonus", 50, "2026-10-12 08:00", "earned", "Completed"),
            Transaction("tx2", "Completed: Watch Reward Video", 50, "2026-10-12 09:15", "earned", "Completed"),
            Transaction("tx3", "Completed: Quick Opinion Survey", 100, "2026-10-11 14:30", "earned", "Completed"),
            Transaction("tx4", "UPI Withdrawal to GooglePay", -500, "2026-10-10 18:00", "withdrawn", "Paid")
        )
    )
    val transactionsList: StateFlow<List<Transaction>> = _transactionsList.asStateFlow()

    // Withdrawal requests
    private val _withdrawalsList = MutableStateFlow(
        listOf(
            WithdrawalRequest("w1", "user_1", "Alex Morgan", "alex@upi", 500, 500, "Paid", "2026-10-10"),
            WithdrawalRequest("w2", "user_2", "Sarah Connor", "sarah@ybl", 1000, 1000, "Pending", "2026-10-12")
        )
    )
    val withdrawalsList: StateFlow<List<WithdrawalRequest>> = _withdrawalsList.asStateFlow()

    // Achievements list
    private val _achievementsList = MutableStateFlow(
        listOf(
            Achievement("a1", "Task Master", "Complete 10 total tasks", true, 10, 10, 200),
            Achievement("a2", "Streak Legend", "Maintain a 7-day streak", false, 5, 7, 300),
            Achievement("a3", "Referral King", "Invite 5 friends", false, 1, 5, 500),
            Achievement("a4", "High Roller", "Reach 5,000 points balance", false, 1250, 5000, 1000)
        )
    )
    val achievementsList: StateFlow<List<Achievement>> = _achievementsList.asStateFlow()

    // Referrals list
    private val _referralsList = MutableStateFlow(
        listOf(
            Referral("r1", "David Miller", "2026-10-08", 100),
            Referral("r2", "Emma Watson", "2026-10-10", 100)
        )
    )
    val referralsList: StateFlow<List<Referral>> = _referralsList.asStateFlow()

    // Admin Settings
    private val _adminSettings = MutableStateFlow(AdminSettings())
    val adminSettings: StateFlow<AdminSettings> = _adminSettings.asStateFlow()

    // Leaderboard
    val leaderboardUsers = listOf(
        LeaderboardUser(1, "Sarah Connor", 3400, "S", false),
        LeaderboardUser(2, "Alex Morgan", 1250, "A", true),
        LeaderboardUser(3, "Michael Scott", 1100, "M", false),
        LeaderboardUser(4, "Dwight Schrute", 950, "D", false),
        LeaderboardUser(5, "John Wick", 850, "J", false)
    )

    fun navigateTo(route: String) {
        _currentScreen.value = route
    }

    fun login(email: String, pass: String): Boolean {
        val found = _usersList.value.find { it.email.equals(email, ignoreCase = true) }
        if (found != null) {
            if (found.isSuspended) return false
            _currentUser.value = found
            _isLoggedIn.value = true
            _currentScreen.value = if (found.isAdmin) "admin" else "home"
            return true
        }
        // If demo credentials match default
        val newUser = User(
            id = "user_${System.currentTimeMillis()}",
            name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
            email = email,
            points = 500, // sign up bonus
            streakCount = 1,
            level = 1,
            isAdmin = email.contains("admin", ignoreCase = true),
            isSuspended = false,
            referralCode = "EARN" + (1000..9999).random()
        )
        _usersList.update { it + newUser }
        _currentUser.value = newUser
        _isLoggedIn.value = true
        _currentScreen.value = if (newUser.isAdmin) "admin" else "home"
        return true
    }

    fun signUp(name: String, email: String, referralCode: String): Boolean {
        val newUser = User(
            id = "user_${System.currentTimeMillis()}",
            name = name,
            email = email,
            points = 200, // welcome bonus
            streakCount = 1,
            level = 1,
            isAdmin = false,
            isSuspended = false,
            referralCode = "EARN" + (1000..9999).random(),
            referredBy = referralCode
        )
        _usersList.update { it + newUser }
        _currentUser.value = newUser
        _isLoggedIn.value = true
        _currentScreen.value = "home"
        addTransaction(Transaction("tx_${System.currentTimeMillis()}", "Sign-up Welcome Bonus", 200, getFormattedDate(), "earned", "Completed"))
        return true
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentScreen.value = "welcome"
    }

    fun completeTask(taskId: String) {
        val task = _tasksList.value.find { it.id == taskId }
        if (task != null && !task.isCompleted) {
            val pointsEarned = (task.rewardPoints * _adminSettings.value.rewardMultiplier).toInt()
            
            // Update task status
            _tasksList.update { list ->
                list.map { if (it.id == taskId) it.copy(isCompleted = true) else it }
            }

            // Update user points securely
            val updatedUser = _currentUser.value.copy(points = _currentUser.value.points + pointsEarned)
            _currentUser.value = updatedUser
            _usersList.update { list -> list.map { if (it.id == updatedUser.id) updatedUser else it } }

            // Add transaction
            addTransaction(Transaction(
                id = "tx_${System.currentTimeMillis()}",
                title = "Completed: ${task.title}",
                points = pointsEarned,
                timestamp = getFormattedDate(),
                type = "earned",
                status = "Completed"
            ))
        }
    }

    fun claimStreakReward() {
        val bonus = 50
        val updatedUser = _currentUser.value.copy(
            points = _currentUser.value.points + bonus,
            streakCount = _currentUser.value.streakCount + 1
        )
        _currentUser.value = updatedUser
        _usersList.update { list -> list.map { if (it.id == updatedUser.id) updatedUser else it } }
        addTransaction(Transaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "Daily Streak Bonus (Day ${updatedUser.streakCount})",
            points = bonus,
            timestamp = getFormattedDate(),
            type = "earned",
            status = "Completed"
        ))
    }

    fun requestWithdrawal(upiId: String, amountPoints: Int): Boolean {
        val user = _currentUser.value
        val minWithdrawal = _adminSettings.value.minWithdrawalAmount
        if (amountPoints < minWithdrawal || user.points < amountPoints) {
            return false
        }

        // Deduct points securely
        val updatedUser = user.copy(points = user.points - amountPoints)
        _currentUser.value = updatedUser
        _usersList.update { list -> list.map { if (it.id == updatedUser.id) updatedUser else it } }

        val request = WithdrawalRequest(
            id = "w_${System.currentTimeMillis()}",
            userId = user.id,
            userName = user.name,
            upiId = upiId,
            amount = amountPoints,
            pointsDeducted = amountPoints,
            status = "Pending",
            requestDate = getFormattedDate().substringBefore(" ")
        )
        _withdrawalsList.update { listOf(request) + it }

        addTransaction(Transaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "Withdrawal Request to $upiId",
            points = -amountPoints,
            timestamp = getFormattedDate(),
            type = "withdrawn",
            status = "Pending"
        ))
        return true
    }

    // Admin Actions
    fun updateWithdrawalStatus(withdrawalId: String, newStatus: String) {
        _withdrawalsList.update { list ->
            list.map { if (it.id == withdrawalId) it.copy(status = newStatus) else it }
        }
    }

    fun updateAdminSettings(settings: AdminSettings) {
        _adminSettings.value = settings
    }

    fun updateAdConfig(config: AdNetworkConfig) {
        _adminSettings.update { it.copy(adConfig = config) }
    }

    fun watchRewardedAd(): Boolean {
        val config = _adminSettings.value.adConfig
        if (!config.isAdEnabled || !config.rewardedAdsAvailable || config.publisherId.isBlank() || config.rewardedAdUnitId.isBlank()) {
            return false
        }
        val reward = config.rewardedPoints
        val updatedUser = _currentUser.value.copy(points = _currentUser.value.points + reward)
        _currentUser.value = updatedUser
        _usersList.update { list -> list.map { if (it.id == updatedUser.id) updatedUser else it } }

        addTransaction(Transaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "Rewarded Video Ad (${config.adNetworkName})",
            points = reward,
            timestamp = getFormattedDate(),
            type = "ad_reward",
            status = "Verified & Completed"
        ))
        return true
    }

    fun toggleUserSuspension(userId: String) {
        _usersList.update { list ->
            list.map { if (it.id == userId) it.copy(isSuspended = !it.isSuspended) else it }
        }
    }

    fun addNewTask(title: String, description: String, reward: Int, category: String) {
        val newTask = TaskItem(
            id = "t_${System.currentTimeMillis()}",
            title = title,
            description = description,
            rewardPoints = reward,
            category = category,
            isCompleted = false
        )
        _tasksList.update { listOf(newTask) + it }
    }

    private fun addTransaction(tx: Transaction) {
        _transactionsList.update { listOf(tx) + it }
    }

    private fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
