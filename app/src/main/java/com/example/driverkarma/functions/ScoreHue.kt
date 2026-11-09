package com.example.driverkarma.functions

/*
* Gets the colour that the driver score circle becomes
* @property score   The score which a hue is associated to.
*/
fun getScoreHue(score:Int): Float {
    return 0f.coerceAtLeast(score.toFloat() * 0.4f / 100f * 360f - 30)
}