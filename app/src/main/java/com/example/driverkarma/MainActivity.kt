package com.example.driverkarma

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.driverkarma.data.DrivingScore
import com.example.driverkarma.ui.home.HomeScreen
import com.example.driverkarma.ui.login.LoginScreen
import com.example.driverkarma.ui.score.ScoreScreen
import com.telematicssdk.tracking.TrackingApi
import com.telematicssdk.tracking.utils.permissions.TrackingPermissionsWizardActivity

class MainActivity : ComponentActivity() {

    private lateinit var tracking: TrackingApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavController()
            }
        }
        initTracking()
    }

    private fun initTracking() {
        tracking = TrackingApi.getInstance()
        tracking.initialize(this)

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == TrackingPermissionsWizardActivity.WIZARD_RESULT_ALL_GRANTED) {
                enableTracking()
            }
        }

        if (!tracking.areAllRequiredPermissionsAndSensorsGranted()) {
            val intent = TrackingPermissionsWizardActivity
                .getStartWizardIntent(this)

            activityResultLauncher.launch(intent)
        }
    }

    private fun enableTracking() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        tracking.setEnableSdk(true)
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