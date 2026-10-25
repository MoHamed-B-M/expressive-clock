package app.materialclock.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A filled button whose container morphs between a resting and a pressed shape.
 *
 * Material3 has no public `ButtonWithAnimatedShape` composable in 1.5.0-alpha25: the official API
 * for this effect is the `Button` overload that takes [ButtonShapes], usually fed by
 * [ButtonDefaults.shapes]. This wrapper names that intent for the app and keeps every call site on
 * one default, so the shape pair can be retuned in one place.
 *
 * The morph only animates when both shapes are corner-based; the defaults are. Combined with the
 * regular pressed-state ripple this is the filled-button half of the Expressive press language.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ButtonWithAnimatedShape(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shapes: ButtonShapes = ButtonDefaults.shapes(),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shapes = shapes,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content,
    )
}
