/*
 * Copyright 2021 Louis Cognault Ayeva Derman
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.louiscad.complete_kotlin.internal

import java.io.File

internal class KotlinNativeCompilerInfo(
    val compilerVersion: CompilerVersion,
    val hostPlatform: HostPlatform,
) {

    companion object {
        val konanDir = File(System.getProperty("user.home")).resolve(".konan")
    }

    private val isPublishedToMavenCentral = compilerVersion.isAtLeast(CompilerVersion.fromString("1.9.20"))

    private val kindaSimpleOsName: String = when {
        compilerVersion.supportsPlatformName() -> when (hostPlatform) {
            HostPlatform.current -> HostPlatform.platformName
            HostPlatform.MacOS -> "macos-aarch64"
            HostPlatform.Linux -> "linux-x86_64"
            HostPlatform.Windows -> "mingw-x86_64"
        }
        else -> hostPlatform.simpleOsName
    }

    private val artifactId: String = "kotlin-native-prebuilt"

    /**
     * The name of the root directory contained inside the Native compiler archive, or as seen in the .konan directory.
     * This follows the old style pattern `kotlin-native-prebuilt-<platform>-<version>` for all versions.
     *
     * Example: `kotlin-native-prebuilt-windows-x86_64-2.0.20`
     */
    val rootDirName: String = "$artifactId-$kindaSimpleOsName-$compilerVersion"

    /**
     * The name of the Kotlin/Native prebuilt archive, without the extension.
     *
     * * On JetBrains CDN (for versions < 1.9.20), the pattern is `kotlin-native-prebuilt-<platform>-<version>`.
     * * On Maven Central (for versions >= 1.9.20), the pattern is `kotlin-native-prebuilt-<version>-<platform>`.
     *
     * Examples:
     *  * `kotlin-native-prebuilt-1.9.10-windows-x86_64`
     *  * `kotlin-native-prebuilt-windows-x86_64-2.0.20`
     */
    val archiveNameWithoutExtension: String = if (isPublishedToMavenCentral) {
        "$artifactId-$compilerVersion-$kindaSimpleOsName"
    } else {
        "$artifactId-$kindaSimpleOsName-$compilerVersion"
    }

    val useZip = hostPlatform == HostPlatform.Windows
    val archiveExtension: String = if (useZip) "zip" else "tar.gz"

    /**
     * The URL of the repository, including the path to the directory that directly contains the artifact.
     *
     * Examples:
     *  * `https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-native-prebuilt/2.0.20`
     *  * `https://complete-kotlin-prebuilt.louiscad.com/kotlin/native/builds/releases/1.9.10/macos-x86_64`
     */
    val repoUrl: String
        get() = if (isPublishedToMavenCentral) {
            "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-native-prebuilt/$compilerVersion"
        } else {
            "https://complete-kotlin-prebuilt.louiscad.com/kotlin/native/builds".let { baseDownloadUrl ->
                val releasePath = when (compilerVersion.meta) {
                    MetaVersion.DEV -> "dev"
                    else -> "releases"
                }
                "$baseDownloadUrl/$releasePath/$compilerVersion/$kindaSimpleOsName"
            }
        }

    /**
     * The direct URL to the prebuilt Kotlin/Native distribution archive.
     *
     * Example: `https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-native-prebuilt/2.0.20/kotlin-native-prebuilt-2.0.20-macos-x86_64.tar.gz`
     */
    val dependencyUrl: String
        get() = "$repoUrl/$archiveNameWithoutExtension.$archiveExtension"
}
