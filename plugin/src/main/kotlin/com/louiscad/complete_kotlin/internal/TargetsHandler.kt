/*
 * Copyright 2021-2022 Louis Cognault Ayeva Derman
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.louiscad.complete_kotlin.internal

import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import kotlin.reflect.full.memberProperties

internal fun Project.completePlatformKlibsIfNeeded() {

    // We are using reflection here because we cannot depend on the Kotlin Gradle plugin
    // as it would lock the version.
    // Using compileOnly is unfortunately not an option either, because
    // it leads to ClassNotFoundException like this one:
    // java.lang.ClassNotFoundException: org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

    val extension = extensions.getByName("kotlin")

    //TODO: Get the CompilerVersion lazily (lazy delegate cannot be used)
    val compilerVersion = CompilerVersion.current(project = project, objectForResourceLookup = extension)

    run {
        val minimumKotlinVersion = "1.5.30"
        if (compilerVersion.isAtLeast(CompilerVersion.fromString(minimumKotlinVersion)).not()) {
            val errorMessage = "This version of CompleteKotlin requires the project to " +
                    "use Kotlin $minimumKotlinVersion or newer. " +
                    "Use version 1.0.0 of CompleteKotlin if you can't upgrade Kotlin in this project."
            throw UnsupportedOperationException(errorMessage)
        }
    }

    val hostCompilerDirs = KotlinNativeCompilerDirs(compilerVersion)


    val targets = extension.javaClass.kotlin.memberProperties.first {
        it.name == "targets"
    }.call(extension) as NamedDomainObjectCollection<*>

    targets.all {
        val presetName = (this.javaClass.kotlin.memberProperties.first {
            it.name == "preset"
        }.call(this) as Named).name
        when (val platform = exclusiveHostPlatform(presetName = presetName)) {
            HostPlatform.current, null -> return@all
            else -> {
                if (hostCompilerDirs.hasPlatformKlibsForTarget(presetName)) {
                    return@all
                }
                val compilerInfo = KotlinNativeCompilerInfo(
                    compilerVersion = compilerVersion,
                    hostPlatform = platform
                )
                val installer = PlatformKlibsInstaller(
                    project = project,
                    hostCompilerDirs = hostCompilerDirs,
                    platformCompilerInfo = compilerInfo
                )
                installer.downloadAndExtract()

            }
        }
    }
}

private fun exclusiveHostPlatform(presetName: String): HostPlatform? = when {
    presetName.startsWith("mingw") -> HostPlatform.Windows
    presetName.startsWith("linuxMips") -> HostPlatform.Linux
    presetName.startsWith("macos") -> HostPlatform.MacOS
    presetName.startsWith("ios") -> HostPlatform.MacOS
    presetName.startsWith("watchos") -> HostPlatform.MacOS
    presetName.startsWith("tvos") -> HostPlatform.MacOS
    else -> null
}
