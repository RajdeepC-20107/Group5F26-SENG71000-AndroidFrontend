package com.example.driverkarma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

@Composable
private fun AppNavController() {
    val nav = rememberNavController()
    NavHost(
        nav,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(onLoggedIn = {
                nav.navigate("score") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            })
        }

        composable("score") {
            ScoreScreen()
        }
    }
}