/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.louiscad.complete_kotlin.internal

import com.louiscad.complete_kotlin.stuff.loadPropertyFromResources
import java.io.Serializable

internal interface CompilerVersion : Serializable {
    val meta: MetaVersion
    val major: Int
    val minor: Int
    val maintenance: Int

    @Deprecated("Milestone is deprecated in favour to MetaVersion's M1 and M2")
    val milestone: Int

    val build: Int

    fun toString(showMeta: Boolean, showBuild: Boolean): String

    companion object {
        // major.minor.patch-meta-build where patch, meta and build are optional.
        private val versionPattern = "(\\d+)\\.(\\d+)(?:\\.(\\d+))?(?:-(\\p{Alpha}\\p{Alnum}|[\\p{Alpha}-]*))?(?:-(\\d+))?".toRegex()

        fun current(objectForResourceLookup: Any): CompilerVersion = fromString(
            objectForResourceLookup.loadPropertyFromResources(
                propFileName = "project.properties",
                property = "kotlin.native.version"
            )
        )

        fun fromString(version: String): CompilerVersion {
            val (major, minor, maintenance, metaString, build) =
                versionPattern.matchEntire(version)?.destructured
                    ?: throw IllegalArgumentException("Cannot parse Kotlin/Native version: $version")

            return CompilerVersionImpl(
                MetaVersion.findAppropriate(metaString),
                major.toInt(),
                minor.toInt(),
                maintenance.toIntOrNull() ?: 0,
                -1,
                build.toIntOrNull() ?: -1
            )
        }

        operator fun invoke(
            meta: MetaVersion = MetaVersion.DEV,
            major: Int,
            minor: Int,
            maintenance: Int,
            milestone: Int = -1,
            build: Int = -1
        ): CompilerVersion = CompilerVersionImpl(
            meta = meta,
            major = major,
            minor = minor,
            maintenance = maintenance,
            milestone = milestone,
            build = build
        )
    }
}

internal fun CompilerVersion.supportsPlatformName(): Boolean {
    return isAtLeast(firstKotlinVersionWithArchDependentCompilerArchives)
}

private val firstKotlinVersionWithArchDependentCompilerArchives = CompilerVersion(
    major = 1,
    minor = 5,
    maintenance = 30,
    build = 1466
)

private fun CompilerVersion.isAtLeast(compilerVersion: CompilerVersion): Boolean {
    if (this.major != compilerVersion.major) return this.major > compilerVersion.major
    if (this.minor != compilerVersion.minor) return this.minor > compilerVersion.minor
    if (this.maintenance != compilerVersion.maintenance) return this.maintenance > compilerVersion.maintenance
    if (this.meta.ordinal != compilerVersion.meta.ordinal) return this.meta.ordinal > compilerVersion.meta.ordinal
    return this.build >= compilerVersion.build
}

private data class CompilerVersionImpl(
    override val meta: MetaVersion,
    override val major: Int,
    override val minor: Int,
    override val maintenance: Int,
    @Deprecated("Milestone is deprecated in favour to MetaVersion's M1 and M2")
    override val milestone: Int,
    override val build: Int
) : CompilerVersion {

    override fun toString(showMeta: Boolean, showBuild: Boolean) = buildString {
        append(major)
        append('.')
        append(minor)
        append('.')
        append(maintenance)
        if (showMeta) {
            append('-')
            append(meta.metaString)
        }
        if (showBuild && build != -1) {
            append('-')
            append(build)
        }
    }

    private val isRelease: Boolean
        get() = meta == MetaVersion.RELEASE

    private val versionString by lazy { toString(!isRelease, true) }

    override fun toString() = versionString
}
