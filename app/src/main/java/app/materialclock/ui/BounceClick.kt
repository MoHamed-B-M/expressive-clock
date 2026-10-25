package app.materialclock.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * M3 Expressive press feedback: the control shrinks while held and springs back with one visible
 * overshoot when released.
 *
 * ## Why the interaction source is a parameter
 *
 * A `Modifier` cannot observe the press stream of a Material button it is attached to: the button
 * owns its own `clickable`. Passing one [MutableInteractionSource] to both the button's
 * `interactionSource` parameter and to [bounceClick] makes the real press state the only animation
 * input, with no second gesture detector competing for the click. For button-like surfaces built
 * from `Surface(onClick = ...)`, the same source goes to `Surface`'s `interactionSource`.
 *
 * Only `graphicsLayer` is added, so the component keeps its ripple and its layout size; the scale is
 * visual, not a hit-test change.
 */
@Composable
fun rememberBounceInteractionSource(): MutableInteractionSource =
    remember { MutableInteractionSource() }

/**
 * The bounce itself.
 *
 * 0.45 damping is under-damped enough to overshoot once on release without ringing; stiffness 900
 * gets the press down and the rebound finished in roughly the time an Expressive press lasts. The
 * 0.001 visibility threshold lets the spring settle fully before it is considered idle.
 *
 * `composed` keeps the animation's state inside the modifier's own small composition, so a press
 * does not recompose the button's content; [graphicsLayer] keeps the work on the render layer.
 */
fun Modifier.bounceClick(
    interactionSource: MutableInteractionSource,
    pressedScale: Float = 0.9f,
): Modifier = composed {
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = 0.45f,
            stiffness = 900f,
            visibilityThreshold = 0.001f,
        ),
        label = "bounceClickScale",
    )
    graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}
