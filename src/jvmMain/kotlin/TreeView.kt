import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class TreeNode(
    val name: AnnotatedString,
    val textColor: Color = Color.White,
    val backgroundColor: Color = Color.Transparent,
    val icon: ImageVector? = defaultIcon,
    val iconColor: Color = Color.White,
    val highlightColor: Color = Color.Blue.copy(alpha = 0.4f),
    val onClick: (() -> Unit)? = null,
    val children: List<TreeNode>? = null,
)

@Composable
fun TreeView(
    modifier: Modifier = Modifier,
    nodes: List<TreeNode>
) {
    var selectedKey by remember { mutableStateOf("") }
    val expandedKeys = remember { mutableStateListOf<String>() }
    ColumnWithScrollbar(
        modifier = modifier
    ) {
        nodes.forEachIndexed { index, node ->
            treeNodeItem(
                node = node,
                padding = 0.dp,
                key = "parent_$index",
                onSelect = { selectedKey = it },
                selectedKey = selectedKey,
                onExpand = {
                    if (node.children != null && node.children.isNotEmpty()) {
                        if (it in expandedKeys) {
                            expandedKeys.remove(it)
                        } else {
                            expandedKeys.add(it)
                        }
                    }
                },
                expandedKeys = expandedKeys
            )
        }
    }
}


private fun LazyListScope.treeNodeItem(
    node: TreeNode,
    padding: Dp,
    key: String,
    onSelect: (String) -> Unit,
    onExpand: (String) -> Unit,
    selectedKey: String,
    expandedKeys: List<String>
) {
    val paddingFactor = 16.dp
    val parentsKeys = getParentsKeys(key)
    val isVisible = if (parentsKeys.isEmpty()) {
        true
    } else {
        parentsKeys.all { it in expandedKeys }
    }

    if (isVisible) {
        item(key) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(28.dp)
                    .background(if (selectedKey == key) node.highlightColor else node.backgroundColor)
                    .clickable {
                        onSelect(key)
                        node.onClick?.invoke()
                        onExpand(key)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                (1..padding.value.toInt().div(paddingFactor.value.toInt())).forEach { _ ->
                    Spacer(Modifier.width(paddingFactor))
                    Spacer(
                        modifier = Modifier.width(1.dp)
                            .fillMaxSize()
                            .background(node.textColor.copy(alpha = 0.3f))
                    )
                }
                if (node.children != null && node.children.isNotEmpty()) {
                    Icon(
                        imageVector = if (key in expandedKeys) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                        contentDescription = "",
                        tint = node.textColor,
                        modifier = Modifier.size(28.dp)
                            .padding(start = 5.dp)
                            .clickable {
                                onExpand(key)
                            }
                    )
                } else {
                    Spacer(Modifier.size(28.dp))
                }
                if (node.icon != null) {
                    Icon(
                        imageVector = node.icon,
                        contentDescription = "",
                        tint = node.iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Spacer(Modifier.size(24.dp))
                }
                Text(
                    text = node.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = node.textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
    if (node.children != null && node.children.isNotEmpty()) {
        val childrenPadding = padding + paddingFactor
        node.children.forEachIndexed { i, childNode ->
            val childNodeKey = "$key.child_$i"
            treeNodeItem(
                node = childNode,
                padding = childrenPadding,
                key = childNodeKey,
                onSelect = onSelect,
                selectedKey = selectedKey,
                onExpand = onExpand,
                expandedKeys = expandedKeys
            )
        }
    }
}

private fun getParentsKeys(childKey: String): List<String> {
    val parentsKeys = mutableListOf<String>()
    val splitKey = childKey.split(".")
    if (splitKey.size > 1) {
        (1..splitKey.size).forEach {
            if (it < splitKey.size) {
                parentsKeys.add(splitKey.dropLast(it).joinToString("."))
            } else {
                parentsKeys.add(splitKey[0])
            }
        }
    }
    return parentsKeys
}

private val defaultIcon: ImageVector
    get() {
        if (s != null) {
            return s!!
        } else {
            s = materialIcon(name = "TreeNode.Default") {
                materialPath {
                    moveTo(3.0f, 6.0f)
                    horizontalLineToRelative(13.0f)
                    verticalLineToRelative(13.0f)
                    lineTo(3.0f, 19.0f)
                    verticalLineToRelative(13f)
                    close()
                }
            }
        }
        return s!!
    }

private var s: ImageVector? = null