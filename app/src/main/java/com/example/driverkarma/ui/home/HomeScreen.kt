package com.example.driverkarma.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.driverkarma.functions.getScoreHue
import kotlin.math.roundToInt

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(navController: NavController) {
    // The score of the user's most recent driving session
    val score = remember {
        mutableIntStateOf(0)
    }

    // Whether the drive is being tracked or not
    val isDriving = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        val fineLocationGranted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

        if (fineLocationGranted) {
            // Permission granted: start the driving session
            isDriving.value = true
        } else {
            Toast.makeText(
                context,
                "Precise location permission is required.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Driver Karma") },
                actions = {
                    ProfileButton()
                }
            )
        },
        bottomBar = {
            NavigationBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StartAndEndDrivingButton(score, isDriving) {
                        startDriving(
                            context,
                            isDriving,
                            locationPermissionLauncher
                        )
                    }
                }
            }
        },
        content = {
                paddingValues -> Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(Modifier.padding(0.dp, 8.dp)) {
                MutableScoreRing(score)
            }
            Row {
                ViewScoringButton(score, navController)
            }
        }},
    )
}

@Composable
private fun MutableScoreRing(score: MutableIntState) {
    Box(
        modifier = Modifier
            .size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val strokeWidth = 8.dp.toPx()

            drawCircle(
                color = Color.Black.copy(alpha = 0.35f),
                style = Stroke(width = strokeWidth)
            )

            drawArc(
                color = Color.hsv(
                    getScoreHue(score.intValue),
                    1f,
                    1f
                ),
                startAngle = -90f,
                sweepAngle = -score.intValue / 100f * 360f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }

        Text(text = score.intValue.toString())
    }
}

@Composable
private fun StartAndEndDrivingButton(
    score: MutableIntState,
    isDriving: MutableState<Boolean>,
    onStartDriving: () -> Unit
) {
    Button(
        onClick = {
            if (!isDriving.value) {
                onStartDriving()
            } else {
                isDriving.value = false
                score.intValue = getRandomScore()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (!isDriving.value) MaterialTheme.colorScheme.primary
                else Color.Red
        )
    ) {
        Text(
            text =
                if (!isDriving.value) "Start Driving Session"
                else "End Driving Session"
        )
    }
}

@Composable
private fun ViewScoringButton(
    score: MutableIntState,
    navController: NavController
) {
    Button(
        onClick = {
            goToScoreScreen(score.intValue, navController)
        }
    ) {
        Text("View Scoring")
    }
}

@Composable
private fun ProfileButton() {
    IconButton(
        onClick = {

        },
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile"
        )
    }
}

// This function is getting removed in due time, most likely
private fun getRandomScore(): Int {
    return (Math.random() * 101).roundToInt()
}

private fun goToScoreScreen(
    score: Int,
    navController: NavController
) {
    navController.navigate(
        "score/$score"
    )
}

/**
 * Checks for required permissions and starts a driving session if permissions are granted.
 *
 * @param context   The context instance
 * @param isDriving     Whether the user is driving or not
 * @param locationPermissionLauncher    The way that location permissions are gotten
 */
private fun startDriving(
    context: Context,
    isDriving: MutableState<Boolean>,
    locationPermissionLauncher: ActivityResultLauncher<Array<String>>
) {
    val fineLocationGranted =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    if (fineLocationGranted) {
        isDriving.value = true
    } else {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}