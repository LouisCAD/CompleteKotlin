package testutils.junit

import org.junit.jupiter.api.DynamicTest
import java.io.File

fun dynamicTest(displayName: String, block: () -> Unit): DynamicTest {
    return DynamicTest.dynamicTest(displayName, block)
}

@JvmName("mapFileListToDynamicTest")
fun List<File>.mapDynamicTest(
    name: (File) -> String = { it.name },
    block: (File) -> Unit
): List<DynamicTest> = map { file ->
    dynamicTest(displayName = name(file), block = { block(file) })
}

fun <T> List<T>.mapDynamicTest(
    name: (T) -> String = { it.toString() },
    block: (T) -> Unit
): List<DynamicTest> = map { t ->
    dynamicTest(displayName = name(t), block = { block(t) })
}
