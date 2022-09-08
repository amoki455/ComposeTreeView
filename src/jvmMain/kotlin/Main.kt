import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.jvmName

@Composable
@Preview
fun App() {
    MaterialTheme {
        var nodes by remember { mutableStateOf(listOf<TreeNode>()) }
        rememberCoroutineScope().launch {
            nodes = listOf(
                TreeNode(
                    name = AnnotatedString(AnnotatedString::class.jvmName),
                    children = getMethodsOfClass(AnnotatedString::class.java)
                )
            )
        }
        Surface(
            color = Color.DarkGray
        ) {
            TreeView(
                modifier = Modifier.fillMaxHeight()
                    .widthIn(280.dp, Dp.Unspecified),
                nodes = nodes
            )
        }
    }
}

fun getMethodsOfClass(clazz: Class<*>?): List<TreeNode> {
    val nodes = mutableListOf<TreeNode>()
    clazz?.methods?.forEach {
        val children = mutableListOf<TreeNode>()
        nodes.add(
            TreeNode(
                name = buildAnnotatedString {
                    append(it.name)
                    append("(")
                    it.parameters.forEachIndexed { i, p ->
                        if (i != 0) {
                            withStyle(SpanStyle(Color.White)) {
                                append(", ")
                            }
                        }
                        withStyle(SpanStyle(Color(0xffff8e32))) {
                            append(p.name)
                        }
                        withStyle(SpanStyle(Color.White)) {
                            append(": ")
                        }
                        withStyle(SpanStyle(Color(0xffff8e32))) {
                            append(p.type.name)
                        }
                    }
                    append("): ")
                    withStyle(SpanStyle(Color(0xffff8e32))) {
                         append(it.returnType.name)
                    }
                },
                onClick = {
                    if (children.isEmpty()) {
                        children.addAll(getMethodsOfClass(it.returnType))
                    }
                },
                children = children
            )
        )
    }
    return nodes
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
