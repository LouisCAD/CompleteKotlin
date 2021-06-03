package org.gradle.kotlin.dsl

import com.louiscad.complete_kotlin.CompleteKotlinExtension
import org.gradle.api.initialization.Settings

inline fun Settings.completeKotlin(configure: CompleteKotlinExtension.() -> Unit) {
    // This function is needed because Gradle doesn't generate accessors for
    // settings extensions.
    extensions.getByType<CompleteKotlinExtension>().configure()
}
