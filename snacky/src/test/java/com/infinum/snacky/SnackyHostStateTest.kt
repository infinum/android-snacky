package com.infinum.snacky

import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [SnackyHostState].
 * Tests snackbar state management, queueing, and callback invocations.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SnackyHostStateTest {

    private lateinit var hostState: SnackyHostState

    @Before
    fun setup() {
        hostState = SnackyHostState()
    }

    @Test
    fun `Initial state should have no current snackbar`() {
        // Then
        assertNull(hostState.currentSnackyState)
    }

    @Test
    fun `showSnackbar should display default snackbar with message`() = runTest {
        // Given
        val message = "Test message"

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(message = message)
        }

        // Then
        assertNotNull(hostState.currentSnackyState)
        val snackyData = hostState.currentSnackyState?.snackyData
        assertNotNull(snackyData)

        // Dismiss to complete the job
        hostState.currentSnackyState?.dismiss()
        job.join()
    }

    @Test
    fun `showSnackbar should set correct duration when actionLabel is null`() = runTest {
        // Given
        val message = "Test message"

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(message = message)
        }

        // Then
        val snackyData = hostState.currentSnackyState?.snackyData
        assertEquals(SnackyDuration.Short, snackyData?.duration)

        // Cleanup
        hostState.currentSnackyState?.dismiss()
        job.join()
    }

    @Test
    fun `showSnackbar should set indefinite duration when actionLabel is provided`() = runTest {
        // Given
        val message = "Test message"
        val actionLabel = "Action"

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(
                message = message,
                actionLabel = actionLabel
            )
        }

        // Then
        val snackyData = hostState.currentSnackyState?.snackyData
        assertEquals(SnackyDuration.Indefinite, snackyData?.duration)

        // Cleanup
        hostState.currentSnackyState?.dismiss()
        job.join()
    }

    @Test
    fun `showSnackbar should use custom duration when provided`() = runTest {
        // Given
        val message = "Test message"
        val customDuration = SnackyDuration.Long

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(
                message = message,
                duration = customDuration
            )
        }

        // Then
        val snackyData = hostState.currentSnackyState?.snackyData
        assertEquals(customDuration, snackyData?.duration)

        // Cleanup
        hostState.currentSnackyState?.dismiss()
        job.join()
    }

    @Test
    fun `performMainAction should invoke onMainActionCallback`() = runTest {
        // Given
        val message = "Test message"
        val actionCallback = mockk<() -> Unit>(relaxed = true)

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(
                message = message,
                onActionCallback = actionCallback
            )
        }

        // Then
        assertNotNull(hostState.currentSnackyState)
        hostState.currentSnackyState?.performMainAction()

        // Verify callback was invoked
        verify { actionCallback() }
        job.join()
    }

    @Test
    fun `performSecondaryAction should invoke onSecondaryActionCallback`() = runTest {
        // Given
        val secondaryActionCallback = mockk<() -> Unit>(relaxed = true)
        val customData = TestSnackyData(
            duration = SnackyDuration.Short,
            onDismissCallback = {},
            onSecondaryActionCallback = secondaryActionCallback
        )

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(customData)
        }

        // Then
        assertNotNull(hostState.currentSnackyState)
        hostState.currentSnackyState?.performSecondaryAction()

        // Verify callback was invoked
        verify { secondaryActionCallback() }
        job.join()
    }

    @Test
    fun `dismiss should invoke onDismissCallback`() = runTest {
        // Given
        val message = "Test message"
        val dismissCallback = mockk<() -> Unit>(relaxed = true)

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(
                message = message,
                onDismissCallback = dismissCallback
            )
        }

        // Then
        assertNotNull(hostState.currentSnackyState)
        hostState.currentSnackyState?.dismiss()

        // Verify callback was invoked
        verify { dismissCallback() }
        job.join()
    }

    @Test
    fun `showSnackbar should clear state after dismissal`() = runTest {
        // Given
        val message = "Test message"

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(message = message)
        }

        // Dismiss the snackbar
        hostState.currentSnackyState?.dismiss()
        job.join()

        // Then
        assertNull(hostState.currentSnackyState)
    }

    @Test
    fun `showSnackbar should display custom SnackyData`() = runTest {
        // Given
        val customData = TestSnackyData(
            duration = SnackyDuration.Short,
            onDismissCallback = {}
        )

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(customData)
        }

        // Then
        assertNotNull(hostState.currentSnackyState)
        assertEquals(customData, hostState.currentSnackyState?.snackyData)

        // Cleanup
        hostState.currentSnackyState?.dismiss()
        job.join()
    }

    @Test
    fun `Multiple snackbars should queue properly`() = runTest {
        // Given
        val message1 = "First message"
        val message2 = "Second message"
        var firstSnackbarShown = false
        var secondSnackbarShown = false

        // When - Launch two snackbars
        val job1 = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(message = message1)
            firstSnackbarShown = true
        }

        val job2 = launch(UnconfinedTestDispatcher(testScheduler)) {
            hostState.showSnackbar(message = message2)
            secondSnackbarShown = true
        }

        // Then - First snackbar should be showing
        assertNotNull(hostState.currentSnackyState)

        // Dismiss first snackbar
        hostState.currentSnackyState?.dismiss()
        job1.join()

        // Second snackbar should now be showing
        assertNotNull(hostState.currentSnackyState)

        // Dismiss second snackbar
        hostState.currentSnackyState?.dismiss()
        job2.join()

        // Verify both were shown
        assertEquals(true, firstSnackbarShown)
        assertEquals(true, secondSnackbarShown)
        assertNull(hostState.currentSnackyState)
    }

    /**
     * Test implementation of [SnackyData] for testing purposes
     */
    private data class TestSnackyData(
        override val duration: SnackyDuration,
        override val onDismissCallback: () -> Unit,
        override val onMainActionCallback: () -> Unit = {},
        override val onSecondaryActionCallback: () -> Unit = {}
    ) : SnackyData {
        @androidx.compose.runtime.Composable
        override fun Render(snackbarState: SnackyState) {
            // No-op for testing
        }
    }
}
