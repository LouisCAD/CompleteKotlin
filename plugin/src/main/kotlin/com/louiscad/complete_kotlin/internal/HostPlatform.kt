/*
 * Copyright 2021 Louis Cognault Ayeva Derman
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.louiscad.complete_kotlin.internal

import java.util.Locale
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberFunctions

internal enum class HostPlatform {
    Linux, Windows, MacOS;

    val simpleOsName: String = name.toLowerCase(Locale.ROOT)

    companion object {
        val platformName: String by lazy {
            Class.forName("org.jetbrains.kotlin.konan.target.HostManager").kotlin.let {
                it.companionObject!!.memberFunctions.first { function ->
                    function.name == "platformName"
                }.call(it.companionObjectInstance) as String
            }
        }
        val current: HostPlatform = System.getProperty("os.name").let { javaOsName ->
            when {
                javaOsName == "Mac OS X" -> MacOS
                javaOsName == "Linux" -> Linux
                javaOsName.startsWith("Windows") -> Windows
                else -> throw IllegalStateException("Unknown operating system: $javaOsName")
            }
        }
    }
}
