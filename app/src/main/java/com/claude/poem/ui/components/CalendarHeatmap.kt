package com.claude.poem.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarHeatmap(
    year: Int,
    month: Int,
    viewedDates: Set<String> = emptySet(),
    modifier: Modifier = Modifier,
) {
    val primary = MaterialTheme.colorScheme.primary
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    val density = LocalDensity.current
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val cal = Calendar.getInstance()
    cal.set(year, month, 1)
    val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1
    val weekDayNames = listOf("日", "一", "二", "三", "四", "五", "六")
    val monthNames = listOf(
        "一月", "二月", "三月", "四月", "五月", "六月",
        "七月", "八月", "九月", "十月", "十一月", "十二月"
    )

    val keyFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US) }
    val monthKeyFormat = remember { SimpleDateFormat("yyyy-MM", Locale.US) }
    val monthKey = monthKeyFormat.format(cal.time)

    val dayIntensity = remember(viewedDates, monthKey) {
        val map = HashMap<Int, Float>(maxDay)
        for (day in 1..maxDay) {
            val probe = Calendar.getInstance()
            probe.set(year, month, day)
            val key = keyFormat.format(probe.time)
            map[day] = if (viewedDates.contains(key)) 0.6f else 0.05f
        }
        map
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val availableWidth = maxWidth
        val cellGap = 4.dp
        val headerHeight = 18.dp
        val rows = 6

        val cellSize = (availableWidth - cellGap * 6) / 7
        val canvasHeight = cellSize * rows + cellGap * (rows - 1) + headerHeight

        Column {
            Text(
                text = "月度活跃 · ${monthNames[month]} $year",
                style = MaterialTheme.typography.titleMedium,
                color = onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                weekDayNames.forEach { name ->
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelSmall,
                        color = onSurfaceVariant,
                        modifier = Modifier.size(cellSize),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(canvasHeight)
            ) {
                val cellPx = cellSize.toPx()
                val gapPx = cellGap.toPx()
                val headerPx = headerHeight.toPx()

                for (row in 0 until rows) {
                    for (col in 0 until 7) {
                        val isFirstWeek = row == 0
                        val effectiveDay = if (isFirstWeek) {
                            val d = col - firstDayOfWeek + 1
                            if (d in 1..maxDay) d else 0
                        } else {
                            val d = (row * 7 + col) - firstDayOfWeek + 1
                            if (d in 1..maxDay) d else 0
                        }

                        if (effectiveDay > 0 && effectiveDay <= maxDay) {
                            val x = cellPx * col + gapPx * col
                            val y = headerPx + cellPx * row + gapPx * row

                            val intensity = dayIntensity[effectiveDay] ?: 0.05f
                            val cellColor = primary.copy(alpha = intensity.coerceIn(0.05f, 0.6f))

                            drawRoundRect(
                                color = cellColor,
                                topLeft = Offset(x, y),
                                size = Size(cellPx, cellPx),
                                cornerRadius = CornerRadius(4.dp.toPx()),
                            )

                            val textPaint = android.graphics.Paint().apply {
                                color = if (intensity > 0.3f) {
                                    if (isDark) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                                } else {
                                    onSurfaceVariant.toArgb()
                                }
                                textSize = with(density) { 11.sp.toPx() }
                                textAlign = android.graphics.Paint.Align.CENTER
                                isAntiAlias = true
                            }

                            drawContext.canvas.nativeCanvas.drawText(
                                effectiveDay.toString(),
                                x + cellPx / 2,
                                y + cellPx / 2 + textPaint.textSize / 3f,
                                textPaint
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "少",
                    style = MaterialTheme.typography.labelSmall,
                    color = onSurfaceVariant,
                )
                Spacer(Modifier.width(4.dp))
                listOf(0.05f, 0.2f, 0.4f, 0.6f).forEach { alpha ->
                    Canvas(modifier = Modifier.size(12.dp)) {
                        drawRoundRect(
                            color = primary.copy(alpha = alpha),
                            cornerRadius = CornerRadius(2.dp.toPx()),
                        )
                    }
                    Spacer(Modifier.width(2.dp))
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "多",
                    style = MaterialTheme.typography.labelSmall,
                    color = onSurfaceVariant,
                )
            }
        }
    }
}

private fun Color.luminance(): Float {
    return (0.299f * red + 0.587f * green + 0.114f * blue)
}
