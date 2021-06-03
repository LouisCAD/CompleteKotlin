/*
 * Copyright 2021 Louis Cognault Ayeva Derman
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.louiscad.complete_kotlin.internal

internal class KotlinNativeCompilerDirs(compilerVersion: CompilerVersion) {

    private val compilerInfo = KotlinNativeCompilerInfo(
        compilerVersion = compilerVersion,
        hostPlatform = HostPlatform.current
    )

    val compilerDir = KotlinNativeCompilerInfo.konanDir.resolve(compilerInfo.dependencyNameWithVersion)
    val platformKlibsDir = compilerDir.resolve("klib").resolve("platform")
    val completeKotlinExtractionDir = platformKlibsDir.resolve("tmp-complete-kotlin")

    fun hasPlatformKlibsForTarget(kotlinNativeTargetPresetName: String): Boolean {
        val konanTargetName = konanTargetName(kotlinNativeTargetPresetName)
        return platformKlibsDir.resolve(konanTargetName).exists()
    }
}
