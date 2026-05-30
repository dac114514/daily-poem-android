package com.claude.poem.ui.components

import com.claude.poem.ui.statistics.WeeklyData

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.toArgb

@Composable
fun WeekChart(
    data: WeeklyData,
    modifier: Modifier = Modifier,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "本周活跃",
            style = MaterialTheme.typography.titleMedium,
            color = onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        val density = LocalDensity.current

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(start = 8.dp, end = 8.dp)
        ) {
            if (data.values.isEmpty()) return@Canvas

            val maxVal = data.values.max().coerceAtLeast(1).toFloat()
            val padding = 8.dp.toPx()
            val bottomPadding = 24.dp.toPx()
            val topPadding = 8.dp.toPx()
            val chartWidth = size.width - padding * 2
            val chartHeight = size.height - bottomPadding - topPadding

            val points = data.values.mapIndexed { index, value ->
                val x = padding + chartWidth * index / (data.values.size - 1).coerceAtLeast(1)
                val y = topPadding + chartHeight * (1 - value / maxVal)
                Offset(x, y)
            }

            // Fill area under line
            if (points.size >= 2) {
                val fillPath = Path().apply {
                    moveTo(points.first().x, size.height - bottomPadding)
                    points.forEach { lineTo(it.x, it.y) }
                    lineTo(points.last().x, size.height - bottomPadding)
                    close()
                }
                drawPath(
                    path = fillPath,
                    color = primaryColor.copy(alpha = 0.1f),
                )
            }

            // Line
            val linePath = Path().apply {
                points.forEachIndexed { i, point ->
                    if (i == 0) moveTo(point.x, point.y)
                    else lineTo(point.x, point.y)
                }
            }
            drawPath(
                path = linePath,
                color = primaryColor,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
            )

            // Dots
            points.forEach { point ->
                drawCircle(
                    color = primaryColor,
                    radius = 4.dp.toPx(),
                    center = point,
                )
                drawCircle(
                    color = Color.White,
                    radius = 2.dp.toPx(),
                    center = point,
                )
            }

            // X-axis labels
            val textPaint = android.graphics.Paint().apply {
                color = onSurfaceVariant.toArgb()
                textSize = with(density) { 10.sp.toPx() }
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
            }

            data.labels.forEachIndexed { index, label ->
                val x = padding + chartWidth * index / (data.values.size - 1).coerceAtLeast(1)
                val y = size.height - 4.dp.toPx()
                drawContext.canvas.nativeCanvas.drawText(
                    label,
                    x,
                    y,
                    textPaint
                )
            }

            // Y-axis value labels
            val valuePaint = android.graphics.Paint().apply {
                color = onSurfaceVariant.toArgb()
                textSize = with(density) { 10.sp.toPx() }
                textAlign = android.graphics.Paint.Align.RIGHT
                isAntiAlias = true
            }
            drawContext.canvas.nativeCanvas.drawText(
                maxVal.toInt().toString(),
                padding - 4.dp.toPx(),
                topPadding + 10.dp.toPx(),
                valuePaint
            )
        }
    }
}
