package com.infinum.snacky

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.State
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.util.fastFilterNotNull
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMapTo

/**
 * A modified version of the Material3 Snackbar `FadeInFadeOutWithScale` function,
 * enhanced with an additional `animationSpec` parameter.
 *
 * This composable fades in and out the content with a scale effect.
 *
 * @param current The current item to display
 * @param modifier The modifier to apply to this layout node
 * @param animationSpec The animation spec to use for the fade in and out animations
 * @param content The content to display
 */
@Composable
internal fun FadeInFadeOutWithScale(
    current: SnackyState?,
    modifier: Modifier = Modifier,
    animationSpec: SnackyAnimationSpec = SnackyAnimationSpec(),
    content: @Composable (SnackyState) -> Unit,
) {
    val state = remember { FadeInFadeOutState<SnackyState?>() }
    if (current != state.current) {
        state.current = current
        val keys = state.items.fastMap { it.key }.toMutableList()
        if (!keys.contains(current)) {
            keys.add(current)
        }
        state.items.clear()
        mapKeysToItems(keys = keys, state = state, current = current, animationSpec = animationSpec)
    }
    Box(modifier) {
        state.scope = currentRecomposeScope
        state.items.fastForEach { (item, opacity) -> key(item) { opacity { content(item!!) } } }
    }
}

private fun mapKeysToItems(
    keys: MutableList<SnackyState?>,
    state: FadeInFadeOutState<SnackyState?>,
    current: SnackyState?,
    animationSpec: SnackyAnimationSpec,
) {
    keys.fastFilterNotNull().fastMapTo(state.items) { key ->
        FadeInFadeOutAnimationItem(key) { children ->
            val isVisible = key == current
            val duration = if (isVisible) animationSpec.fadeInDuration else animationSpec.fadeOutDuration
            val delay = animationSpec.fadeOutDuration + animationSpec.inBetweenDelay
            val animationDelay = if (isVisible && keys.fastFilterNotNull().size != 1) delay else 0
            val opacity =
                animatedOpacity(
                    animation = tween(
                        easing = animationSpec.scaleEasing,
                        delayMillis = animationDelay,
                        durationMillis = duration,
                    ),
                    visible = isVisible,
                    onAnimationFinish = {
                        if (key != state.current) {
                            // leave only the current in the list
                            state.items.removeAll { it.key == key }
                            state.scope?.invalidate()
                        }
                    },
                )
            val scale =
                animatedScale(
                    animation = tween(
                        easing = animationSpec.opacityEasing,
                        delayMillis = animationDelay,
                        durationMillis = duration,
                    ),
                    visible = isVisible,
                    scaleFactor = animationSpec.scaleFactor,
                )
            Box(
                Modifier.graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    alpha = opacity.value,
                )
                    .semantics {
                        liveRegion = LiveRegionMode.Polite
                        dismiss {
                            key.dismiss()
                            true
                        }
                    },
            ) {
                children()
            }
        }
    }
}

private class FadeInFadeOutState<T> {
    var current: Any? = Any()
    var items = mutableListOf<FadeInFadeOutAnimationItem<T>>()
    var scope: RecomposeScope? = null
}

private data class FadeInFadeOutAnimationItem<T>(
    val key: T,
    val transition: FadeInFadeOutTransition,
)

private typealias FadeInFadeOutTransition = @Composable (content: @Composable () -> Unit) -> Unit

@Composable
private fun animatedOpacity(
    animation: AnimationSpec<Float>,
    visible: Boolean,
    onAnimationFinish: () -> Unit = {},
): State<Float> {
    val alpha = remember { Animatable(if (!visible) 1f else 0f) }
    LaunchedEffect(visible) {
        alpha.animateTo(if (visible) 1f else 0f, animationSpec = animation)
        onAnimationFinish()
    }
    return alpha.asState()
}

@Composable
private fun animatedScale(animation: AnimationSpec<Float>, visible: Boolean, scaleFactor: Float): State<Float> {
    val scale = remember { Animatable(if (!visible) 1f else scaleFactor) }
    LaunchedEffect(visible) {
        scale.animateTo(if (visible) 1f else scaleFactor, animationSpec = animation)
    }
    return scale.asState()
}
