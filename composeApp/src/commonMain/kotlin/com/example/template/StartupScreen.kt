package com.example.template

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val ANIMATION_DURATION_MS = 3000L
private const val HEART_BEAT_DURATION_MS = 500
private const val INITIAL_SCALE = 1f
private const val TARGET_SCALE = 1.2f
private const val ICON_SIZE_DP = 120

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

    StartupContent(
        onAnimationFinished = { store.dispatch(StartupIntent.AnimationFinished) }
    )
}

@Composable
fun StartupContent(
    onAnimationFinished: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "HeartBeat")
    val scale by infiniteTransition.animateFloat(
        initialValue = INITIAL_SCALE,
        targetValue = TARGET_SCALE,
        animationSpec = infiniteRepeatable(
            animation = tween(HEART_BEAT_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale"
    )

    LaunchedEffect(Unit) {
        delay(ANIMATION_DURATION_MS)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            modifier = Modifier
                .size(ICON_SIZE_DP.dp)
                .scale(scale),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
