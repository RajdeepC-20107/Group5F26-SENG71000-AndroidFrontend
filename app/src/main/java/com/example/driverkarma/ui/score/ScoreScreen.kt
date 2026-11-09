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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.driverkarma.data.DrivingScore
import com.example.driverkarma.data.ScoreFactor

// ScoreScreen.kt
val sampleScore = DrivingScore( //sample score stub
    score = 87, date = "Nov 5, 2026", distanceKm = 23.4, durationMin = 31,
    factors = listOf(
        ScoreFactor("Speed Limit", "Two instances of driving over speed limit", -6),
        ScoreFactor("Braking Events", "One harsh braking event detected.", -4),
        ScoreFactor("Weather", "Dry conditions", 0)
    )
)

@Composable
fun ScoreScreen(data: DrivingScore = sampleScore) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentPadding = PaddingValues(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ScoreRing(data.score)
            Spacer(Modifier.height(8.dp))
            Text(data.date, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Stat("Distance", "${data.distanceKm} km")
                Stat("Duration", "${data.durationMin} min")
            }
            Spacer(Modifier.height(24.dp))
            Text("Events", style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth())
        }
        items(data.factors) { factor ->
            FactorCard(factor = factor)
        }
    }
}

@Composable
private fun FactorCard(factor: ScoreFactor) {
    Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(factor.name, fontWeight = FontWeight.Bold)
                Text(if (factor.impact == 0) "—" else "${factor.impact}")
            }
            Text(factor.note, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun Stat(label: String, value: String) =
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }

@Composable
fun ScoreRing(score: Int) {
    val color = when {
        score >= 80 -> Color(0xFF2E7D32)
        score >= 60 -> Color(0xFFF9A825)
        else -> Color(0xFFC62828)
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            drawArc(Color.LightGray, 0f, 360f, false, style = stroke)
            drawArc(color, -90f, (360f * score) / 100f, false, style = stroke)
        }
        Text("$score", fontSize = 52.sp, fontWeight = FontWeight.Bold)
    }
}
