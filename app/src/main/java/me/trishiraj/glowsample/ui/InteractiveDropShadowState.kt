package me.trishiraj.glowsample.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import me.trishiraj.shadowglow.ShadowBlurStyle

@Stable
class InteractiveDropShadowState(
    isGradientMode: Boolean,
    shadowColor: Color,
    gradientAlpha: Float,
    borderRadius: Float,
    blurRadius: Float,
    offsetX: Float,
    offsetY: Float,
    spread: Float,
    shadowBlurStyle: ShadowBlurStyle,
    enableGyroParallax: Boolean,
    parallaxSensitivity: Float,
    enableBreathingEffect: Boolean,
    breathingIntensity: Float,
    breathingDuration: Float,
    enableGlowTrail: Boolean,
    glowTrailWidth: Float,
    glowTrailBlur: Float,
    glowTrailLength: Float,
    glowTrailDuration: Float,
    glowTrailClockwise: Boolean,
    glowTrailAlpha: Float
) {
    var isGradientMode by mutableStateOf(isGradientMode)
    var shadowColor by mutableStateOf(shadowColor)
    val gradientColors = listOf(Color.Red.copy(alpha = 0.7f), Color.Blue.copy(alpha = 0.7f))
    var gradientAlpha by mutableFloatStateOf(gradientAlpha)

    var borderRadius by mutableFloatStateOf(borderRadius)
    var blurRadius by mutableFloatStateOf(blurRadius)
    var offsetX by mutableFloatStateOf(offsetX)
    var offsetY by mutableFloatStateOf(offsetY)
    var spread by mutableFloatStateOf(spread)

    var shadowBlurStyle by mutableStateOf(shadowBlurStyle)
    var enableGyroParallax by mutableStateOf(enableGyroParallax)
    var parallaxSensitivity by mutableFloatStateOf(parallaxSensitivity)

    var enableBreathingEffect by mutableStateOf(enableBreathingEffect)
    var breathingIntensity by mutableFloatStateOf(breathingIntensity)
    var breathingDuration by mutableFloatStateOf(breathingDuration)

    var enableGlowTrail by mutableStateOf(enableGlowTrail)
    var glowTrailWidth by mutableFloatStateOf(glowTrailWidth)
    var glowTrailBlur by mutableFloatStateOf(glowTrailBlur)
    var glowTrailLength by mutableFloatStateOf(glowTrailLength)
    var glowTrailDuration by mutableFloatStateOf(glowTrailDuration)
    var glowTrailClockwise by mutableStateOf(glowTrailClockwise)
    var glowTrailAlpha by mutableFloatStateOf(glowTrailAlpha)
}

@Composable
fun rememberInteractiveDropShadowState(
    isGradientMode: Boolean = false,
    shadowColor: Color? = null,
    gradientAlpha: Float = 1.0f,
    borderRadius: Float = 16f,
    blurRadius: Float = 16f,
    offsetX: Float = 0f,
    offsetY: Float = 4f,
    spread: Float = 0f,
    shadowBlurStyle: ShadowBlurStyle = ShadowBlurStyle.NORMAL,
    enableGyroParallax: Boolean = false,
    parallaxSensitivity: Float = 4f,
    enableBreathingEffect: Boolean = false,
    breathingIntensity: Float = 4f,
    breathingDuration: Float = 1500f,
    enableGlowTrail: Boolean = true,
    glowTrailWidth: Float = 10f,
    glowTrailBlur: Float = 20f,
    glowTrailLength: Float = 80f,
    glowTrailDuration: Float = 2800f,
    glowTrailClockwise: Boolean = true,
    glowTrailAlpha: Float = 1f
): InteractiveDropShadowState {
    val isInDarkMode = isSystemInDarkTheme()
    val defaultSolidColor =
        if (isInDarkMode) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f)

    return remember(
        isGradientMode,
        shadowColor,
        gradientAlpha,
        borderRadius,
        blurRadius,
        offsetX,
        offsetY,
        spread,
        shadowBlurStyle,
        enableGyroParallax,
        parallaxSensitivity,
        enableBreathingEffect,
        breathingIntensity,
        breathingDuration,
        enableGlowTrail,
        glowTrailWidth,
        glowTrailBlur,
        glowTrailLength,
        glowTrailDuration,
        glowTrailClockwise,
        glowTrailAlpha,
        isInDarkMode,
        defaultSolidColor,
    ) {
        InteractiveDropShadowState(
            isGradientMode = isGradientMode,
            shadowColor = shadowColor ?: defaultSolidColor,
            gradientAlpha = gradientAlpha,
            borderRadius = borderRadius,
            blurRadius = blurRadius,
            offsetX = offsetX,
            offsetY = offsetY,
            spread = spread,
            shadowBlurStyle = shadowBlurStyle,
            enableGyroParallax = enableGyroParallax,
            parallaxSensitivity = parallaxSensitivity,
            enableBreathingEffect = enableBreathingEffect,
            breathingIntensity = breathingIntensity,
            breathingDuration = breathingDuration,
            enableGlowTrail = enableGlowTrail,
            glowTrailWidth = glowTrailWidth,
            glowTrailBlur = glowTrailBlur,
            glowTrailLength = glowTrailLength,
            glowTrailDuration = glowTrailDuration,
            glowTrailClockwise = glowTrailClockwise,
            glowTrailAlpha = glowTrailAlpha,
        )
    }
}
