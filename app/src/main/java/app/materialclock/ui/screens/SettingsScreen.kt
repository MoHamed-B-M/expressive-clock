package app.materialclock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.materialclock.BuildConfig
import app.materialclock.ui.bounceClick
import app.materialclock.ui.rememberBounceInteractionSource

/**
 * The Settings destination reached from the gear on the Alarms tab.
 *
 * ## Why this is a screen and not another sheet
 *
 * Every existing preference still lives in its tab's bottom sheet, opened by tapping that tab's
 * title; those sheets are untouched. This screen is the requested single front door, so it is
 * deliberately shallow: three large rows, each one thing, no nested scrollable menus. Theme and
 * Alarm style are placeholders until their real controls land, and they say so in the supporting
 * text rather than pretending to work; tapping them reports the same thing through the app's
 * snackbar.
 *
 * ## Why it is built from [ListItem] inside a [Surface]
 *
 * The row shape is the app's existing list vocabulary (the settings sheets are all [ListItem]s),
 * lifted onto a floating tonal card so it reads as a screen rather than a sheet. Colour, type and
 * geometry all come from the theme: [MaterialTheme.shapes.extraLarge] is the same large, pill-like
 * corner the floating dock, the alarm tiles and the add button use, and nothing here names a
 * colour or a type size of its own.
 */
@Composable
fun SettingsScreen(
    contentPadding: PaddingValues,
    onTheme: () -> Unit,
    onAlarmStyle: () -> Unit,
    onAbout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { SettingsSection("Appearance") }
        item {
            SettingsCard(
                icon = Icons.Outlined.Palette,
                title = "Theme",
                subtitle = "Colour, contrast and light/dark · coming soon",
                onClick = onTheme,
            )
        }
        item {
            SettingsCard(
                icon = Icons.Outlined.Alarm,
                title = "Alarm style",
                subtitle = "How the alarm tiles and dial look · coming soon",
                onClick = onAlarmStyle,
            )
        }

        item { SettingsSection("About") }
        item {
            SettingsCard(
                icon = Icons.Outlined.Info,
                title = "About",
                subtitle = "Clock ${BuildConfig.VERSION_NAME}",
                onClick = onAbout,
            )
        }
    }
}

@Composable
private fun SettingsSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLargeEmphasized,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
    )
}

/**
 * One floating settings row: tonal icon in a circle, an emphasized title, a quiet explanation, and
 * a trailing chevron that only points one way.
 *
 * The whole card is the target, not the row inside it. [Surface]'s clickable overload supplies the
 * ripple, the minimum interactive size and the button role for a screen reader, so the [ListItem]
 * itself stays a layout rather than a second, invisible control.
 */
@Composable
private fun SettingsCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val bounce = rememberBounceInteractionSource()
    Surface(
        onClick = onClick,
        interactionSource = bounce,
        modifier = Modifier.fillMaxWidth().bounceClick(bounce),
        shape = MaterialTheme.shapes.extraLarge,
        color = scheme.surfaceContainer,
    ) {
        ListItem(
            headlineContent = {
                Text(title, style = MaterialTheme.typography.titleMediumEmphasized)
            },
            supportingContent = {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = scheme.onSurfaceVariant,
                )
            },
            leadingContent = {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(scheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = scheme.onPrimaryContainer,
                        modifier = Modifier.size(22.dp),
                    )
                }
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = scheme.onSurfaceVariant,
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        )
    }
}
