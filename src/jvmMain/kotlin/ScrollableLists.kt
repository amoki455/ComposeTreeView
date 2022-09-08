import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ColumnWithScrollbar(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: LazyListScope.() -> Unit
) {
    val scrollState = rememberLazyListState()
    Box(modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = scrollState,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
        VerticalScrollbar(
            rememberScrollbarAdapter(scrollState),
            modifier = Modifier.align(Alignment.TopEnd)
                .width(5.dp),
            style = LocalScrollbarStyle.current.copy(
                unhoverColor = MaterialTheme.colors.onSurface,
                hoverColor = MaterialTheme.colors.secondary
            )
        )
    }
}

@Composable
fun RowWithScrollbar(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: LazyListScope.() -> Unit
) {
    val scrollState = rememberLazyListState()
    Box(modifier) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = scrollState,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            content()
        }
        HorizontalScrollbar(
            rememberScrollbarAdapter(scrollState),
            modifier = Modifier.align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(5.dp),
            style = LocalScrollbarStyle.current.copy(
                unhoverColor = MaterialTheme.colors.onSurface,
                hoverColor = MaterialTheme.colors.secondary
            )
        )
    }
}