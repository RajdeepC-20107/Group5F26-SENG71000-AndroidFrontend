package com.example.driverkarma.functions

/**
 * Gets the colour that the driver score circle becomes.
 *
 * @param score The score which a hue is associated to
 * @return A float value to use as the score's hue.
 *
 * @author  Rajdeep Chowdhury
 */
fun getScoreHue(score:Int): Float {
    return 0f.coerceAtLeast(score.toFloat() * 0.4f / 100f * 360f - 30)
}