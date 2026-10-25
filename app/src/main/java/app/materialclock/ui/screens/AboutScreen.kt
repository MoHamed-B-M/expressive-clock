package app.materialclock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.materialclock.BuildConfig
import app.materialclock.R
import app.materialclock.ui.bounceClick
import app.materialclock.ui.rememberBounceInteractionSource

/**
 * The About destination, reached from Settings.
 *
 * The content here is the app's identity rather than a repeat of the older About section in the
 * Stopwatch settings sheet, which still owns the detailed privacy, permission and licence pages.
 * This screen answers three questions and then gets out of the way: what is this, which build is
 * it, and where does the data go. The back arrow in the top bar is the primary affordance; the
 * large tonal button repeats it at the bottom of the scroll, and [androidx.activity.compose.BackHandler]
 * in the app shell makes the system gesture agree with both.
 */
@Composable
fun AboutScreen(
    contentPadding: PaddingValues,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    val bounce = rememberBounceInteractionSource()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // One large floating card, the same surface role as an alarm tile, so About belongs to the
        // same app rather than looking like a system-generated page.
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            color = scheme.surfaceContainer,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(scheme.tertiaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = scheme.onTertiaryContainer,
                        modifier = Modifier.size(44.dp),
                    )
                }
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLargeEmphasized,
                    color = scheme.onSurface,
                )
                Text(
                    text = "Version ${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.titleMedium,
                    color = scheme.onSurfaceVariant,
                )
                Text(
                    text = "A Material 3 Expressive clock for alarms, world time, timers and " +
                        "stopwatch. Everything is computed and stored on your device: no accounts, " +
                        "no telemetry and no network access.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = scheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "No accounts · No telemetry · No network",
                    style = MaterialTheme.typography.labelLargeEmphasized,
                    color = scheme.primary,
                    textAlign = TextAlign.Center,
                )
            }
        }

        FilledTonalButton(
            onClick = onBack,
            interactionSource = bounce,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .bounceClick(bounce),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text("Back", style = MaterialTheme.typography.titleMediumEmphasized)
        }
    }
}
