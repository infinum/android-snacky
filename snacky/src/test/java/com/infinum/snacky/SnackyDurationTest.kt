package com.infinum.snacky

import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Unit tests for [SnackyDuration] sealed class.
 * Tests the different duration types and their validations.
 */
class SnackyDurationTest {

    @Test
    fun `Short duration should have 4 seconds value`() {
        // Given
        val shortDuration = SnackyDuration.Short

        // Then
        assertEquals(4.seconds, shortDuration.value)
    }

    @Test
    fun `Long duration should have 10 seconds value`() {
        // Given
        val longDuration = SnackyDuration.Long

        // Then
        assertEquals(10.seconds, longDuration.value)
    }

    @Test
    fun `Indefinite duration should have Long MAX_VALUE milliseconds`() {
        // Given
        val indefiniteDuration = SnackyDuration.Indefinite

        // Then
        assertEquals(Long.MAX_VALUE.milliseconds, indefiniteDuration.value)
    }

    @Test
    fun `Custom duration with valid value should be created successfully`() {
        // Given
        val customDuration = 6.seconds

        // When
        val snackyDuration = SnackyDuration.Custom(customDuration)

        // Then
        assertEquals(customDuration, snackyDuration.value)
    }

    @Test
    fun `Custom duration with zero value should throw IllegalArgumentException`() {
        // Given
        val zeroDuration = 0.seconds

        // Then
        assertThrows(IllegalArgumentException::class.java) {
            SnackyDuration.Custom(zeroDuration)
        }
    }

    @Test
    fun `Custom duration with negative value should throw IllegalArgumentException`() {
        // Given
        val negativeDuration = (-5).seconds

        // Then
        assertThrows(IllegalArgumentException::class.java) {
            SnackyDuration.Custom(negativeDuration)
        }
    }

    @Test
    fun `Custom duration with 1 millisecond should be created successfully`() {
        // Given
        val customDuration = 1.milliseconds

        // When
        val snackyDuration = SnackyDuration.Custom(customDuration)

        // Then
        assertEquals(customDuration, snackyDuration.value)
    }

    @Test
    fun `Different duration types should not be equal`() {
        // Given
        val shortDuration = SnackyDuration.Short
        val longDuration = SnackyDuration.Long

        // Then
        assertNotEquals(shortDuration, longDuration)
        assertNotEquals(shortDuration.value, longDuration.value)
    }

    @Test
    fun `Two Custom durations with same value should be equal`() {
        // Given
        val customDuration1 = SnackyDuration.Custom(6.seconds)
        val customDuration2 = SnackyDuration.Custom(6.seconds)

        // Then
        assertEquals(customDuration1, customDuration2)
        assertEquals(customDuration1.value, customDuration2.value)
    }

    @Test
    fun `Two Custom durations with different values should not be equal`() {
        // Given
        val customDuration1 = SnackyDuration.Custom(6.seconds)
        val customDuration2 = SnackyDuration.Custom(8.seconds)

        // Then
        assertNotEquals(customDuration1, customDuration2)
        assertNotEquals(customDuration1.value, customDuration2.value)
    }
}
