package com.louiscad.complete_kotlin

import com.louiscad.complete_kotlin.internal.CompilerVersion
import org.junit.jupiter.api.TestFactory
import testutils.junit.mapDynamicTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CompilerVersionTest {

    @Test
    fun testMyKnowledge() {
        assertEquals(
            expected = "1.5",
            actual = CompilerVersion.fromString("1.5").toString()
        )
    }

    @TestFactory
    fun compareSubstringApproachWithCompilerVersionClass() =
        kotlinVersionsOnMavenCentral().mapDynamicTest { kotlinVersion ->
            val compilerVersion = CompilerVersion.fromString(kotlinVersion).toString()
            val preDashPart = kotlinVersion.substringBefore(delimiter = '-')
            val postDashPart = kotlinVersion.substringAfter(delimiter = '-', missingDelimiterValue = "").let {
                if (it.isEmpty()) "" else "-$it"
            }
            val computedKotlinVersion = preDashPart.substringBefore(".0") + postDashPart
            @OptIn(ExperimentalStdlibApi::class)
            assertEquals(
                expected = compilerVersion,
                actual = computedKotlinVersion
            )
        }

    private fun kotlinVersionsOnMavenCentral(): List<String> = listOf(
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
        "1.4.21-2",
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
