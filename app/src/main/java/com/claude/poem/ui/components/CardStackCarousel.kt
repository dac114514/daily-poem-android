package com.claude.poem.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.claude.poem.data.model.Poem
import kotlinx.coroutines.launch
import kotlin.math.abs

private enum class CardRole { LEFT, CENTER, RIGHT }

private const val SWIPE_THRESHOLD = 0.4f
private const val FLING_VELOCITY = 0.8f
private const val SWIPE_OUT_DURATION_MS = 220
private const val SWIPE_IN_DURATION_MS = 280
private const val SNAP_BACK_DURATION_MS = 240
private const val MAX_DRAG_OFFSET = 1.5f
private const val ANIMATION_TARGET = 1.4f
private const val DRAG_WIDTH_DP = 280
private const val REST_SIDE_SCALE = 0.94f
private const val REST_SIDE_ALPHA = 0.85f
private const val REST_CENTER_SCALE = 1.0f
private const val REST_CENTER_ALPHA = 1.0f
private const val FADE_OUT_COMPLETE_AT = 1.0f

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

    val cardWidthDp = (DRAG_WIDTH_DP).dp
    val cardHeightDp = 440.dp

    val dragWidthPx = with(density) { (DRAG_WIDTH_DP).dp.toPx() }

    val visualPos = remember { Animatable(0f) }

    var lastDragTime by remember { mutableStateOf(0L) }
    var releaseVelocity by remember { mutableStateOf(0f) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(460.dp)
            .clipToBounds()
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
                                val target = if (isNext) -ANIMATION_TARGET else ANIMATION_TARGET

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

        key(rightPoem.id) {
            CardSlot(
                role = CardRole.RIGHT,
                poem = rightPoem,
                visualPos = p,
                dragWidthPx = dragWidthPx,
                cardWidthDp = cardWidthDp,
                cardHeightDp = cardHeightDp,
                restScale = REST_SIDE_SCALE,
                restAlpha = REST_SIDE_ALPHA,
                fadeOutCompleteAt = FADE_OUT_COMPLETE_AT,
                modifier = Modifier.zIndex(1f),
            )
        }
        key(leftPoem.id) {
            CardSlot(
                role = CardRole.LEFT,
                poem = leftPoem,
                visualPos = p,
                dragWidthPx = dragWidthPx,
                cardWidthDp = cardWidthDp,
                cardHeightDp = cardHeightDp,
                restScale = REST_SIDE_SCALE,
                restAlpha = REST_SIDE_ALPHA,
                fadeOutCompleteAt = FADE_OUT_COMPLETE_AT,
                modifier = Modifier.zIndex(1f),
            )
        }
        key(centerPoem.id) {
            CardSlot(
                role = CardRole.CENTER,
                poem = centerPoem,
                visualPos = p,
                dragWidthPx = dragWidthPx,
                cardWidthDp = cardWidthDp,
                cardHeightDp = cardHeightDp,
                restScale = REST_CENTER_SCALE,
                restAlpha = REST_CENTER_ALPHA,
                fadeOutCompleteAt = FADE_OUT_COMPLETE_AT,
                modifier = Modifier.zIndex(2f),
            )
        }
    }
}

@Composable
private fun CardSlot(
    role: CardRole,
    poem: Poem,
    visualPos: Float,
    dragWidthPx: Float,
    cardWidthDp: Dp,
    cardHeightDp: Dp,
    restScale: Float,
    restAlpha: Float,
    fadeOutCompleteAt: Float,
    modifier: Modifier = Modifier,
) {
    val restX = when (role) {
        CardRole.LEFT -> -dragWidthPx
        CardRole.CENTER -> 0f
        CardRole.RIGHT -> dragWidthPx
    }

    val cardX = restX + visualPos * dragWidthPx

    val fadeProgress = (abs(visualPos) / fadeOutCompleteAt).coerceIn(0f, 1f)
    val alpha = when (role) {
        CardRole.CENTER -> restAlpha
        else -> (restAlpha * (1f - fadeProgress)).coerceAtLeast(0f)
    }
    val scale = restScale

    Box(
        modifier = modifier
            .width(cardWidthDp)
            .height(cardHeightDp)
            .graphicsLayer {
                translationX = cardX
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            },
    ) {
        PoemCard(poem = poem)
    }
}
