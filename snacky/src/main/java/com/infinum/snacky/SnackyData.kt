package com.infinum.snacky

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

/**
 * Interface to represent the visuals of one particular Snackbar as a piece of the [SnackyState]
 *
 * @property duration duration of the Snackbar
 * @property onDismissCallback the callback to be called when the Snackbar is dismissed
 * @property onMainActionCallback the callback to be called when the main action is performed
 * @property onSecondaryActionCallback the callback to be called when the secondary action is performed
 * @property hasAction a boolean indicating whether the Snackbar has an action
 */
@Stable
public interface SnackyData {
    public val duration: SnackyDuration

    public val onDismissCallback: () -> Unit
        get() = {}

    public val onMainActionCallback: () -> Unit
        get() = {}

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
