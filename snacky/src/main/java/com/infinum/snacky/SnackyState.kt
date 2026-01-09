package com.infinum.snacky

import androidx.compose.runtime.Stable

/**
 * Interface to represent the state of one particular Snackbar as a piece of the
 * [SnackyHostState].
 */
@Stable
public interface SnackyState {
    /**
     * Holds the data needed to render the Snackbar
     */
    public val snackyData: SnackyData

    /**
     * Function to be called when main Snackbar action has been performed to notify the listeners.
     */
    public fun performMainAction()

    /**
     * Function to be called when secondary Snackbar action has been performed to notify the listeners.
     */
    public fun performSecondaryAction()

    /**
     * Function to be called when the Snackbar has been dismissed to notify the listeners.
     */
    public fun dismiss()
}
