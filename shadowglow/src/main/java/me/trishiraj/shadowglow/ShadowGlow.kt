package me.trishiraj.shadowglow

import android.graphics.BlurMaskFilter
import android.graphics.LinearGradient as AndroidLinearGradient
import android.graphics.Paint as AndroidPaint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toAndroidTileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Applies a drop shadow effect to the composable using a solid color.
 *
 * @param color The color of the shadow.
 * @param borderRadius The radius of the shadow's corners.
 * @param blurRadius The base blur radius of the shadow.
 * @param offsetX The static horizontal offset of the shadow.
 * @param offsetY The static vertical offset of the shadow.
 * @param spread The amount to expand the shadow's bounds before blurring.
 * @param blurStyle The style of the blur effect.
 * @param enableGyroParallax If true, enables a parallax effect on the shadow based on device orientation.
 * @param parallaxSensitivity The maximum displacement for the gyroscope-driven parallax effect.
 * @param enableBreathingEffect If true, enables a breathing (pulsating) animation on the shadow's blur radius.
 * @param breathingEffectIntensity The maximum additional blur radius applied during the breathing animation.
 * @param breathingDurationMillis The duration for one full cycle of the breathing animation.
 * @return A [Modifier] that applies the drop shadow effect.
 */
fun Modifier.shadowGlow(
    color: Color = Color.Black.copy(alpha = 0.4f),
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 8.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    spread: Dp = 0.dp,
    blurStyle: ShadowBlurStyle = ShadowBlurStyle.NORMAL,
    enableGyroParallax: Boolean = false,
    parallaxSensitivity: Dp = 4.dp,
    enableBreathingEffect: Boolean = false,
    breathingEffectIntensity: Dp = 4.dp,
    breathingDurationMillis: Int = 1500,
    enableGlowTrail: Boolean = false,
    glowTrailWidth: Dp = 8.dp,
    glowTrailBlurRadius: Dp = 16.dp,
    glowTrailLengthDegrees: Float = 60f,
    glowTrailDurationMillis: Int = 2500,
    glowTrailClockwise: Boolean = true,
    glowTrailAlpha: Float = 1f
): Modifier = composed {

    val glowTrailProgress = rememberGlowTrailProgess(
        enableGlowTrail,
        glowTrailClockwise,
        glowTrailDurationMillis
    )

    val parallaxState = if (enableGyroParallax) rememberGyroParallaxState(parallaxSensitivity) else null

    val animatedBreathingValuePx = rememberAnimatedBreathingValue(
        enabled = enableBreathingEffect,
        intensity = breathingEffectIntensity,
        durationMillis = breathingDurationMillis
    )

    this.then(
        Modifier.drawBehind {
            val spreadPx = spread.toPx()
            val totalBlurPx = blurRadius.toPx() + animatedBreathingValuePx.value
            val currentAnimatedBlurPx = animatedBreathingValuePx.value
            val totalBlurRadiusPx = (totalBlurPx + currentAnimatedBlurPx).coerceAtLeast(0f)

            val baseOffsetXPx = offsetX.toPx()
            val baseOffsetYPx = offsetY.toPx()
            val shadowBorderRadiusPx = borderRadius.toPx()

            val dynamicOffsetXPx = parallaxState?.value?.first ?: 0f
            val dynamicOffsetYPx = parallaxState?.value?.second ?: 0f

            val totalOffsetXPx = baseOffsetXPx + dynamicOffsetXPx
            val totalOffsetYPx = baseOffsetYPx + dynamicOffsetYPx

            val shadowColorArgb = color.toArgb()

            val left = -spreadPx + totalOffsetXPx
            val top = -spreadPx + totalOffsetYPx
            val right = size.width + spreadPx + totalOffsetXPx
            val bottom = size.height + spreadPx + totalOffsetYPx

            if (color.alpha == 0f && totalBlurRadiusPx <= 0f && spreadPx == 0f && dynamicOffsetXPx == 0f && dynamicOffsetYPx == 0f && baseOffsetXPx == 0f && baseOffsetYPx == 0f) {
                return@drawBehind
            }

            val frameworkPaint = AndroidPaint().apply {
                isAntiAlias = true
                style = AndroidPaint.Style.FILL
                this.color = shadowColorArgb
                if (totalBlurRadiusPx > 0f) {
                    maskFilter = BlurMaskFilter(totalBlurRadiusPx, blurStyle.toAndroidBlurStyle())
                }
            }

            drawShadowShape(left, top, right, bottom, shadowBorderRadiusPx, frameworkPaint)

            if (enableGlowTrail) {
                drawGlowTrailAlongShape(
                    shape = RoundedCornerShape(borderRadius),
                    progress = glowTrailProgress.value,
                    trailFraction = glowTrailLengthDegrees / 360f,
                    color = color,
                    strokeWidthPx = glowTrailWidth.toPx(),
                    blurRadiusPx = glowTrailBlurRadius.toPx(),
                    alpha = glowTrailAlpha
                )
            }
        }
    )
}

/**
 * Applies a drop shadow effect to the composable using a linear gradient.
 * (Gyro parallax and breathing effect parameters documented as above)
 */
fun Modifier.shadowGlow(
    gradientColors: List<Color>,
    gradientStartFactorX: Float = 0f,
    gradientStartFactorY: Float = 0f,
    gradientEndFactorX: Float = 1f,
    gradientEndFactorY: Float = 1f,
    gradientColorStops: List<Float>? = null,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 8.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    spread: Dp = 0.dp,
    alpha: Float = 1.0f,
    blurStyle: ShadowBlurStyle = ShadowBlurStyle.NORMAL,
    enableGyroParallax: Boolean = false,
    parallaxSensitivity: Dp = 4.dp,
    enableBreathingEffect: Boolean = false,
    breathingEffectIntensity: Dp = 4.dp,
    breathingDurationMillis: Int = 1500,
    enableGlowTrail: Boolean = false,
    glowTrailWidth: Dp = 8.dp,
    glowTrailBlurRadius: Dp = 16.dp,
    glowTrailLengthDegrees: Float = 60f,
    glowTrailDurationMillis: Int = 2500,
    glowTrailClockwise: Boolean = true,
    glowTrailAlpha: Float = 1f
): Modifier = composed {
    val parallaxState =
        if (enableGyroParallax) rememberGyroParallaxState(parallaxSensitivity) else null
    val animatedBreathingValuePx = rememberAnimatedBreathingValue(
        enabled = enableBreathingEffect,
        intensity = breathingEffectIntensity,
        durationMillis = breathingDurationMillis
    )

    val glowTrailProgress = rememberGlowTrailProgess(
        enableGlowTrail,
        glowTrailClockwise,
        glowTrailDurationMillis
    )

    this.then(
        Modifier.drawBehind {
            if (gradientColors.isEmpty() || alpha == 0f) {
                return@drawBehind
            }
            val spreadPx = spread.toPx()
            val baseBlurRadiusPx = blurRadius.toPx()
            val currentAnimatedBlurPx = animatedBreathingValuePx.value
            val totalBlurRadiusPx = (baseBlurRadiusPx + currentAnimatedBlurPx).coerceAtLeast(0f)

            val baseOffsetXPx = offsetX.toPx()
            val baseOffsetYPx = offsetY.toPx()
            val shadowBorderRadiusPx = borderRadius.toPx()

            val dynamicOffsetXPx = parallaxState?.value?.first ?: 0f
            val dynamicOffsetYPx = parallaxState?.value?.second ?: 0f

            val totalOffsetXPx = baseOffsetXPx + dynamicOffsetXPx
            val totalOffsetYPx = baseOffsetYPx + dynamicOffsetYPx

            val actualStartX = gradientStartFactorX * size.width
            val actualStartY = gradientStartFactorY * size.height
            val actualEndX = gradientEndFactorX * size.width
            val actualEndY = gradientEndFactorY * size.height

            val left = -spreadPx + totalOffsetXPx
            val top = -spreadPx + totalOffsetYPx
            val right = size.width + spreadPx + totalOffsetXPx
            val bottom = size.height + spreadPx + totalOffsetYPx

            val frameworkPaint = AndroidPaint().apply {
                isAntiAlias = true
                style = AndroidPaint.Style.FILL
                this.alpha = (alpha.coerceIn(0f, 1f) * 255).toInt()
                shader = AndroidLinearGradient(
                    actualStartX, actualStartY, actualEndX, actualEndY,
                    gradientColors.map { it.toArgb() }.toIntArray(),
                    gradientColorStops?.toFloatArray(),
                    TileMode.Clamp.toAndroidTileMode()
                )
                if (totalBlurRadiusPx > 0f) {
                    maskFilter = BlurMaskFilter(totalBlurRadiusPx, blurStyle.toAndroidBlurStyle())
                }
            }

            drawShadowShape(left, top, right, bottom, shadowBorderRadiusPx, frameworkPaint)

            if (enableGlowTrail) {
                drawGlowTrailAlongShape(
                    shape = RoundedCornerShape(borderRadius),
                    progress = glowTrailProgress.value,
                    trailFraction = glowTrailLengthDegrees / 360f,
                    color = gradientColors.first(),
                    strokeWidthPx = glowTrailWidth.toPx(),
                    blurRadiusPx = glowTrailBlurRadius.toPx(),
                    alpha = glowTrailAlpha
                )
            }
        }
    )
}
