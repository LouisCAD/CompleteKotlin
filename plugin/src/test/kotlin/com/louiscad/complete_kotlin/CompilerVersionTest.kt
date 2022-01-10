package com.louiscad.complete_kotlin

import com.louiscad.complete_kotlin.internal.CompilerVersion
import com.louiscad.complete_kotlin.internal.isAtLeast
import org.junit.jupiter.api.TestFactory
import testutils.junit.mapDynamicTest
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CompilerVersionTest {

    @TestFactory
    fun `check older Kotlin versions detection`() = unsupportedKotlinCompilerVersions().mapDynamicTest { version ->
        val compilerVersion = CompilerVersion.fromString(version)
        assertFalse(
            actual = compilerVersion.isAtLeast(CompilerVersion.fromString("1.5.30"))
        )
    }

    @TestFactory
    fun `check detection of supported Kotlin versions`() = supportedKotlinCompilerVersions().mapDynamicTest { version ->
        val compilerVersion = CompilerVersion.fromString(version)
        assertTrue(
            actual = compilerVersion.isAtLeast(CompilerVersion.fromString("1.5.30"))
        )
    }

    private fun supportedKotlinCompilerVersions(): List<String> = listOf(
        "1.5.32",
        "1.6.0-M1",
        "1.6.0-RC",
        "1.6.0-RC2",
        "1.6.0",
        "1.6.10-RC",
        "1.6.10",
    )

    private fun unsupportedKotlinCompilerVersions(): List<String> = listOf(
        "1.2.50",
        "1.2.51",
        "1.2.60",
        "1.2.61",
        "1.2.70",
        "1.2.71",
        "1.3.0-rc-190",
        "1.3.0-rc-198",
        "1.3.0",
        "1.3.10",
        "1.3.11",
        "1.3.20",
        "1.3.21",
        "1.3.30",
        "1.3.31",
        "1.3.40",
        "1.3.41",
        "1.3.50",
        "1.3.60",
        "1.3.61",
        "1.3.70",
        "1.3.71",
        "1.3.72",
        "1.4.0-rc",
        "1.4.0",
        "1.4.10",
        "1.4.20-M1",
        "1.4.20-M2",
        "1.4.20-RC",
        "1.4.20",
        "1.4.21",
        //"1.4.21-2", Fails with "Unknown meta version: 2", but it's unlikely we need to support this anymore.
        "1.4.30-M1",
        "1.4.30-RC",
        "1.4.30",
        "1.4.31",
        "1.4.32",
        "1.5.0-M1",
        "1.5.0-M2",
        "1.5.0-RC",
        "1.5.0",
        "1.5.10",
        "1.5.20-M1",
        "1.5.0",
        "1.5.10",
        "1.5.0-M1"
    )
}
