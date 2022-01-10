pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }

    val versionFile = rootDir.parentFile.resolve("plugin/version.txt")
    val pluginVersion = versionFile.useLines { it.first() }

    @Suppress("UnstableApiUsage")
    plugins {
        id("com.louiscad.complete-kotlin") version pluginVersion
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}
