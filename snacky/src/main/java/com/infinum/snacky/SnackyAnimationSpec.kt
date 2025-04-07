package com.infinum.snacky

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing

/**
 * Default animation spec for the Snacky animations
 *
 * @property fadeInDuration the duration of the fade in animation
 * @property fadeOutDuration the duration of the fade out animation
 * @property inBetweenDelay the delay between the fade in and fade out animations
 * @property scaleFactor the scale factor to use for the scale animation
 * @property scaleEasing the easing to use for the scale animation
 * @property opacityEasing the easing to use for the opacity animation
 */
public data class SnackyAnimationSpec(
    val fadeInDuration: Int = SnackbarFadeInMillis,
    val fadeOutDuration: Int = SnackbarFadeOutMillis,
    val inBetweenDelay: Int = SnackbarInBetweenDelayMillis,
    val scaleFactor: Float = SnackbarScaleFactor,
    val scaleEasing: Easing = LinearEasing,
    val opacityEasing: Easing = FastOutSlowInEasing,
)

private const val SnackbarFadeInMillis = 150
private const val SnackbarFadeOutMillis = 75
private const val SnackbarInBetweenDelayMillis = 0
private const val SnackbarScaleFactor = 0.8f
