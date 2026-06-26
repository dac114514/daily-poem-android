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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.claude.poem.data.model.Poem
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

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

    val pxPerCard = with(density) { (stepAngle / sensitivity).dp.toPx() }
    val ringRPx = with(density) { 360.dp.toPx() }
    val cameraDistPx = with(density) { 2000.dp.toPx() }

    var lastDragTimeMs by remember { mutableStateOf(0L) }
    var releaseVelocity by remember { mutableStateOf(0f) }

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
                        lastDragTimeMs = System.currentTimeMillis()
                        releaseVelocity = 0f
                    },
                    onDragEnd = {
                        scope.launch {
                            val flingCards = if (abs(releaseVelocity) < FLING_VELOCITY_THRESHOLD) {
                                0f
                            } else {
                                -releaseVelocity * FLING_PROJECTION_MS / 1000f / pxPerCard
                            }
                            val target = (visualOffset.value + flingCards).roundToInt().toFloat()
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
                    val now = System.currentTimeMillis()
                    val dt = (now - lastDragTimeMs).toFloat().coerceAtLeast(1f)
                    if (lastDragTimeMs > 0L) {
                        releaseVelocity = dragAmount / dt * 1000f
                    }
                    lastDragTimeMs = now

                    val cardsDelta = -dragAmount / pxPerCard
                    scope.launch {
                        visualOffset.snapTo(visualOffset.value + cardsDelta)
                    }
                }
            },
        contentAlignment = Alignment.Center,
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
            val angleRad = effectiveAngle * (PI.toFloat() / 180f)
            val zDist = ringRPx * cos(angleRad)
            val perspectiveScale = cameraDistPx / (cameraDistPx - zDist)

            Box(
                modifier = Modifier
                    .zIndex(if (absAngle < stepAngle / 2) 2f else 1f)
                    .widthIn(max = 360.dp)
                    .fillMaxWidth()
                    .height(480.dp)
                    .graphicsLayer {
                        transformOrigin = TransformOrigin.Center
                        rotationY = effectiveAngle
                        translationX = ringRPx * sin(angleRad)
                        scaleX = perspectiveScale
                        scaleY = perspectiveScale
                        cameraDistance = cameraDistPx
                        this.alpha = opacity
                    },
            ) {
                PoemCard(poem = poem)
            }
        }
    }
}

private const val FLING_PROJECTION_MS = 200f
private const val FLING_VELOCITY_THRESHOLD = 500f
