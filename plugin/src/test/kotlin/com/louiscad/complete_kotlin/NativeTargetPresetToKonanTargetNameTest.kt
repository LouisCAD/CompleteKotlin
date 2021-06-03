/*
 * Copyright 2021 Louis Cognault Ayeva Derman
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.louiscad.complete_kotlin

import com.louiscad.complete_kotlin.internal.konanTargetName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import testutils.junit.mapDynamicTest
import kotlin.test.assertEquals

class NativeTargetPresetToKonanTargetNameTest {

    @TestFactory
    fun `test conversion of kotlin native preset name to konan target name`(): List<DynamicTest> {
        return knownTargetNames.mapDynamicTest { targetName ->
            val presetName = konanTargetNameToPresetName(targetName)
            val computedTargetName = konanTargetName(kotlinNativeTargetPresetName = presetName)
            assertEquals(
                expected = targetName,
                actual = computedTargetName
            )
        }
    }

    private val knownTargetNames = listOf(
        "android_x64",
        "android_x86",
        "android_arm32",
        "android_arm64",
        "ios_arm32",
        "ios_arm64",
        "ios_x64",
        "ios_simulator_arm64",
        "watchos_arm32",
        "watchos_arm64",
        "watchos_x86",
        "watchos_x64",
        "watchos_simulator_arm64",
        "tvos_arm64",
        "tvos_x64",
        "tvos_simulator_arm64",
        "linux_x64",
        "mingw_x86",
        "mingw_x64",
        "macos_x64",
        "macos_arm64",
        "linux_arm64",
        "linux_arm32_hfp",
        "linux_mips32",
        "linux_mipsel32",
        "wasm32"
    )

    private fun konanTargetNameToPresetName(targetName: String): String {
        // Code ported from the following files in the Kotlin repo:
        // - org.jetbrains.kotlin.konan.target.utils.kt
        // - org.jetbrains.kotlin.konan.target.KonanTarget.kt
        return when (targetName) {
            "android_arm64" -> "androidNativeArm64"
            "android_arm32" -> "androidNativeArm32"
            "android_x86" -> "androidNativeX86"
            "android_x64" -> "androidNativeX64"
            else -> {
                val nameParts = targetName.split('_').mapNotNull {
                    it.takeIf(String::isNotEmpty)
                }
                nameParts.asSequence().drop(1).joinToString(
                    separator = "",
                    prefix = nameParts.firstOrNull().orEmpty()
                ) {
                    it.capitalize()
                }
            }
        }
    }
}
