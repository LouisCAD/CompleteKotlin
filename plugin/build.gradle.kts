import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "2.0.0"
}

version = file("version.txt").useLines { it.first() }
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

//pluginBundle {
//    website = "https://github.com/LouisCAD/CompleteKotlin"
//    vcsUrl = "https://github.com/LouisCAD/CompleteKotlin.git"
//    tags = listOf("kotlin", "kotlin-multiplatform", "kmm", "plugins")
//}

repositories { mavenCentral() }

dependencies {
    implementation(kotlin("gradle-plugin", "2.3.0"))
    testImplementation(kotlin("test-junit5"))
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}
