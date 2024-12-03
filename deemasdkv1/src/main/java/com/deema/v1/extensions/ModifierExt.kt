package com.deema.v1.extensions

import android.view.ViewConfiguration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastSumBy
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

fun Modifier.drawHorizontalScrollbar(
    state: ScrollState,
    reverseScrolling: Boolean = false
): Modifier = drawScrollbar(state, Orientation.Horizontal, reverseScrolling)

fun Modifier.drawVerticalScrollbar(
    state: ScrollState,
    reverseScrolling: Boolean = false
): Modifier = drawScrollbar(state, Orientation.Vertical, reverseScrolling)

private fun Modifier.drawScrollbar(
    state: ScrollState,
    orientation: Orientation,
    reverseScrolling: Boolean
): Modifier = drawScrollbar(
    orientation, reverseScrolling
) { reverseDirection, atEnd, color, alpha ->
    if (state.maxValue > 0) {
        val canvasSize = if (orientation == Orientation.Horizontal) size.width else size.height
        val totalSize = canvasSize + state.maxValue
        val thumbSize = canvasSize / totalSize * canvasSize
        val startOffset = state.value / totalSize * canvasSize
        drawScrollbar(
            orientation, reverseDirection, atEnd, color, alpha, thumbSize, startOffset
        )
    }
}

fun Modifier.drawHorizontalScrollbar(
    state: LazyListState,
    reverseScrolling: Boolean = false
): Modifier = drawScrollbar(state, Orientation.Horizontal, reverseScrolling)

fun Modifier.drawVerticalScrollbar(
    state: LazyListState,
    reverseScrolling: Boolean = false
): Modifier = drawScrollbar(state, Orientation.Vertical, reverseScrolling)

private fun Modifier.drawScrollbar(
    state: LazyListState,
    orientation: Orientation,
    reverseScrolling: Boolean
): Modifier = drawScrollbar(
    orientation, reverseScrolling
) { reverseDirection, atEnd, color, alpha ->
    val layoutInfo = state.layoutInfo
    val viewportSize = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
    val items = layoutInfo.visibleItemsInfo
    val itemsSize = items.fastSumBy { it.size }
    if (items.size < layoutInfo.totalItemsCount || itemsSize > viewportSize) {
        val estimatedItemSize = if (items.isEmpty()) 0f else itemsSize.toFloat() / items.size
        val totalSize = estimatedItemSize * layoutInfo.totalItemsCount
        val canvasSize = if (orientation == Orientation.Horizontal) size.width else size.height
        val thumbSize = viewportSize / totalSize * canvasSize
        val startOffset = if (items.isEmpty()) 0f else items.first().run {
            (estimatedItemSize * index - offset) / totalSize * canvasSize
        }
        drawScrollbar(
            orientation, reverseDirection, atEnd, color, alpha, thumbSize, startOffset
        )
    }
}

fun Modifier.drawVerticalScrollbar(
    state: LazyGridState,
    spanCount: Int,
    reverseScrolling: Boolean = false
): Modifier = drawScrollbar(
    Orientation.Vertical, reverseScrolling
) { reverseDirection, atEnd, color, alpha ->
    val layoutInfo = state.layoutInfo
    val viewportSize = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
    val items = layoutInfo.visibleItemsInfo
    val rowCount = (items.size + spanCount - 1) / spanCount
    var itemsSize = 0
    for (i in 0 until rowCount) {
        itemsSize += items[i * spanCount].size.height
    }
    if (items.size < layoutInfo.totalItemsCount || itemsSize > viewportSize) {
        val estimatedItemSize = if (rowCount == 0) 0f else itemsSize.toFloat() / rowCount
        val totalRow = (layoutInfo.totalItemsCount + spanCount - 1) / spanCount
        val totalSize = estimatedItemSize * totalRow
        val canvasSize = size.height
        val thumbSize = viewportSize / totalSize * canvasSize
        val startOffset = if (rowCount == 0) 0f else items.first().run {
            val rowIndex = index / spanCount
            (estimatedItemSize * rowIndex - offset.y) / totalSize * canvasSize
        }
        drawScrollbar(
            Orientation.Vertical, reverseDirection, atEnd, color, alpha, thumbSize, startOffset
        )
    }
}

private fun DrawScope.drawScrollbar(
    orientation: Orientation,
    reverseDirection: Boolean,
    atEnd: Boolean,
    color: Color,
    alpha: () -> Float,
    thumbSize: Float,
    startOffset: Float
) {
    val thicknessPx = Thickness.toPx()
    val topLeft = if (orientation == Orientation.Horizontal) {
        Offset(
            if (reverseDirection) size.width - startOffset - thumbSize else startOffset,
            if (atEnd) size.height - thicknessPx else 0f
        )
    } else {
        Offset(
            if (atEnd) size.width - thicknessPx else 0f,
            if (reverseDirection) size.height - startOffset - thumbSize else startOffset
        )
    }
    val size = if (orientation == Orientation.Horizontal) {
        Size(thumbSize, thicknessPx)
    } else {
        Size(thicknessPx, thumbSize)
    }

    drawRect(
        color = color,
        topLeft = topLeft,
        size = size,
        alpha = alpha()
    )
}

private fun Modifier.drawScrollbar(
    orientation: Orientation,
    reverseScrolling: Boolean,
    onDraw: DrawScope.(
        reverseDirection: Boolean,
        atEnd: Boolean,
        color: Color,
        alpha: () -> Float
    ) -> Unit
): Modifier = composed {
    val scrolled = remember {
        MutableSharedFlow<Unit>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }
    val nestedScrollConnection = remember(orientation, scrolled) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = if (orientation == Orientation.Horizontal) consumed.x else consumed.y
                if (delta != 0f) scrolled.tryEmit(Unit)
                return Offset.Zero
            }
        }
    }

    val alpha = remember { Animatable(0f) }
    LaunchedEffect(scrolled, alpha) {
        scrolled.collectLatest {
            alpha.snapTo(1f)
            delay(ViewConfiguration.getScrollDefaultDelay().toLong())
            alpha.animateTo(0f, animationSpec = FadeOutAnimationSpec)
        }
    }

    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    val reverseDirection = if (orientation == Orientation.Horizontal) {
        if (isLtr) reverseScrolling else !reverseScrolling
    } else reverseScrolling
    val atEnd = if (orientation == Orientation.Vertical) isLtr else true

    val color = BarColor

    Modifier
        .nestedScroll(nestedScrollConnection)
        .drawWithContent {
            drawContent()
            onDraw(reverseDirection, atEnd, color, alpha::value)
        }
}

private val BarColor: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)

private val Thickness = 4.dp
private val FadeOutAnimationSpec =
    tween<Float>(durationMillis = ViewConfiguration.getScrollBarFadeDuration())




//
//import androidx.compose.animation.core.AnimationSpec
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.ScrollState
//import androidx.compose.foundation.gestures.FlingBehavior
//import androidx.compose.foundation.gestures.Orientation
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//
//fun Modifier.scrollbar(
//    state: ScrollState,
//    direction: Orientation,
//    indicatorThickness: Dp = 8.dp,
//    indicatorColor: Color = Color.LightGray,
//    alpha: Float = if (state.isScrollInProgress) 0.8f else 0f,
//    alphaAnimationSpec: AnimationSpec<Float> = tween(
//        delayMillis = if (state.isScrollInProgress) 0 else 1500,
//        durationMillis = if (state.isScrollInProgress) 150 else 500
//    ),
//    padding: PaddingValues = PaddingValues(all = 0.dp)
//): Modifier = composed {
//    val scrollbarAlpha by animateFloatAsState(
//        targetValue = alpha,
//        animationSpec = alphaAnimationSpec
//    )
//
//    drawWithContent {
//        drawContent()
//
//        val showScrollBar = state.isScrollInProgress || scrollbarAlpha > 0.0f
//
//        // Draw scrollbar only if currently scrolling or if scroll animation is ongoing.
//        if (showScrollBar) {
//            val (topPadding, bottomPadding, startPadding, endPadding) = listOf(
//                padding.calculateTopPadding().toPx(), padding.calculateBottomPadding().toPx(),
//                padding.calculateStartPadding(layoutDirection).toPx(),
//                padding.calculateEndPadding(layoutDirection).toPx()
//            )
//            val contentOffset = state.value
//            val viewPortLength = if (direction == Orientation.Vertical)
//                size.height else size.width
//            val viewPortCrossAxisLength = if (direction == Orientation.Vertical)
//                size.width else size.height
//            val contentLength = max(viewPortLength + state.maxValue, 0.001f)  // To prevent divide by zero error
//            val indicatorLength = ((viewPortLength / contentLength) * viewPortLength) - (
//                    if (direction == Orientation.Vertical) topPadding + bottomPadding
//                    else startPadding + endPadding
//                    )
//            val indicatorThicknessPx = indicatorThickness.toPx()
//
//            val scrollOffsetViewPort = viewPortLength * contentOffset / contentLength
//
//            val scrollbarSizeWithoutInsets = if (direction == Orientation.Vertical)
//                Size(indicatorThicknessPx, indicatorLength)
//            else Size(indicatorLength, indicatorThicknessPx)
//
//            val scrollbarPositionWithoutInsets = if (direction == Orientation.Vertical)
//                Offset(
//                    x = if (layoutDirection == LayoutDirection.Ltr)
//                        viewPortCrossAxisLength - indicatorThicknessPx - endPadding
//                    else startPadding,
//                    y = scrollOffsetViewPort + topPadding
//                )
//            else
//                Offset(
//                    x = if (layoutDirection == LayoutDirection.Ltr)
//                        scrollOffsetViewPort + startPadding
//                    else viewPortLength - scrollOffsetViewPort - indicatorLength - endPadding,
//                    y = viewPortCrossAxisLength - indicatorThicknessPx - bottomPadding
//                )
//
//            drawRoundRect(
//                color = indicatorColor,
//                cornerRadius = CornerRadius(
//                    x = indicatorThicknessPx / 2, y = indicatorThicknessPx / 2
//                ),
//                topLeft = scrollbarPositionWithoutInsets,
//                size = scrollbarSizeWithoutInsets,
//                alpha = scrollbarAlpha
//            )
//        }
//    }
//}
//
//data class ScrollBarConfig(
//    val indicatorThickness: Dp = 8.dp,
//    val indicatorColor: Color = Color.LightGray,
//    val alpha: Float? = null,
//    val alphaAnimationSpec: AnimationSpec<Float>? = null,
//    val padding: PaddingValues = PaddingValues(all = 0.dp)
//)
//
//fun Modifier.verticalScrollWithScrollbar(
//    state: ScrollState,
//    enabled: Boolean = true,
//    flingBehavior: FlingBehavior? = null,
//    reverseScrolling: Boolean = false,
//    scrollbarConfig: ScrollBarConfig = ScrollBarConfig()
//) = this
//    .scrollbar(
//        state, Orientation.Vertical,
//        indicatorThickness = scrollbarConfig.indicatorThickness,
//        indicatorColor = scrollbarConfig.indicatorColor,
//        alpha = scrollbarConfig.alpha ?: if (state.isScrollInProgress) 0.8f else 0f,
//        alphaAnimationSpec = scrollbarConfig.alphaAnimationSpec ?: tween(
//            delayMillis = if (state.isScrollInProgress) 0 else 1500,
//            durationMillis = if (state.isScrollInProgress) 150 else 500
//        ),
//        padding = scrollbarConfig.padding
//    )
//    .verticalScroll(state, enabled, flingBehavior, reverseScrolling)
//
//
//
//fun Modifier.horizontalScrollWithScrollbar(
//    state: ScrollState,
//    enabled: Boolean = true,
//    flingBehavior: FlingBehavior? = null,
//    reverseScrolling: Boolean = false,
//    scrollbarConfig: ScrollBarConfig = ScrollBarConfig()
//) = this
//    .scrollbar(
//        state, Orientation.Horizontal,
//        indicatorThickness = scrollbarConfig.indicatorThickness,
//        indicatorColor = scrollbarConfig.indicatorColor,
//        alpha = scrollbarConfig.alpha ?: if (state.isScrollInProgress) 0.8f else 0f,
//        alphaAnimationSpec = scrollbarConfig.alphaAnimationSpec ?: tween(
//            delayMillis = if (state.isScrollInProgress) 0 else 1500,
//            durationMillis = if (state.isScrollInProgress) 150 else 500
//        ),
//        padding = scrollbarConfig.padding
//    )
//    .horizontalScroll(state, enabled, flingBehavior, reverseScrolling)