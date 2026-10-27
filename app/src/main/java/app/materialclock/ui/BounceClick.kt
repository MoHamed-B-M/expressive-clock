package app.materialclock.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay

/**
 * M3 Expressive press feedback: the control shrinks while held and springs back with one visible
 * overshoot when released.
 *
 * ## Why the interaction source is a parameter
 *
 * A `Modifier` cannot observe the press stream of a Material button it is attached to: the button
 * owns its own `clickable`. Passing one [MutableInteractionSource] to both the button's
 * `interactionSource` parameter and to [bounceClick] makes the real press state the only animation
 * input, shared between the visual bounce, animated container shapes, and connected button groups.
 *
 * ## Immediate touch-down gesture response
 *
 * Standard clickables delay emitting [PressInteraction.Press] until a tap-timeout window expires
 * in order to disambiguate scrolls from clicks. To ensure animations (bounce scale, ButtonWithAnimatedShape
 * shape morph, and ButtonGroup neighbor squish) trigger instantly when a finger touches down, this
 * modifier attaches a low-level [pointerInput] detector using [awaitFirstDown] (`requireUnconsumed = false`).
 * It emits [PressInteraction.Press] immediately on touch down into [interactionSource] and releases or
 * cancels it on lift without consuming pointer events or interfering with high-level click handling.
 */
@Composable
fun rememberBounceInteractionSource(): MutableInteractionSource =
    remember { MutableInteractionSource() }

/**
 * The bounce itself.
 *
 * Uses [Animatable] with [LaunchedEffect] to ensure the press animation triggers immediately on
 * touch-down and has enough time to be visible even for quick taps.
 */
fun Modifier.bounceClick(
    interactionSource: MutableInteractionSource,
    pressedScale: Float = 0.9f,
    enabled: Boolean = true,
): Modifier = composed {
    val pressed by interactionSource.collectIsPressedAsState()
    val scale = remember { Animatable(1f) }

    LaunchedEffect(pressed, enabled) {
        if (pressed && enabled) {
            scale.snapTo(1f)
            scale.animateTo(
                targetValue = pressedScale,
                animationSpec = spring(
                    dampingRatio = 0.45f,
                    stiffness = 900f,
                ),
            )
        } else if (enabled) {
            // Add a small delay to ensure quick taps are visible
            delay(40)
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = 0.45f,
                    stiffness = 900f,
                ),
            )
        } else {
            scale.snapTo(1f)
        }
    }

    if (!enabled) {
        return@composed this
    }
    this
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
        .pointerInput(interactionSource) {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                val press = PressInteraction.Press(down.position)
                var released = false
                try {
                    interactionSource.tryEmit(press)
                    val upOrCancel = waitForUpOrCancellation()
                    released = true
                    if (upOrCancel != null) {
                        interactionSource.tryEmit(PressInteraction.Release(press))
                    } else {
                        interactionSource.tryEmit(PressInteraction.Cancel(press))
                    }
                } finally {
                    if (!released) {
                        interactionSource.tryEmit(PressInteraction.Cancel(press))
                    }
                }
            }
        }
}
