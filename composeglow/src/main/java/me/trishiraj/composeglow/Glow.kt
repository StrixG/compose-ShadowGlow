package me.trishiraj.composeglow

import android.graphics.BlurMaskFilter
import android.graphics.Paint as AndroidPaint
import android.graphics.Shader as AndroidShader
import android.graphics.LinearGradient as AndroidLinearGradient
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toAndroidTileMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dropShadow(
    color: Color = Color.Black.copy(alpha = 0.4f),
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 8.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    spread: Dp = 0.dp
): Modifier = this.then(
    Modifier.drawBehind {
        val spreadPx = spread.toPx()
        val blurRadiusPx = blurRadius.toPx()
        val offsetXPx = offsetX.toPx()
        val offsetYPx = offsetY.toPx()

        val shadowColorArgb = color.toArgb()

        drawIntoCanvas { canvas ->
            val frameworkPaint = AndroidPaint().apply {
                isAntiAlias = true
                this.color = android.graphics.Color.TRANSPARENT
                style = AndroidPaint.Style.FILL
                if (blurRadiusPx > 0f && Color(shadowColorArgb).alpha > 0f) {
                    setShadowLayer(blurRadiusPx, offsetXPx, offsetYPx, shadowColorArgb)
                }
            }

            val left = -spreadPx
            val top = -spreadPx
            val right = size.width + spreadPx
            val bottom = size.height + spreadPx
            val shadowBorderRadius = borderRadius.toPx()

            if (blurRadiusPx > 0f || spreadPx > 0f || offsetXPx != 0f || offsetYPx != 0f) {
                canvas.nativeCanvas.drawRoundRect(
                    left,
                    top,
                    right,
                    bottom,
                    shadowBorderRadius,
                    shadowBorderRadius,
                    frameworkPaint
                )
            }
        }
    }
)


fun Modifier.dropShadow(
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
    alpha: Float = 1.0f
): Modifier = this.then(
    Modifier.drawBehind {
        if (gradientColors.isEmpty()) {
            return@drawBehind
        }
        val spreadPx = spread.toPx()
        val blurRadiusPx = blurRadius.toPx()
        val offsetXPx = offsetX.toPx()
        val offsetYPx = offsetY.toPx()

        val actualStartX = gradientStartFactorX * size.width
        val actualStartY = gradientStartFactorY * size.height
        val actualEndX = gradientEndFactorX * size.width
        val actualEndY = gradientEndFactorY * size.height

        val frameworkPaint = AndroidPaint().apply {
            isAntiAlias = true
            style = AndroidPaint.Style.FILL
            this.alpha = (alpha.coerceIn(0f, 1f) * 255).toInt()

            val shader: AndroidShader = AndroidLinearGradient(
                actualStartX,
                actualStartY,
                actualEndX,
                actualEndY,
                gradientColors.map { it.toArgb() }.toIntArray(),
                gradientColorStops?.toFloatArray(),
                TileMode.Clamp.toAndroidTileMode()
            )
            this.shader = shader

            if (blurRadiusPx > 0f) {
                maskFilter = BlurMaskFilter(blurRadiusPx, BlurMaskFilter.Blur.NORMAL)
            }
        }

        val left = -spreadPx + offsetXPx
        val top = -spreadPx + offsetYPx
        val right = size.width + spreadPx + offsetXPx
        val bottom = size.height + spreadPx + offsetYPx
        val shadowBorderRadius = borderRadius.toPx()

        if (blurRadiusPx > 0f || spreadPx > 0f || offsetXPx != 0f || offsetYPx != 0f) {
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawRoundRect(
                    left,
                    top,
                    right,
                    bottom,
                    shadowBorderRadius,
                    shadowBorderRadius,
                    frameworkPaint
                )
            }
        }
    }
)
