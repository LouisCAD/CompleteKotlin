plugins {
    kotlin("multiplatform") version "2.3.0"
    id("com.louiscad.complete-kotlin")
}

kotlin {
    macosX64()
    linuxMips32()
}
