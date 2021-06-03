/*
 * Copyright 2021 Louis Cognault Ayeva Derman
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.louiscad.complete_kotlin.internal

import java.util.Locale

internal fun konanTargetName(kotlinNativeTargetPresetName: String): String {
    return when {
        kotlinNativeTargetPresetName.startsWith("androidNative") -> {
           kotlinNativeTargetPresetName.replace(
               oldValue = "androidNative",
               newValue = "android_"
           ).toLowerCase(Locale.ROOT)
        }
        else -> kotlinNativeTargetPresetName.toSnakeCase()
    }
}

private fun String.toSnakeCase() = replace(humps, "_").toLowerCase()
private val humps = "(?<=.)(?=\\p{Upper})".toRegex()
