package me.trishiraj.composeglow

import android.graphics.BlurMaskFilter
import android.graphics.Paint as AndroidPaint
import android.graphics.Shader as AndroidShader
import android.graphics.LinearGradient as AndroidLinearGradient
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines the style of the blur effect for the shadow, corresponding to `BlurMaskFilter.Blur`.
 */
enum class ShadowBlurStyle {
    /** Normal blur, feathering the edges of the shadow. */
    NORMAL,
    /** Solidify the shadow silhouette, with no feathering. */
    SOLID,
    /** Blur only the outer edge of the shadow. */
    OUTER,
    /** Blur only the inner edge of the shadow. */
    INNER
}

/**
 * Converts [ShadowBlurStyle] to the Android-specific `BlurMaskFilter.Blur`.
 */
internal fun ShadowBlurStyle.toAndroidBlurStyle(): BlurMaskFilter.Blur {
    return when (this) {
        ShadowBlurStyle.NORMAL -> BlurMaskFilter.Blur.NORMAL
        ShadowBlurStyle.SOLID -> BlurMaskFilter.Blur.SOLID
        ShadowBlurStyle.OUTER -> BlurMaskFilter.Blur.OUTER
        ShadowBlurStyle.INNER -> BlurMaskFilter.Blur.INNER
    }
}

/**
 * Applies a drop shadow effect to the composable using a solid color.
 *
 * @param color The color of the shadow.
 * @param borderRadius The radius of the shadow's corners.
 * @param blurRadius The blur radius of the shadow. A larger value creates a softer, more dispersed shadow.
 * @param offsetX The horizontal offset of the shadow. Positive values shift the shadow to the right.
 * @param offsetY The vertical offset of the shadow. Positive values shift the shadow downwards.
 * @param spread The amount to expand the shadow's bounds before blurring. Positive values make the shadow larger.
 * @param blurStyle The style of the blur effect (Normal, Solid, Outer, Inner).
 * @return A [Modifier] that applies the drop shadow effect.
 */
fun Modifier.dropShadow(
    color: Color = Color.Black.copy(alpha = 0.4f),
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 8.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    spread: Dp = 0.dp,
    blurStyle: ShadowBlurStyle = ShadowBlurStyle.NORMAL
): Modifier = this.then(
    Modifier.drawBehind {
        val spreadPx = spread.toPx()
        val blurRadiusPx = blurRadius.toPx()
        val offsetXPx = offsetX.toPx()
        val offsetYPx = offsetY.toPx()
        val shadowBorderRadiusPx = borderRadius.toPx()

        val shadowColorArgb = color.toArgb()

        // Early exit if the shadow would be completely invisible
        if (color.alpha == 0f && blurRadiusPx == 0f && spreadPx == 0f && offsetXPx == 0f && offsetYPx == 0f) {
            return@drawBehind
        }

        val frameworkPaint = AndroidPaint().apply {
            isAntiAlias = true
            style = AndroidPaint.Style.FILL
            this.color = shadowColorArgb

            if (blurRadiusPx > 0f) {
                maskFilter = BlurMaskFilter(blurRadiusPx, blurStyle.toAndroidBlurStyle())
            }
        }

        val left = -spreadPx + offsetXPx
        val top = -spreadPx + offsetYPx
        val right = size.width + spreadPx + offsetXPx
        val bottom = size.height + spreadPx + offsetYPx

        drawShadowShape(left, top, right, bottom, shadowBorderRadiusPx, frameworkPaint)
    }
)

/**
 * Applies a drop shadow effect to the composable using a linear gradient.
 *
 * @param gradientColors The list of colors for the linear gradient shadow.
 * @param gradientStartFactorX The starting X-coordinate factor (0.0 to 1.0) for the gradient, relative to the composable's width.
 * @param gradientStartFactorY The starting Y-coordinate factor (0.0 to 1.0) for the gradient, relative to the composable's height.
 * @param gradientEndFactorX The ending X-coordinate factor (0.0 to 1.0) for the gradient, relative to the composable's width.
 * @param gradientEndFactorY The ending Y-coordinate factor (0.0 to 1.0) for the gradient, relative to the composable's height.
 * @param gradientColorStops Optional list of color stops for the gradient (0.0 to 1.0).
 * @param gradientTileMode The tile mode for the gradient shader.
 * @param borderRadius The radius of the shadow's corners.
 * @param blurRadius The blur radius of the shadow.
 * @param offsetX The horizontal offset of the shadow.
 * @param offsetY The vertical offset of the shadow.
 * @param spread The amount to expand the shadow's bounds before blurring.
 * @param alpha The overall alpha transparency for the gradient shadow (0.0 to 1.0).
 * @param blurStyle The style of the blur effect (Normal, Solid, Outer, Inner).
 * @return A [Modifier] that applies the gradient drop shadow effect.
 */
fun Modifier.dropShadow(
    gradientColors: List<Color>,
    gradientStartFactorX: Float = 0f,
    gradientStartFactorY: Float = 0f,
    gradientEndFactorX: Float = 1f,
    gradientEndFactorY: Float = 1f,
    gradientColorStops: List<Float>? = null,
    gradientTileMode: TileMode = TileMode.Clamp,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 8.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    spread: Dp = 0.dp,
    alpha: Float = 1.0f,
    blurStyle: ShadowBlurStyle = ShadowBlurStyle.NORMAL
): Modifier = this.then(
    Modifier.drawBehind {
        // Early exit if no gradient colors are provided or if shadow is fully transparent
        if (gradientColors.isEmpty() || alpha == 0f) {
            return@drawBehind
        }

        val spreadPx = spread.toPx()
        val blurRadiusPx = blurRadius.toPx()
        val offsetXPx = offsetX.toPx()
        val offsetYPx = offsetY.toPx()
        val shadowBorderRadiusPx = borderRadius.toPx()

        val actualStartX = gradientStartFactorX * size.width
        val actualStartY = gradientStartFactorY * size.height
        val actualEndX = gradientEndFactorX * size.width
        val actualEndY = gradientEndFactorY * size.height

        val frameworkPaint = AndroidPaint().apply {
            isAntiAlias = true
            style = AndroidPaint.Style.FILL
            this.alpha = (alpha.coerceIn(0f, 1f) * 255).toInt()

            shader = AndroidLinearGradient(
                actualStartX,
                actualStartY,
                actualEndX,
                actualEndY,
                gradientColors.map { it.toArgb() }.toIntArray(),
                gradientColorStops?.toFloatArray(),
                gradientTileMode.toAndroidTileMode()
            )

            if (blurRadiusPx > 0f) {
                maskFilter = BlurMaskFilter(blurRadiusPx, blurStyle.toAndroidBlurStyle())
            }
        }

        val left = -spreadPx + offsetXPx
        val top = -spreadPx + offsetYPx
        val right = size.width + spreadPx + offsetXPx
        val bottom = size.height + spreadPx + offsetYPx

        drawShadowShape(left, top, right, bottom, shadowBorderRadiusPx, frameworkPaint)
    }
)

/**
 * Private helper to perform the actual drawing of the shadow shape (rounded rectangle).
 */
private fun DrawScope.drawShadowShape(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    cornerRadiusPx: Float,
    paint: AndroidPaint
) {
    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawRoundRect(left, top, right, bottom, cornerRadiusPx, cornerRadiusPx, paint)
    }
}

/**
 * Converts Compose [TileMode] to Android-specific `Shader.TileMode`.
 */
private fun TileMode.toAndroidTileMode(): AndroidShader.TileMode {
    return when (this) {
        TileMode.Clamp -> AndroidShader.TileMode.CLAMP
        TileMode.Repeated -> AndroidShader.TileMode.REPEAT
        TileMode.Mirror -> AndroidShader.TileMode.MIRROR
        else -> AndroidShader.TileMode.CLAMP // Default for TileMode.Decal or others
    }
}
