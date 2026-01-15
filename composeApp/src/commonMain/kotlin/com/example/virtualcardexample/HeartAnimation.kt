package com.example.virtualcardexample

import androidx.compose.animation.core.*
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

@Composable
fun HeartAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val color by infiniteTransition.animateColor(
        initialValue = Color(0xFFFF4081),
        targetValue = Color(0xFFC2185B),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(modifier = Modifier.size(150.dp * scale)) {
            val width = size.width
            val height = size.height
            
            val path = Path().apply {
                moveTo(width / 2f, height * 0.35f)
                cubicTo(width * 0.2f, height * 0.1f, width * -0.1f, height * 0.5f, width * 0.5f, height * 0.9f)
                cubicTo(width * 1.1f, height * 0.5f, width * 0.8f, height * 0.1f, width * 0.5f, height * 0.35f)
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
                style = Stroke(width = 4.dp.toPx())
            )
        }
    }
}