#!/usr/bin/env kotlin

/*
 * Copyright 2022 Louis Cognault Ayeva Derman
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

import java.io.File
import java.io.FileNotFoundException
import java.net.URL
import java.nio.file.Paths
import kotlin.system.exitProcess

//@file:Repository("https://repo.maven.apache.org/maven2/")
//@file:Repository("https://oss.sonatype.org/content/repositories/snapshots")
//@file:Repository("file:///Users/louiscad/.m2/repository")
//@file:DependsOn("com.louiscad.incubator:lib-publishing-helpers:0.2.3")


val filePaths = listOf(
    "compiler/util-io/src/org/jetbrains/kotlin/konan/CompilerVersion.kt",
    "compiler/util-io/src/org/jetbrains/kotlin/konan/MetaVersion.kt",
    "libraries/tools/kotlin-gradle-plugin/src/main/kotlin/org/jetbrains/kotlin/gradle/targets/native/NativeCompilerDownloader.kt",
    "native/utils/src/org/jetbrains/kotlin/konan/target/KonanTarget.kt",
    "native/utils/src/org/jetbrains/kotlin/konan/target/HostManager.kt",
)

checkFilesDidntChange(
    baseUrl = "https://github.com/JetBrains/kotlin/raw/master",
    fileRelativePaths = filePaths,
    directory = Paths.get("").toFile().resolve("expected-files")
)
println("Success")

fun checkFilesDidntChange(
    baseUrl: String,
    fileRelativePaths: List<String>,
    directory: File
) {
    val urlStart = baseUrl.removeSuffix("/")
    fileRelativePaths.forEach { filePath ->
        val url = "$urlStart/$filePath"
        try {
            val actualFileContent = URL(url).readText()
            val localFile = directory.resolve(filePath)
            val expectedFileContent = localFile.takeIf { it.exists() }?.readText()
            if (actualFileContent != expectedFileContent) {
                localFile.parentFile.mkdirs()
                localFile.resolveSibling(
                    relative = "${localFile.nameWithoutExtension}.actual.${localFile.extension}"
                ).writeText(actualFileContent)
                System.err.println("Expected file content doesn't match actual one for path $filePath")
                exitProcess(1)
            }
        } catch (e: FileNotFoundException) {
            System.err.println("Actual file for path $filePath wasn't found at $url")
            exitProcess(1)
        } catch (e: Exception) {
            throw RuntimeException("An issue happened while comparing $filePath", e)
        }
    }
}
