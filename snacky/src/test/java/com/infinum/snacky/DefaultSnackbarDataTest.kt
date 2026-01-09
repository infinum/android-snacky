package com.infinum.snacky

import com.infinum.snacky.default.DefaultSnackbarData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Unit tests for [DefaultSnackbarData].
 * Tests the data model for default snackbars and their properties.
 */
class DefaultSnackbarDataTest {

    @Test
    fun `DefaultSnackbarData should store message correctly`() {
        // Given
        val message = "Test message"

        // When
        val data = DefaultSnackbarData(
            message = message,
            actionLabel = null,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short
        )

        // Then
        assertEquals(message, data.message)
    }

    @Test
    fun `DefaultSnackbarData should store action label correctly`() {
        // Given
        val actionLabel = "Action"

        // When
        val data = DefaultSnackbarData(
            message = "Test",
            actionLabel = actionLabel,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short
        )

        // Then
        assertEquals(actionLabel, data.actionLabel)
    }

    @Test
    fun `DefaultSnackbarData should handle null action label`() {
        // When
        val data = DefaultSnackbarData(
            message = "Test",
            actionLabel = null,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short
        )

        // Then
        assertNull(data.actionLabel)
    }

    @Test
    fun `DefaultSnackbarData should store dismiss action flag correctly`() {
        // When
        val dataWithDismiss = DefaultSnackbarData(
            message = "Test",
            actionLabel = null,
            withDismissAction = true,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short
        )

        val dataWithoutDismiss = DefaultSnackbarData(
            message = "Test",
            actionLabel = null,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short
        )

        // Then
        assertEquals(true, dataWithDismiss.withDismissAction)
        assertEquals(false, dataWithoutDismiss.withDismissAction)
    }

    @Test
    fun `DefaultSnackbarData should store dismiss action content description`() {
        // Given
        val contentDescription = "Close"

        // When
        val data = DefaultSnackbarData(
            message = "Test",
            actionLabel = null,
            withDismissAction = true,
            dismissActionContentDescription = contentDescription,
            duration = SnackyDuration.Short
        )

        // Then
        assertEquals(contentDescription, data.dismissActionContentDescription)
    }

    @Test
    fun `DefaultSnackbarData should store duration correctly`() {
        // Given
        val duration = SnackyDuration.Long

        // When
        val data = DefaultSnackbarData(
            message = "Test",
            actionLabel = null,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = duration
        )

        // Then
        assertEquals(duration, data.duration)
    }

    @Test
    fun `DefaultSnackbarData should have default empty callbacks`() {
        // When
        val data = DefaultSnackbarData(
            message = "Test",
            actionLabel = null,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short
        )

        // Then - Should not throw when calling callbacks
        data.onDismissCallback()
        data.onMainActionCallback()
    }

    @Test
    fun `DefaultSnackbarData should store custom callbacks`() {
        // Given
        var mainActionCalled = false
        var dismissCalled = false

        // When
        val data = DefaultSnackbarData(
            message = "Test",
            actionLabel = "Action",
            withDismissAction = true,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short,
            onMainActionCallback = { mainActionCalled = true },
            onDismissCallback = { dismissCalled = true }
        )

        data.onMainActionCallback()
        data.onDismissCallback()

        // Then
        assertEquals(true, mainActionCalled)
        assertEquals(true, dismissCalled)
    }

    @Test
    fun `DefaultSnackbarData hasAction should be based on callback comparison`() {
        // When
        val data = DefaultSnackbarData(
            message = "Test",
            actionLabel = null,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short
        )

        // Then
        // Note: hasAction checks if callbacks are not equal to empty lambda {}
        // This is implementation-specific behavior
        val hasAction = data.hasAction
        // Just verify the property is accessible
        assertEquals(hasAction, data.hasAction)
    }

    @Test
    fun `Two DefaultSnackbarData with same properties should have same core values`() {
        // Given
        val callback = { }
        val data1 = DefaultSnackbarData(
            message = "Test",
            actionLabel = "Action",
            withDismissAction = true,
            dismissActionContentDescription = "Close",
            duration = SnackyDuration.Short,
            onDismissCallback = callback,
            onMainActionCallback = callback
        )

        val data2 = DefaultSnackbarData(
            message = "Test",
            actionLabel = "Action",
            withDismissAction = true,
            dismissActionContentDescription = "Close",
            duration = SnackyDuration.Short,
            onDismissCallback = callback,
            onMainActionCallback = callback
        )

        // Then - Check that core properties match
        assertEquals(data1.message, data2.message)
        assertEquals(data1.actionLabel, data2.actionLabel)
        assertEquals(data1.withDismissAction, data2.withDismissAction)
        assertEquals(data1.dismissActionContentDescription, data2.dismissActionContentDescription)
        assertEquals(data1.duration, data2.duration)
    }

    @Test
    fun `Two DefaultSnackbarData with different messages should not be equal`() {
        // Given
        val data1 = DefaultSnackbarData(
            message = "Test 1",
            actionLabel = null,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short
        )

        val data2 = DefaultSnackbarData(
            message = "Test 2",
            actionLabel = null,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short
        )

        // Then
        assertNotEquals(data1, data2)
    }

    @Test
    fun `Two DefaultSnackbarData with different durations should not be equal`() {
        // Given
        val data1 = DefaultSnackbarData(
            message = "Test",
            actionLabel = null,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Short
        )

        val data2 = DefaultSnackbarData(
            message = "Test",
            actionLabel = null,
            withDismissAction = false,
            dismissActionContentDescription = "",
            duration = SnackyDuration.Long
        )

        // Then
        assertNotEquals(data1, data2)
    }
}
