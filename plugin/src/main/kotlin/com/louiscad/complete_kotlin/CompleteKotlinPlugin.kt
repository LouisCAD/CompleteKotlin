/*
 * Copyright 2021 Louis Cognault Ayeva Derman
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.louiscad.complete_kotlin

import com.louiscad.complete_kotlin.internal.completePlatformKlibsIfNeeded
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

class CompleteKotlinPlugin : Plugin<Any> {

    override fun apply(target: Any) {
        val isInCi: Boolean = System.getenv("CI") == "true"
        if (isInCi) {
            println("CompleteKotlin has detected it's being run on CI, so it disabled itself to save bandwidth.")
            return
        }
        /* // Commented out because we currently have no config options in the Gradle extension.
        settings.extensions.create<CompleteKotlinExtension>("completeKotlin")
        settings.gradle.settingsEvaluated {
            val extension: CompleteKotlinExtension = settings.extensions.getByType()
            // ...
        }
        */
        when (target) {
            is Settings -> target.gradle.beforeProject {
                setupIfNeeded(project)
            }
            is Project -> when (target) {
                target.rootProject -> target.allprojects {
                    setupIfNeeded(project)
                }
                else -> setupIfNeeded(target)
            }
            else -> {
                val notInExtraClause: String = when (target) {
                    is Gradle -> ", not in an initialization script."
                    else -> ""
                }
                val errorMessage = """
                plugins.id("com.louiscad.complete-kotlin") must be configured in settings.gradle(.kts)$notInExtraClause.
                See https://github.com/LouisCAD/CompleteKotlin
                """.trimIndent()
                throw IllegalArgumentException(errorMessage)
            }
        }
    }

    private fun setupIfNeeded(project: Project) {
        project.plugins.withId("org.jetbrains.kotlin.multiplatform") {
            project.completePlatformKlibsIfNeeded()
        }
    }
}
