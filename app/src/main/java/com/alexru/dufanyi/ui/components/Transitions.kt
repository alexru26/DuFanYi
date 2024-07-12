package com.alexru.dufanyi.ui.components

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

fun enterTransition(): EnterTransition {
    return slideInHorizontally(
        animationSpec = tween(durationMillis = 200),
        initialOffsetX = { fullWidth -> fullWidth / 3 }
    ) + fadeIn(
        animationSpec = tween(durationMillis = 500)
    )
}

fun exitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(durationMillis = 1)
    )
}

fun popEnterTransition(): EnterTransition {
    return slideInHorizontally(
        animationSpec = tween(durationMillis = 200),
        initialOffsetX = { fullWidth -> -fullWidth / 3 }
    ) + fadeIn(
        animationSpec = tween(durationMillis = 500)
    )
}

fun popExitTransition(): ExitTransition {
    return slideOutHorizontally(
        animationSpec = tween(durationMillis = 400),
        targetOffsetX = { fullWidth -> fullWidth }
    ) + fadeOut(
        animationSpec = tween(durationMillis = 150)
    )
}