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
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType

class CompleteKotlinPlugin : Plugin<Any> {

    override fun apply(target: Any) {
        require(target is Settings) {
            val notInExtraClause: String = when (target) {
                is Project -> when (target) {
                    target.rootProject -> ", not in build.gradle(.kts)"
                    else -> ", not in a build.gradle(.kts) file."
                }
                is Gradle -> ", not in an initialization script."
                else -> ""
            }
            """
            plugins.id("com.louiscad.complete-kotlin") must be configured in settings.gradle(.kts)$notInExtraClause.
            See https://github.com/LouisCAD/CompleteKotlin
            """.trimIndent()
        }
        val isInCi: Boolean = System.getenv("CI") == "true"
        if (isInCi) {
            println("CompleteKotlin has detected it's being run on CI, so it disabled itself to save bandwidth.")
            return
        }
        setup(target)
    }

    private fun setup(settings: Settings) {
        settings.extensions.create<CompleteKotlinExtension>("completeKotlin")
        settings.gradle.settingsEvaluated {
            val extension: CompleteKotlinExtension = settings.extensions.getByType()

            settings.gradle.beforeProject {
                project.plugins.withId("org.jetbrains.kotlin.multiplatform") {
                    project.completePlatformKlibsIfNeeded()
                }
            }
        }
    }
}
