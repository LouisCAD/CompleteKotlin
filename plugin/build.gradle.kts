import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish")
}

version = file("version.txt").useLines { it.first() }.also { check(it == "1.1.0-SNAPSHOT") }
group = "com.louiscad.complete-kotlin"

gradlePlugin {
    // Define the plugin
    plugins.create("complete-kotlin") {
        id = "com.louiscad.complete-kotlin"
        displayName = "CompleteKotlin"
        description = "Enable auto-completion and symbol resolution for all Kotlin/Native platforms."
        implementationClass = "com.louiscad.complete_kotlin.CompleteKotlinPlugin"
    }
}

pluginBundle {
    website = "https://github.com/LouisCAD/CompleteKotlin"
    vcsUrl = "https://github.com/LouisCAD/CompleteKotlin.git"
    tags = listOf("kotlin", "kotlin-multiplatform", "kmm", "plugins")
}

repositories { mavenCentral() }

dependencies {
    implementation(gradleKotlinDsl())
    testImplementation(Kotlin.test.junit5)
}

tasks.withType<KotlinJvmCompile>().configureEach {
    kotlinOptions.apiVersion = "1.4"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}
