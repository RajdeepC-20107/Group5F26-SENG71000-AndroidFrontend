package com.example.driverkarma.ui.score

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.driverkarma.data.DrivingScore
import com.example.driverkarma.data.ScoreFactor
import com.example.driverkarma.functions.getScoreHue

/**
 * The screen used to display a score and its associated data
 *
 * @param navController The navigation controller used to navigate through the app
 * @param score The driving score that is being displayed
 *
 * @author  Sifan Terefe
 * @author  Rajdeep Chowdhury
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ScoreScreen(
    navController: NavController,
    score: DrivingScore
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    BackButton(navController)
                }
            )
        },
        content = {
                paddingValues -> Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentPadding = PaddingValues(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    ScoreRing(score.score)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        score.date,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Stat("Distance", "${score.distanceKm} km")
                        Stat("Duration", "${score.durationMin} min")
                    }
                    Spacer(Modifier.height(24.dp))
                    Text(
                        "Events",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                items(score.factors) { factor ->
                    FactorCard(factor = factor)
                }
            }
        }},
    )
}

/**
 * Displays a contributing factor to the driver score going down.
 *
 * @param factor    Represents a factor that caused score to go down.
 *
 * @author  Sifan Terefe
 */
@Composable
private fun FactorCard(factor: ScoreFactor) {
    Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(factor.name, fontWeight = FontWeight.Bold)
                Text(if (factor.impact == 0.0) "—" else "${factor.impact}")
            }
            Text(factor.note, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

/**
 * Formatted text used for representing a statistic.
 * Currently used to show distance travelled and drive duration.
 *
 * @param label The name of the statistic
 * @param value The value of the statistic
 *
 * @author  Sifan Terefe
 */
@Composable
private fun Stat(label: String, value: String) = Column(
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        value,
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        label,
        style = MaterialTheme.typography.bodySmall
    )
}

/**
 * The coloured ring that visualizes the score.
 *
 * @param score The score the user got.
 *
 * @author  Sifan Terefe
 * @author  Rajdeep Chowdhury
 */
@Composable
fun ScoreRing(score: Int) {
    val color = Color.hsv(
        getScoreHue(score),
        1F,
        1F
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(180.dp)
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = Stroke(
                width = 18.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawArc(
                Color.LightGray,
                0f,
                360f,
                false,
                style = stroke
            )
            drawArc(color,
                -90f,
                -(360f * score) / 100f,
                false,
                style = stroke
            )
        }
        Text(
            "$score",
            fontSize = 52.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * The back button to return to the home screen.
 *
 * @param navController The navigation controller used to navigate through the app.
 *
 * @author  Rajdeep Chowdhury
 */
@Composable
private fun BackButton(navController: NavController) {
    IconButton(
        onClick = {
            navController.popBackStack()
        },
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBackIosNew,
            contentDescription = "Back"
        )
    }
}