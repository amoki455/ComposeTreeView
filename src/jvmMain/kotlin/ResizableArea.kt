import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun ResizableArea(
    modifier: Modifier,
    onRequestNewWidth: ((Dp) -> Unit)? = null,
    onRequestNewHeight: ((Dp) -> Unit)? = null,
    dragAreaSize: Dp = 16.dp,
    dragAreaColor: Color = Color.Blue.copy(alpha = 0.2f),
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    var boxSize = remember { DpSize(0.dp, 0.dp) }
    val horizontalDragState = rememberDraggableState {
        onRequestNewWidth?.invoke(
            boxSize.width + with(density) {
                it.toDp()
            }
        )
    }
    val verticalDragState = rememberDraggableState {
        onRequestNewHeight?.invoke(
            boxSize.height + with(density) {
                it.toDp()
            }
        )
    }

    BoxWithConstraints(
        modifier = modifier,
    ) {
        boxSize = DpSize(maxWidth, maxHeight)
        Column {
            Row(
                modifier = Modifier.height(boxSize.height.minus(if (onRequestNewHeight != null) dragAreaSize else 0.dp))
            ) {
                val contentWidth = boxSize.width.minus(if (onRequestNewWidth != null) dragAreaSize else 0.dp)
                Box(
                    modifier = Modifier.width(contentWidth)
                ) {
                    content()
                }
                DraggableBar(
                    state = horizontalDragState,
                    orientation = Orientation.Horizontal,
                    color = dragAreaColor
                )
            }
            if (onRequestNewHeight != null) {
                DraggableBar(
                    state = verticalDragState,
                    orientation = Orientation.Vertical,
                    color = dragAreaColor
                )
            }
        }
    }
}

@Composable
private fun DraggableBar(
    state: DraggableState,
    orientation: Orientation,
    color: Color
) {
    Surface(
        color = color,
        modifier = Modifier.fillMaxSize()
            .draggable(
                state = state,
                orientation = orientation
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}