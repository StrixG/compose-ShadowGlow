package me.trishiraj.glowsample.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.trishiraj.glowsample.ui.InteractiveDropShadowState
import me.trishiraj.shadowglow.ShadowBlurStyle
import kotlin.math.roundToInt

@Composable
fun ConfigurationPanel(state: InteractiveDropShadowState, isInDarkMode: Boolean) {
    Text("Configuration", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(16.dp))

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ControlRow(
            "Gradient Mode:",
            checkbox = true,
            checked = state.isGradientMode,
            onCheckedChange = { state.isGradientMode = it })
        ControlRow(
            "Gyro Parallax:",
            checkbox = true,
            checked = state.enableGyroParallax,
            onCheckedChange = { state.enableGyroParallax = it })
        ControlRow(
            "Breathing Effect:",
            checkbox = true,
            checked = state.enableBreathingEffect,
            onCheckedChange = { state.enableBreathingEffect = it })
        ControlRow(
            "Enable Glow Trail",
            checkbox = true,
            checked = state.enableGlowTrail,
            onCheckedChange = { state.enableGlowTrail = it })

        SliderControl(
            "Border Radius: ${state.borderRadius.roundToInt()}dp",
            state.borderRadius,
            0f,
            50f
        ) { state.borderRadius = it }
        SliderControl(
            "Base Blur Radius: ${state.blurRadius.roundToInt()}dp",
            state.blurRadius,
            0f,
            50f
        ) { state.blurRadius = it }
        SliderControl("Offset X: ${state.offsetX.roundToInt()}dp", state.offsetX, -50f, 50f) {
            state.offsetX = it
        }
        SliderControl("Offset Y: ${state.offsetY.roundToInt()}dp", state.offsetY, -50f, 50f) {
            state.offsetY = it
        }
        SliderControl("Spread: ${state.spread.roundToInt()}dp", state.spread, 0f, 30f) { state.spread = it }

        if (state.isGradientMode) {
            SliderControl(
                "Gradient Alpha: ${"%.2f".format(state.gradientAlpha)}",
                state.gradientAlpha,
                0f,
                1f
            ) { state.gradientAlpha = it }
        }
        if (state.enableGyroParallax) {
            SliderControl(
                "Parallax Sensitivity: ${state.parallaxSensitivity.roundToInt()}dp",
                state.parallaxSensitivity,
                0f,
                20f
            ) { state.parallaxSensitivity = it }
        }
        if (state.enableBreathingEffect) {
            SliderControl(
                "Breathing Intensity: ${state.breathingIntensity.roundToInt()}dp",
                state.breathingIntensity,
                0f,
                20f
            ) { state.breathingIntensity = it }
            SliderControl(
                "Breathing Duration: ${state.breathingDuration.toInt()}ms",
                state.breathingDuration,
                500f,
                5000f
            ) { state.breathingDuration = it }
        }
        if (state.enableGlowTrail) {
            ControlRow(
                "clockwise",
                checkbox = true,
                checked = state.glowTrailClockwise,
                onCheckedChange = { state.glowTrailClockwise = it })

            SliderControl(
                "Trail Length: ${state.glowTrailLength.roundToInt()}",
                state.glowTrailLength,
                10f,
                360f
            ) { state.glowTrailLength = it }

            SliderControl(
                "Trail Width: ${state.glowTrailWidth.roundToInt()}",
                state.glowTrailWidth,
                2f,
                30f
            ) { state.glowTrailWidth = it }

            SliderControl(
                "Trail Duration: ${state.glowTrailDuration.roundToInt()}ms",
                state.glowTrailDuration,
                500f,
                6000f
            ) { state.glowTrailDuration = it }

            SliderControl(
                "Trail Alpha: ${"%.2f".format(state.glowTrailAlpha)}",
                state.glowTrailAlpha,
                0.1f,
                1f
            ) { state.glowTrailAlpha = it }
        }

        Text("Blur Style:", style = MaterialTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShadowBlurStyle.entries.forEach { style ->
                Button(
                    onClick = { state.shadowBlurStyle = style },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.shadowBlurStyle == style) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(style.name, fontSize = 10.sp)
                }
            }
        }

        if (!state.isGradientMode) {
            val defaultSolidColor = if (isInDarkMode) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f)
            Text("Shadow Color:", style = MaterialTheme.typography.bodyMedium)
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val adaptiveBlack =
                    if (isInDarkMode) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f)
                val commonColors = mapOf(
                    "Default" to adaptiveBlack,
                    "Red" to Color.Red.copy(alpha = 0.5f),
                    "Blue" to Color.Blue.copy(alpha = 0.5f),
                    "Green" to Color.Green.copy(alpha = 0.6f)
                )
                LaunchedEffect(isInDarkMode, defaultSolidColor) {
                    if (state.shadowColor != defaultSolidColor && (state.shadowColor == Color.Black.copy(
                            alpha = 0.4f
                        ) || state.shadowColor == Color.White.copy(alpha = 0.5f))
                    ) {
                        state.shadowColor = defaultSolidColor
                    }
                }

                commonColors.forEach { (name, color) ->
                    Button(
                        onClick = { state.shadowColor = color },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.shadowColor == color) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(name, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}