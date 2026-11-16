package com.example.driverkarma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.driverkarma.data.DrivingScore
import com.example.driverkarma.data.ScoreFactor
import com.example.driverkarma.ui.home.HomeScreen
import com.example.driverkarma.ui.login.LoginScreen
import com.example.driverkarma.ui.score.ScoreScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavController()
            }
        }
    }
}

/**
 * The navigation controller composable used by the entire app.
 *
 * @author  Rajdeep Chowdhury
 * @author  Sifan Terefe
 */
@Composable
private fun AppNavController() {
    val nav = rememberNavController()
    NavHost(
        nav,
        startDestination = "login"
    ) {
        //Login Screen
        composable("login") {
            LoginScreen(onLoggedIn = {
                nav.navigate("home") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            })
        }

        //Home Screen
        composable("home") {
            HomeScreen(
                nav
            )
        }

        //Score Screen
        //Requires a score integer passed into it
        //TODO: This will eventually need more than just a score number
        composable(
            "score/{score}",
            arguments = listOf(
                navArgument("score") {
                    type = NavType.IntType
                }
            )
        ) { params ->
            ScoreScreen(
                nav,
                DrivingScore(
                    score = params.arguments?.getInt("score") ?: 0,
                    date = "",
                    distanceKm = 0.0,
                    durationMin = 0,
                    factors = listOf(
                        ScoreFactor(
                            "Speed Limit",
                            "Two instances of driving over speed limit",
                            -6.0
                        ),
                        ScoreFactor(
                            "Braking Events",
                            "One harsh braking event detected.",
                            -4.0
                        ),
                        ScoreFactor(
                            "Weather",
                            "Dry conditions",
                            0.0
                        )
                    )
                )
            )
        }
    }
}