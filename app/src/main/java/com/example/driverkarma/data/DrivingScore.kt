package com.example.driverkarma.data

data class DrivingScore(
    val score: Int,              // from 0-100 matches the Driver Scores table
    val date: String,
    val distanceKm: Double,
    val durationMin: Int,
    val factors: List<ScoreFactor>
)
