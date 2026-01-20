package com.example.template

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val STARTUP_DELAY = 3000L
private const val ANIMATION_DURATION = 800
private const val INITIAL_SCALE = 1f
private const val TARGET_SCALE = 1.3f
private val HEART_COLOR = Color(0xFFE91E63)
private val HEART_SIZE = 200.dp

private const val HEART_BOTTOM_Y_FRACTION = 0.75f
private const val HEART_TOP_Y_FRACTION = 0.25f
private const val HEART_CONTROL_Y_FRACTION = 0.5f

 @Composable
fun StartupScreen(
    store: StartupStore,
    onFinished: () -> Unit
) {
    val state by store.state.collectAsState()

    LaunchedEffect(state.isFinished) {
        if (state.isFinished) {
            onFinished()
        }
    }

    LaunchedEffect(Unit) {
        delay(STARTUP_DELAY)
        store.dispatch(StartupIntent.AnimationFinished)
    }

    StartupContent()
}

 @Composable
fun StartupContent() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            HeartAnimation(modifier = Modifier.size(HEART_SIZE))
        }
    }
}

 @Composable
fun HeartAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "heartScale")
    val scale by infiniteTransition.animateFloat(
        initialValue = INITIAL_SCALE,
        targetValue = TARGET_SCALE,
        animationSpec = infiniteRepeatable(
            animation = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Canvas(modifier = modifier.scale(scale)) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(width / 2, height * HEART_BOTTOM_Y_FRACTION)
            cubicTo(
                0f, height * HEART_CONTROL_Y_FRACTION,
                0f, 0f,
                width / 2, height * HEART_TOP_Y_FRACTION
            )
            cubicTo(
                width, 0f,
                width, height * HEART_CONTROL_Y_FRACTION,
                width / 2, height * HEART_BOTTOM_Y_FRACTION
            )
        }
        drawPath(path, HEART_COLOR)
    }
}
