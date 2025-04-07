package com.infinum.snacky

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Represents the duration of a Snackbar.
 */
public sealed class SnackyDuration(public val value: Duration) {
    public data object Short : SnackyDuration(SNACKBAR_SHORT_DURATION)
    public data object Long : SnackyDuration(SNACKBAR_LONG_DURATION)
    public data object Indefinite : SnackyDuration(SNACKBAR_INDEFINITE_DURATION.toDuration(DurationUnit.MILLISECONDS))

    public data class Custom(val duration: Duration) : SnackyDuration(duration) {
        init {
            require(duration > 0.seconds) { "Snackbar duration must be greater than 0 seconds." }
        }
    }
}

private val SNACKBAR_SHORT_DURATION = 4.seconds
private val SNACKBAR_LONG_DURATION = 10.seconds
private const val SNACKBAR_INDEFINITE_DURATION = Long.MAX_VALUE
