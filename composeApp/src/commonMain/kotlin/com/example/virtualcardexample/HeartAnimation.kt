package com.example.virtualcardexample

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

private const val ANIMATION_DURATION_MS = 1000
private const val INITIAL_SCALE = 1f
private const val TARGET_SCALE = 1.3f
private const val HEART_SIZE_DP = 150
private const val STROKE_WIDTH_DP = 4

// Path constants
private const val CP1X = 0.2f
private const val CP1Y = 0.1f
private const val CP2X = -0.1f
private const val CP2Y = 0.5f
private const val CP3X = 0.5f
private const val CP3Y = 0.9f
private const val CP4X = 1.1f
private const val CP4Y = 0.5f
private const val CP5X = 0.8f
private const val CP5Y = 0.1f
private const val HEART_START_Y = 0.35f

// Color constants
private const val HEART_COLOR_INITIAL = 0xFFFF4081
private const val HEART_COLOR_TARGET = 0xFFC2185B

@Composable
fun HeartAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = INITIAL_SCALE,
        targetValue = TARGET_SCALE,
        animationSpec = infiniteRepeatable(
            animation = tween(ANIMATION_DURATION_MS, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val color by infiniteTransition.animateColor(
        initialValue = Color(HEART_COLOR_INITIAL),
        targetValue = Color(HEART_COLOR_TARGET),
        animationSpec = infiniteRepeatable(
            animation = tween(ANIMATION_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(modifier = Modifier.size(HEART_SIZE_DP.dp * scale)) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(width / 2f, height * HEART_START_Y)
                cubicTo(width * CP1X, height * CP1Y, width * CP2X, height * CP2Y, width * CP3X, height * CP3Y)
                cubicTo(width * CP4X, height * CP4Y, width * CP5X, height * CP5Y, width * CP3X, height * HEART_START_Y)
                close()
            }

            // Draw the heart fill
            drawPath(
                path = path,
                color = color,
                style = Fill
            )

            // Draw a cartoonish outline
            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = STROKE_WIDTH_DP.dp.toPx())
            )
        }
    }
}
