package com.example.driverkarma.data


// Models.kt
data class ScoreFactor(val name: String, val note: String, val impact: Int)

data class DrivingScore(
    val score: Int,              // from 0-100 matches the Driver Scores table
    val date: String,
    val distanceKm: Double,
    val durationMin: Int,
    val factors: List<ScoreFactor>
)

sealed interface LoginResult {
    data object Success : LoginResult
    data object InvalidCredentials : LoginResult
    data object NoConnection : LoginResult
}