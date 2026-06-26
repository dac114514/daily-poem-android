package com.claude.poem.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.claude.poem.data.model.Poem
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun RingCarousel(
    poems: List<Poem>,
    currentIndex: Int,
    onIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    stepAngle: Float = 50f,
    sensitivity: Float = 0.25f,
) {
    if (poems.isEmpty()) return
    val n = poems.size
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val visualOffset = remember { Animatable(0f) }

    val pxPerCard = with(density) {
        (stepAngle / sensitivity).dp.toPx()
    }

    LaunchedEffect(currentIndex) {
        if (visualOffset.value != 0f && !visualOffset.isRunning) {
            visualOffset.snapTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(480.dp)
            .pointerInput(n, pxPerCard) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        scope.launch { visualOffset.stop() }
                    },
                    onDragEnd = {
                        scope.launch {
                            val target = visualOffset.value.toInt().toFloat()
                            visualOffset.animateTo(
                                targetValue = target,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMediumLow,
                                ),
                            )
                            val newIndex = ((currentIndex + target.toInt()) % n + n) % n
                            if (newIndex != currentIndex) {
                                onIndexChange(newIndex)
                            }
                        }
                    },
                ) { change, dragAmount ->
                    change.consume()
                    val cardsDelta = -dragAmount / pxPerCard
                    scope.launch {
                        visualOffset.snapTo(visualOffset.value + cardsDelta)
                    }
                }
            },
    ) {
        val halfN = n / 2f
        for (offset in -1..1) {
            val poemIndex = ((currentIndex + offset) % n + n) % n
            val poem = poems[poemIndex]
            val rawOffset = offset - visualOffset.value
            val wrapped = ((rawOffset % n) + n + halfN) % n - halfN
            val effectiveAngle = wrapped * stepAngle
            val absAngle = abs(effectiveAngle)
            val opacity = when {
                absAngle > 100f -> 0f
                absAngle > stepAngle + 10f -> 0.5f
                absAngle > stepAngle - 5f -> 0.85f
                else -> 1f
            }
            val depth = 1f - absAngle / 200f * 0.15f

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 360.dp)
                    .graphicsLayer {
                        rotationY = effectiveAngle
                        cameraDistance = 8f * density.density
                        this.alpha = opacity
                        scaleX = depth
                        scaleY = depth
                    },
            ) {
                PoemCard(poem = poem)
            }
        }
    }
}
