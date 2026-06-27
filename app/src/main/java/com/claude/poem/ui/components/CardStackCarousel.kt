package com.claude.poem.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.claude.poem.data.model.Poem
import kotlinx.coroutines.launch
import kotlin.math.abs

private enum class CardRole { LEFT, CENTER, RIGHT }

private const val SWIPE_THRESHOLD = 0.4f
private const val FLING_VELOCITY = 0.8f
private const val SWIPE_OUT_DURATION_MS = 200
private const val SWIPE_IN_DURATION_MS = 240
private const val SNAP_BACK_DURATION_MS = 240
private const val MAX_DRAG_OFFSET = 1.3f

@Composable
fun CardStackCarousel(
    leftPoem: Poem?,
    centerPoem: Poem?,
    rightPoem: Poem?,
    onSwipeNext: () -> Unit,
    onSwipePrev: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (leftPoem == null || centerPoem == null || rightPoem == null) return

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val cardWidthDp = 280.dp
    val cardHeightDp = 440.dp
    val cardOffsetDp = 64.dp
    val dragWidthDp = 110.dp
    val depthDp = 28.dp
    val restSideScale = 0.88f
    val restSideAlpha = 0.55f
    val parallaxRate = 0.7f

    val cardOffsetPx = with(density) { cardOffsetDp.toPx() }
    val dragWidthPx = with(density) { dragWidthDp.toPx() }
    val depthPx = with(density) { depthDp.toPx() }

    val visualPos = remember { Animatable(0f) }

    var lastDragTime by remember { mutableStateOf(0L) }
    var releaseVelocity by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(460.dp)
            .pointerInput(centerPoem.id, leftPoem.id, rightPoem.id) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        scope.launch { visualPos.stop() }
                        lastDragTime = System.currentTimeMillis()
                        releaseVelocity = 0f
                    },
                    onDragEnd = {
                        scope.launch {
                            val p = visualPos.value
                            val v = releaseVelocity
                            val shouldSwipe = abs(p) > SWIPE_THRESHOLD || abs(v) > FLING_VELOCITY

                            if (shouldSwipe) {
                                val isNext = p < 0f
                                val target = if (isNext) -1f else 1f

                                visualPos.animateTo(
                                    targetValue = target,
                                    animationSpec = tween(
                                        durationMillis = SWIPE_OUT_DURATION_MS,
                                        easing = FastOutSlowInEasing,
                                    ),
                                )
                                if (isNext) onSwipeNext() else onSwipePrev()
                                visualPos.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(
                                        durationMillis = SWIPE_IN_DURATION_MS,
                                        easing = FastOutSlowInEasing,
                                    ),
                                )
                            } else {
                                visualPos.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(
                                        durationMillis = SNAP_BACK_DURATION_MS,
                                        easing = FastOutSlowInEasing,
                                    ),
                                )
                            }
                        }
                    },
                    onDragCancel = {
                        scope.launch {
                            visualPos.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(
                                    durationMillis = SNAP_BACK_DURATION_MS,
                                    easing = FastOutSlowInEasing,
                                ),
                            )
                        }
                    },
                ) { change, dragAmount ->
                    change.consume()
                    val now = System.currentTimeMillis()
                    val dt = (now - lastDragTime).toFloat().coerceAtLeast(1f)
                    if (lastDragTime > 0L) {
                        releaseVelocity = dragAmount / dt * 1000f / dragWidthPx
                    }
                    lastDragTime = now

                    val delta = dragAmount / dragWidthPx
                    scope.launch {
                        visualPos.snapTo(
                            (visualPos.value + delta).coerceIn(-MAX_DRAG_OFFSET, MAX_DRAG_OFFSET)
                        )
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        val p = visualPos.value

        CardSlot(
            role = CardRole.LEFT,
            poem = leftPoem,
            visualPos = p,
            cardOffsetPx = cardOffsetPx,
            dragWidthPx = dragWidthPx,
            depthPx = depthPx,
            restScale = restSideScale,
            restAlpha = restSideAlpha,
            parallaxRate = parallaxRate,
            modifier = Modifier.zIndex(1f),
        )
        CardSlot(
            role = CardRole.RIGHT,
            poem = rightPoem,
            visualPos = p,
            cardOffsetPx = cardOffsetPx,
            dragWidthPx = dragWidthPx,
            depthPx = depthPx,
            restScale = restSideScale,
            restAlpha = restSideAlpha,
            parallaxRate = parallaxRate,
            modifier = Modifier.zIndex(1f),
        )
        CardSlot(
            role = CardRole.CENTER,
            poem = centerPoem,
            visualPos = p,
            cardOffsetPx = cardOffsetPx,
            dragWidthPx = dragWidthPx,
            depthPx = depthPx,
            restScale = 1.0f,
            restAlpha = 1.0f,
            parallaxRate = 1.0f,
            modifier = Modifier.zIndex(2f),
        )
    }
}

@Composable
private fun CardSlot(
    role: CardRole,
    poem: Poem,
    visualPos: Float,
    cardOffsetPx: Float,
    dragWidthPx: Float,
    depthPx: Float,
    restScale: Float,
    restAlpha: Float,
    parallaxRate: Float,
    modifier: Modifier = Modifier,
) {
    val restX = when (role) {
        CardRole.LEFT -> -cardOffsetPx
        CardRole.CENTER -> 0f
        CardRole.RIGHT -> cardOffsetPx
    }
    val restZ = if (role == CardRole.CENTER) 0f else -depthPx

    val dragX = visualPos * dragWidthPx * parallaxRate

    val centerPushBack = if (role == CardRole.CENTER) abs(visualPos) * depthPx * 0.3f else 0f
    val sideForward = if (role != CardRole.CENTER) (1f - abs(visualPos)) * depthPx * 0.4f else 0f
    val z = restZ + sideForward - centerPushBack

    val scale = when (role) {
        CardRole.CENTER -> restScale - abs(visualPos) * 0.05f
        else -> restScale + abs(visualPos) * 0.05f
    }

    val alpha = when (role) {
        CardRole.CENTER -> (restAlpha - abs(visualPos) * 0.3f).coerceAtLeast(0f)
        else -> (restAlpha + abs(visualPos) * 0.2f).coerceAtMost(1f)
    }

    Box(
        modifier = modifier
            .width(280.dp)
            .height(440.dp)
            .graphicsLayer {
                translationX = restX + dragX
                translationZ = z
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            },
    ) {
        PoemCard(poem = poem)
    }
}
