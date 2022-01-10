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

    private val kindaSimpleOsName: String = when {
        compilerVersion.supportsPlatformName() -> when (hostPlatform) {
            HostPlatform.current -> HostPlatform.platformName
            HostPlatform.MacOS -> "macos-aarch64"
            HostPlatform.Linux -> "linux-x86_64"
            HostPlatform.Windows -> "mingw-x86_64"
        }
        else -> hostPlatform.simpleOsName
    }

    val dependencyName: String = "kotlin-native-prebuilt-$kindaSimpleOsName"
    val dependencyNameWithVersion: String = "$dependencyName-$compilerVersion"

    val useZip = hostPlatform == HostPlatform.Windows
    val archiveExtension: String = if (useZip) "zip" else "tar.gz"

    val repoUrl: String
        get() = "https://complete-kotlin-prebuilt.louiscad.com/kotlin/native/builds".let { baseDownloadUrl ->
            val releasePath = when (compilerVersion.meta) {
                MetaVersion.DEV -> "dev"
                else -> "releases"
            }
            "$baseDownloadUrl/$releasePath/$compilerVersion/$kindaSimpleOsName"
        }
    val dependencyUrl: String
        get() {
            val dependencyFileName = "$dependencyNameWithVersion.$archiveExtension"
            return "$repoUrl/$dependencyFileName"
        }
}
