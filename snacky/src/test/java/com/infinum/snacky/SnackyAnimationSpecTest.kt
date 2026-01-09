package com.infinum.snacky

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Unit tests for [SnackyAnimationSpec].
 * Tests animation configuration and default values.
 */
class SnackyAnimationSpecTest {

    @Test
    fun `Default SnackyAnimationSpec should have correct default values`() {
        // When
        val spec = SnackyAnimationSpec()

        // Then
        assertEquals(150, spec.fadeInDuration)
        assertEquals(75, spec.fadeOutDuration)
        assertEquals(0, spec.inBetweenDelay)
        assertEquals(0.8f, spec.scaleFactor, 0.001f)
        assertEquals(LinearEasing, spec.scaleEasing)
        assertEquals(FastOutSlowInEasing, spec.opacityEasing)
    }

    @Test
    fun `SnackyAnimationSpec should accept custom fadeInDuration`() {
        // Given
        val customFadeIn = 200

        // When
        val spec = SnackyAnimationSpec(fadeInDuration = customFadeIn)

        // Then
        assertEquals(customFadeIn, spec.fadeInDuration)
    }

    @Test
    fun `SnackyAnimationSpec should accept custom fadeOutDuration`() {
        // Given
        val customFadeOut = 100

        // When
        val spec = SnackyAnimationSpec(fadeOutDuration = customFadeOut)

        // Then
        assertEquals(customFadeOut, spec.fadeOutDuration)
    }

    @Test
    fun `SnackyAnimationSpec should accept custom inBetweenDelay`() {
        // Given
        val customDelay = 50

        // When
        val spec = SnackyAnimationSpec(inBetweenDelay = customDelay)

        // Then
        assertEquals(customDelay, spec.inBetweenDelay)
    }

    @Test
    fun `SnackyAnimationSpec should accept custom scaleFactor`() {
        // Given
        val customScale = 0.5f

        // When
        val spec = SnackyAnimationSpec(scaleFactor = customScale)

        // Then
        assertEquals(customScale, spec.scaleFactor, 0.001f)
    }

    @Test
    fun `SnackyAnimationSpec should accept custom scaleEasing`() {
        // Given
        val customEasing = LinearOutSlowInEasing

        // When
        val spec = SnackyAnimationSpec(scaleEasing = customEasing)

        // Then
        assertEquals(customEasing, spec.scaleEasing)
    }

    @Test
    fun `SnackyAnimationSpec should accept custom opacityEasing`() {
        // Given
        val customEasing = LinearEasing

        // When
        val spec = SnackyAnimationSpec(opacityEasing = customEasing)

        // Then
        assertEquals(customEasing, spec.opacityEasing)
    }

    @Test
    fun `SnackyAnimationSpec should accept all custom values`() {
        // Given
        val customFadeIn = 200
        val customFadeOut = 100
        val customDelay = 50
        val customScale = 0.5f
        val customScaleEasing = LinearOutSlowInEasing
        val customOpacityEasing = LinearEasing

        // When
        val spec = SnackyAnimationSpec(
            fadeInDuration = customFadeIn,
            fadeOutDuration = customFadeOut,
            inBetweenDelay = customDelay,
            scaleFactor = customScale,
            scaleEasing = customScaleEasing,
            opacityEasing = customOpacityEasing
        )

        // Then
        assertEquals(customFadeIn, spec.fadeInDuration)
        assertEquals(customFadeOut, spec.fadeOutDuration)
        assertEquals(customDelay, spec.inBetweenDelay)
        assertEquals(customScale, spec.scaleFactor, 0.001f)
        assertEquals(customScaleEasing, spec.scaleEasing)
        assertEquals(customOpacityEasing, spec.opacityEasing)
    }

    @Test
    fun `Two SnackyAnimationSpec with same values should be equal`() {
        // Given
        val spec1 = SnackyAnimationSpec(
            fadeInDuration = 200,
            fadeOutDuration = 100,
            scaleFactor = 0.5f
        )

        val spec2 = SnackyAnimationSpec(
            fadeInDuration = 200,
            fadeOutDuration = 100,
            scaleFactor = 0.5f
        )

        // Then
        assertEquals(spec1, spec2)
    }

    @Test
    fun `Two SnackyAnimationSpec with different fadeInDuration should not be equal`() {
        // Given
        val spec1 = SnackyAnimationSpec(fadeInDuration = 200)
        val spec2 = SnackyAnimationSpec(fadeInDuration = 150)

        // Then
        assertNotEquals(spec1, spec2)
    }

    @Test
    fun `Two SnackyAnimationSpec with different scaleFactor should not be equal`() {
        // Given
        val spec1 = SnackyAnimationSpec(scaleFactor = 0.8f)
        val spec2 = SnackyAnimationSpec(scaleFactor = 0.5f)

        // Then
        assertNotEquals(spec1, spec2)
    }

    @Test
    fun `SnackyAnimationSpec should support zero scaleFactor`() {
        // When
        val spec = SnackyAnimationSpec(scaleFactor = 0f)

        // Then
        assertEquals(0f, spec.scaleFactor, 0.001f)
    }

    @Test
    fun `SnackyAnimationSpec should support scaleFactor of 1`() {
        // When
        val spec = SnackyAnimationSpec(scaleFactor = 1f)

        // Then
        assertEquals(1f, spec.scaleFactor, 0.001f)
    }

    @Test
    fun `SnackyAnimationSpec should support zero durations`() {
        // When
        val spec = SnackyAnimationSpec(
            fadeInDuration = 0,
            fadeOutDuration = 0,
            inBetweenDelay = 0
        )

        // Then
        assertEquals(0, spec.fadeInDuration)
        assertEquals(0, spec.fadeOutDuration)
        assertEquals(0, spec.inBetweenDelay)
    }

    @Test
    fun `Copy of SnackyAnimationSpec should create new instance with updated values`() {
        // Given
        val original = SnackyAnimationSpec(fadeInDuration = 150)

        // When
        val copy = original.copy(fadeInDuration = 200)

        // Then
        assertEquals(150, original.fadeInDuration)
        assertEquals(200, copy.fadeInDuration)
        assertNotEquals(original, copy)
    }
}
