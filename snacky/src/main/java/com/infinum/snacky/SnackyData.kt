package com.infinum.snacky

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

/**
 * Interface to represent the visuals of one particular Snackbar as a piece of the [SnackyState]
 */
@Stable
public interface SnackyData {
    /**
     * Duration of the Snackbar
     */
    public val duration: SnackyDuration

    /**
     * The callback to be called when the Snackbar is dismissed
     */
    public val onDismissCallback: () -> Unit
        get() = {}

    /**
     * The callback to be called when the main action is performed
     */
    public val onMainActionCallback: () -> Unit
        get() = {}

    /**
     * The callback to be called when the secondary action is performed
     */
    public val onSecondaryActionCallback: () -> Unit
        get() = {}

    /**
     * A boolean indicating whether the Snackbar has an action.
     * @return true if either the main or secondary action callback is non-empty, false otherwise.
     */
    public val hasAction: Boolean
        get() = onMainActionCallback != {} || onSecondaryActionCallback != {}

    /**
     * Function to be called to render the Snackbar
     *
     * @param snackbarState the state of the Snackbar
     */
    @Composable
    public fun Render(snackbarState: SnackyState)
}
