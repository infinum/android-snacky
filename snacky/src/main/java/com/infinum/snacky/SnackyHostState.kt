package com.infinum.snacky

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.infinum.snacky.default.DefaultSnackbar
import com.infinum.snacky.default.DefaultSnackbarData
import kotlin.coroutines.resume
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * State of the [SnackyHost], which controls the queue and the current Snackbar being shown
 * inside the [SnackyHost].
 *
 * This state is usually [remember]ed and used to provide a [SnackyHost] to a [Scaffold].
 */
@Stable
public class SnackyHostState {

    /**
     * Only one [DefaultSnackbar] can be shown at a time. Since a suspending Mutex is a fair queue, this
     * manages our message queue and we don't have to maintain one.
     */
    private val mutex = Mutex()

    /** The current [SnackyState] being shown by the [SnackyHost], or `null` if none. */
    public var currentSnackyState: SnackyState? by mutableStateOf(null)
        private set

    /**
     * Shows or queues to be shown a [DefaultSnackbar] at the bottom of the [Scaffold] to which this state
     * is attached and suspends until the snackbar has disappeared.
     *
     * [SnackyHostState] guarantees to show at most one snackbar at a time. If this function is
     * called while another snackbar is already visible, it will be suspended until this snackbar is
     * shown and subsequently addressed. If the caller is cancelled, the snackbar will be removed
     * from display and/or the queue to be displayed.
     *
     * All of this allows for granular control over the snackbar queue
     *
     * @param message text to be shown in the Snackbar
     * @param actionLabel optional action label to show as button in the Snackbar
     * @param hasDismissAction a boolean to show a dismiss action in the Snackbar. This is
     *   recommended to be set to true for better accessibility when a Snackbar is set with a
     *   [SnackyDuration.Indefinite]
     * @param onActionCallback the callback to be called when the action is performed
     * @param onDismissCallback the callback to be called when the Snackbar is dismissed
     * @param duration duration to control how long snackbar will be shown in [SnackyHost], either
     *   [SnackyDuration.Short], [SnackyDuration.Long] or [SnackyDuration.Indefinite].
     */
    public suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        hasDismissAction: Boolean = false,
        dismissActionContentDescription: String = "",
        onActionCallback: () -> Unit = { },
        onDismissCallback: () -> Unit = { },
        duration: SnackyDuration = if (actionLabel == null) SnackyDuration.Short else SnackyDuration.Indefinite,
    ) {
        showSnackbar(
            DefaultSnackbarData(
                message = message,
                duration = duration,
                onDismissCallback = onDismissCallback,
                onMainActionCallback = onActionCallback,
                actionLabel = actionLabel,
                withDismissAction = hasDismissAction,
                dismissActionContentDescription = dismissActionContentDescription,
            ),
        )
    }

    /**
     * Shows or queues to be shown a custom Snackbar at the bottom of the [Scaffold] to which this state
     * is attached and suspends until the snackbar has disappeared.
     *
     * [SnackyHostState] guarantees to show at most one snackbar at a time. If this function is
     * called while another snackbar is already visible, it will be suspended until this snackbar is
     * shown and subsequently addressed. If the caller is cancelled, the snackbar will be removed
     * from display and/or the queue to be displayed.
     *
     * All of this allows for granular control over the snackbar queue
     *
     * @param snackyData [SnackyData] that are used to create a custom Snackbar
     */
    public suspend fun showSnackbar(snackyData: SnackyData): Unit =
        mutex.withLock {
            try {
                suspendCancellableCoroutine { continuation ->
                    currentSnackyState = SnackyStateImpl(snackyData, continuation)
                }
            } finally {
                currentSnackyState = null
            }
        }

    private class SnackyStateImpl(
        override val snackyData: SnackyData,
        private val continuation: CancellableContinuation<Unit>,
    ) : SnackyState {

        override fun performMainAction() {
            snackyData.onMainActionCallback()
            if (continuation.isActive) continuation.resume(Unit)
        }

        override fun performSecondaryAction() {
            snackyData.onSecondaryActionCallback()
            if (continuation.isActive) continuation.resume(Unit)
        }

        override fun dismiss() {
            snackyData.onDismissCallback()
            if (continuation.isActive) continuation.resume(Unit)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as SnackyStateImpl

            if (snackyData != other.snackyData) return false
            if (continuation != other.continuation) return false

            return true
        }

        override fun hashCode(): Int {
            var result = snackyData.hashCode()
            result = 31 * result + continuation.hashCode()
            return result
        }
    }
}

@Composable
public fun rememberSnackyHostState(): SnackyHostState = remember { SnackyHostState() }
