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
import com.example.driverkarma.ui.home.HomeScreen
import com.example.driverkarma.ui.login.LoginScreen
import com.example.driverkarma.ui.score.ScoreScreen
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Android 17+ blocks the local network unless the user allows it.
        // Needed so the app can reach the dev backend at 10.0.2.2.
        if (Build.VERSION.SDK_INT >= 37 &&
            ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_LOCAL_NETWORK")
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf("android.permission.ACCESS_LOCAL_NETWORK"), 1
            )
        }
        setContent {
            MaterialTheme {
                AppNavController()
            }
        }
    }
}

@Composable
private fun AppNavController() {
    val nav = rememberNavController()
    NavHost(
        nav,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(onLoggedIn = {
                nav.navigate("home") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            })
        }

        composable("home") {
            HomeScreen(
                nav
            )
        }

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
                    factors = listOf()
                )
            )
        }
    }
}