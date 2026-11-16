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
import com.example.driverkarma.classes.GpxFileManager
import com.example.driverkarma.classes.LocationTracker
import com.example.driverkarma.functions.getScoreHue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * The app's home screen.
 *
 * @param navController The navigation controller used to navigate to other screens.
 *
 * @author  Rajdeep Chowdhury
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(navController: NavController) {
    //The score of the user's most recent driving session
    val score = remember {
        mutableIntStateOf(0)
    }

    //Whether the drive is being tracked or not
    val isDriving = remember {
        mutableStateOf(false)
    }

    //The current local Compose application context
    val context = LocalContext.current

    //The location tracker object instance used for location tracking
    val locationTracker = remember {
        LocationTracker(context)
    }

    //The gpx file manager object instance used to create .gpx files
    val gpxFileManager = remember {
        GpxFileManager()
    }

    //Coroutine that is launched upon ending a drive session
    val coroutineScope = rememberCoroutineScope()

    //The permission launcher that asks for precise location permission
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        val fineLocationGranted =
            (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true)

        if (!fineLocationGranted) {
            Toast.makeText(
                context,
                "Precise location permission is required.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //The scaffold which the visual application lives on.
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
                    StartAndEndDrivingButton(
                        isDriving,
                        onStartDriving = {
                            startDriving(
                                context,
                                isDriving,
                                locationPermissionLauncher,
                                locationTracker
                            )
                        },
                        onEndDriving = {
                            endDriving(
                                score,
                                isDriving,
                                locationTracker,
                                gpxFileManager,
                                coroutineScope,
                                context
                            )
                        }
                    )
                }
            }
        },
        content = { paddingValues -> Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
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

/**
 * The score ring displayed big and centered on the home screen.
 *
 * @param score The score to be displayed
 *
 * @author  Rajdeep Chowdhury
 */
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

/**
 * The button used to start and end driving sessions.
 *
 * @param isDriving Whether the phone is reportedly driving or not
 * @param onStartDriving    The function that must run upon pressing the button to start driving.
 * @param onEndDriving      The function that must run upon pressing the button to end driving.
 *
 * @author  Rajdeep Chowdhury
 */
@Composable
private fun StartAndEndDrivingButton(
    isDriving: MutableState<Boolean>,
    onStartDriving: () -> Unit,
    onEndDriving: () -> Unit
) {
    Button(
        onClick = {
            if (!isDriving.value) {
                onStartDriving()
            } else {
                onEndDriving()
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

/**
 * The button used to navigate to the score screen.
 *
 * @param score The score most recently achieved by the user and is being displayed
 * @param navController The navigation controller used to switch between screens
 *
 * @author  Rajdeep Chowdhury
 */
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

/**
 * TODO: This is not actually implemented yet, and may not get implemented.
 * The profile icon button used to go to the profile section.
 *
 * @author  Rajdeep Chowdhury
 */
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

/**
 * TODO: This still need to make a call to the backend.
 * The function used to navigate to the score screen.
 *
 * @param score The most recent score achieved by the user.
 * @param navController The navigation controller used to navigate through the app.
 *
 * @author  Rajdeep Chowdhury
 */
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
 * @param isDriving Whether the user is driving or not
 * @param locationPermissionLauncher    The way that location permissions are gotten
 * @param locationTracker   The location tracker object being used to track location
 *
 * @author  Rajdeep Chowdhury
 */
private fun startDriving(
    context: Context,
    isDriving: MutableState<Boolean>,
    locationPermissionLauncher: ActivityResultLauncher<Array<String>>,
    locationTracker: LocationTracker
) {
    val fineLocationGranted =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    if (fineLocationGranted) {
        locationTracker.startTracking()
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

/**
 * Ends a driving session, assuming one was initially started.
 *
 * @param score The score that is being display on the home page
 * @param isDriving Whether the vehicle is driving or not
 * @param locationTracker   The location tracker object being used to track location
 * @param gpxFileManager    The gpx file manager object being used to write .gpx files
 * @param coroutineScope    The coroutine that will be launched upon the drive ending
 * @param context   The current Compose application context
 *
 * @author  Rajdeep Chowdhury
 */
private fun endDriving(
    score: MutableIntState,
    isDriving: MutableState<Boolean>,
    locationTracker: LocationTracker,
    gpxFileManager: GpxFileManager,
    coroutineScope: CoroutineScope,
    context: Context
) {
    locationTracker.stopTracking()
    isDriving.value = false
    score.intValue = getRandomScore()

    coroutineScope.launch {
        val locations = locationTracker.getLocations()

        if (locations.isNotEmpty()) {
            val gpxFile = gpxFileManager.createGpxFile(
                context,
                locations
            )

            Toast.makeText(context,
                "GPX file created: ${gpxFile.absolutePath}",
                Toast.LENGTH_SHORT
            ).show()
        }
        else {
            Toast.makeText(context,
                "No location data was successfully tracked.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}