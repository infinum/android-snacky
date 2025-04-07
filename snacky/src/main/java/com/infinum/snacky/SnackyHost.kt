package com.infinum.snacky

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * Host for Snackbars to be used in [Scaffold] to properly show, hide and dismiss items
 *
 * This component with default parameters comes build-in with [Scaffold], if you need to show a
 * default [Snackbar], use [SnackyHostState.showSnackbar].
 *
 * If you want to customize appearance of the Snackbar, you can use [SnackyHostState.showSnackbar] passing
 * a custom [SnackyData] implementation.
 *
 * @param hostState state of this component to read and show Snackbars accordingly
 * @param modifier the [Modifier] to be applied to this component
 * @param animationSpec the [SnackyAnimationSpec] to be used for the displaying fae in and out animations
 */
@Composable
public fun SnackyHost(
    hostState: SnackyHostState,
    modifier: Modifier = Modifier,
    animationSpec: SnackyAnimationSpec = SnackyAnimationSpec(),
) {
    val currentSnackbarData = hostState.currentSnackyState
    LaunchedEffect(currentSnackbarData) {
        if (currentSnackbarData != null) {
            delay(currentSnackbarData.snackyData.duration.value.inWholeMilliseconds)
            currentSnackbarData.dismiss()
        }
    }
    FadeInFadeOutWithScale(
        current = hostState.currentSnackyState,
        modifier = modifier,
        animationSpec = animationSpec,
    ) {
        currentSnackbarData?.snackyData?.Render(currentSnackbarData)
    }
}
