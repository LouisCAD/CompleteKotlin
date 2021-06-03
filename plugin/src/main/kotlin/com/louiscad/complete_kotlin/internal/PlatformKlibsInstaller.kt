/*
 * Copyright 2021 Louis Cognault Ayeva Derman
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.louiscad.complete_kotlin.internal

import com.louiscad.complete_kotlin.stuff.formatContentLength
import com.louiscad.complete_kotlin.stuff.lifecycleWithDuration
import com.louiscad.complete_kotlin.stuff.probeRemoteFileLength
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.file.FileTree
import org.gradle.api.logging.Logger
import java.io.File

internal class PlatformKlibsInstaller(
    private val project: Project,
    private val hostCompilerDirs: KotlinNativeCompilerDirs,
    private val platformCompilerInfo: KotlinNativeCompilerInfo
) {

    private val logger: Logger get() = project.logger

    private fun archiveFileTree(archive: File): FileTree = when {
        platformCompilerInfo.useZip -> project.zipTree(archive)
        else -> project.tarTree(archive)
    }

    private fun setupRepo(repoUrl: String): ArtifactRepository {
        return project.repositories.ivy {
            setUrl(repoUrl)
            patternLayout {
                artifact("[artifact]-[revision].[ext]")
            }
            metadataSources {
                artifact()
            }
        }
    }

    private fun removeRepo(repo: ArtifactRepository) {
        project.repositories.remove(repo)
    }

    fun downloadAndExtract() {

        val repo = setupRepo(repoUrl = platformCompilerInfo.repoUrl)

        val compilerDependency = project.dependencies.create(
            mapOf(
                "name" to platformCompilerInfo.dependencyName,
                "version" to platformCompilerInfo.compilerVersion.toString(),
                "ext" to platformCompilerInfo.archiveExtension
            )
        )

        val configuration = project.configurations.detachedConfiguration(compilerDependency)
        with(platformCompilerInfo) {
            logger.lifecycle("\nPlease wait while missing platform klibs for of Kotlin/Native $compilerVersion for $hostPlatform exclusive targets are being installed.")
        }

        val suffix = project.probeRemoteFileLength(
            url = platformCompilerInfo.dependencyUrl,
            probingTimeoutMs = 200
        )?.let { " (${formatContentLength(it)})" }.orEmpty()
        logger.lifecycle("Downloading the magic $suffix")
        val archive = logger.lifecycleWithDuration("Download of the magic complete") {
            configuration.files.single()
        }

        logger.info("Using magic archive: ${archive.absolutePath}")

        val compilerDirectory: File = hostCompilerDirs.compilerDir
        logger.lifecycle("Unpacking magic to $compilerDirectory")
        logger.lifecycleWithDuration("Unpack of the magic to $compilerDirectory finished,") {
            project.copy {
                from(archiveFileTree(archive)) {
                    include("${platformCompilerInfo.dependencyNameWithVersion}/klib/platform/**")
                }
                into(hostCompilerDirs.completeKotlinExtractionDir)
            }
            val platformKlibsDir = hostCompilerDirs.platformKlibsDir
            val existingPlatformKlibDirNames = platformKlibsDir.listFiles { file ->
                file.isDirectory
            }?.asSequence()?.map { it.name }?.toList() ?: emptyList()
            hostCompilerDirs.completeKotlinExtractionDir
                .resolve(platformCompilerInfo.dependencyNameWithVersion)
                .resolve("klib")
                .resolve("platform")
                .listFiles { file ->
                    file.isDirectory && file.name !in existingPlatformKlibDirNames
                }?.forEach { dir ->
                    dir.renameTo(platformKlibsDir.resolve(dir.name))
                }
            project.delete(hostCompilerDirs.completeKotlinExtractionDir)
        }

        removeRepo(repo)
    }
}
