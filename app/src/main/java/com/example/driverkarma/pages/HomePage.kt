package com.example.driverkarma.pages

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomePage() {
    // The score of the user's most recent driving session
    val score = remember {
        mutableIntStateOf(0)
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
                    RandomScoreButton(score)
                }
            }
        },
        content = {
            paddingValues -> Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    DriveScoreCircle(score)
                }
            }
        },
    )
}

@Composable
private fun DriveScoreCircle(score: MutableIntState) {
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
                    getWheelHue(score.intValue),
                    1f,
                    1f
                ),
                startAngle = -90f,
                sweepAngle = -score.intValue / 100f * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
        }

        Text(text = score.intValue.toString())
    }
}

@Composable
private fun RandomScoreButton(score: MutableIntState) {
    Button(
        onClick = {
            score.intValue = getRandomScore()
        }
    ) {
        Text(text = "Get a score!")
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
            contentDescription = "Favorite"
        )
    }
}

/*
* Gets the colour that the driver score circle becomes
*/
private fun getWheelHue(int:Int): Float {
    return 0f.coerceAtLeast(int.toFloat() * 0.4f / 100f * 360f - 30)
}

// This function is getting removed in due time, most likely
private fun getRandomScore(): Int {
    return (Math.random() * 101).roundToInt()
}