package me.trishiraj.glowsample.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.trishiraj.glowsample.ui.component.ConfigurationPanel
import me.trishiraj.glowsample.ui.theme.GlowSampleTheme
import me.trishiraj.shadowglow.shadowGlow
import kotlin.math.roundToInt

@Composable
fun InteractiveDropShadowScreen(modifier: Modifier = Modifier) {
    val state = rememberInteractiveDropShadowState()
    val isInDarkMode = isSystemInDarkTheme()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShadowedCard(state)
        Spacer(Modifier.height(24.dp))
        ConfigurationPanel(state, isInDarkMode)
    }
}

@Composable
private fun ShadowedCard(state: InteractiveDropShadowState) {
    val dynamicModifier = if (state.isGradientMode) {
        Modifier.shadowGlow(
            gradientColors = state.gradientColors,
            alpha = state.gradientAlpha,
            borderRadius = state.borderRadius.dp,
            blurRadius = state.blurRadius.dp,
            offsetX = state.offsetX.dp,
            offsetY = state.offsetY.dp,
            spread = state.spread.dp,
            blurStyle = state.shadowBlurStyle,
            enableGyroParallax = state.enableGyroParallax,
            parallaxSensitivity = state.parallaxSensitivity.dp,
            enableBreathingEffect = state.enableBreathingEffect,
            breathingEffectIntensity = state.breathingIntensity.dp,
            breathingDurationMillis = state.breathingDuration.toInt(),
            enableGlowTrail = state.enableGlowTrail,
            glowTrailWidth = state.glowTrailWidth.dp,
            glowTrailBlurRadius = state.glowTrailBlur.dp,
            glowTrailLengthDegrees = state.glowTrailLength,
            glowTrailDurationMillis = state.glowTrailDuration.toInt(),
            glowTrailClockwise = state.glowTrailClockwise,
            glowTrailAlpha = state.glowTrailAlpha
        )
    } else {
        Modifier.shadowGlow(
            color = state.shadowColor,
            borderRadius = state.borderRadius.dp,
            blurRadius = state.blurRadius.dp,
            offsetX = state.offsetX.dp,
            offsetY = state.offsetY.dp,
            spread = state.spread.dp,
            blurStyle = state.shadowBlurStyle,
            enableGyroParallax = state.enableGyroParallax,
            parallaxSensitivity = state.parallaxSensitivity.dp,
            enableBreathingEffect = state.enableBreathingEffect,
            breathingEffectIntensity = state.breathingIntensity.dp,
            breathingDurationMillis = state.breathingDuration.toInt(),
            enableGlowTrail = state.enableGlowTrail,
            glowTrailWidth = state.glowTrailWidth.dp,
            glowTrailBlurRadius = state.glowTrailBlur.dp,
            glowTrailLengthDegrees = state.glowTrailLength,
            glowTrailDurationMillis = state.glowTrailDuration.toInt(),
            glowTrailClockwise = state.glowTrailClockwise,
            glowTrailAlpha = state.glowTrailAlpha
        )
    }

    Card(
        modifier = dynamicModifier
            .fillMaxWidth(0.8f)
            .height(180.dp),
        shape = RoundedCornerShape(state.borderRadius.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Shadowed Card", fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (state.isGradientMode) "Mode: Gradient" else "Mode: Solid Color",
                fontSize = 12.sp
            )
            Text(
                "Base Blur: ${state.blurRadius.roundToInt()}dp, Style: ${state.shadowBlurStyle}",
                fontSize = 12.sp
            )
            if (state.enableGyroParallax) {
                Text(
                    "Gyro Parallax: Enabled (${state.parallaxSensitivity.roundToInt()}dp)",
                    fontSize = 12.sp
                )
            }
            if (state.enableBreathingEffect) {
                Text(
                    "Breathing Effect: Enabled (Intensity: ${state.breathingIntensity.roundToInt()}dp, Duration: ${state.breathingDuration.toInt()}ms)",
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun InteractiveDropShadowScreenPreview() {
    GlowSampleTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            InteractiveDropShadowScreen()
        }
    }
}