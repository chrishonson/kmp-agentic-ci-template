package com.example.template

import androidx.compose.animation.core.*
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
import androidx.compose.runtime.rememberInfiniteTransition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

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
        delay(3000)
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
            HeartAnimation(modifier = Modifier.size(200.dp))
        }
    }
}

@Composable
fun HeartAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.scale(scale)) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(width / 2, height * 0.75f)
            cubicTo(
                0f, height * 0.5f,
                0f, 0f,
                width / 2, height * 0.25f
            )
            cubicTo(
                width, 0f,
                width, height * 0.5f,
                width / 2, height * 0.75f
            )
        }
        drawPath(path, Color(0xFFE91E63))
    }
}
