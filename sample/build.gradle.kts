plugins {
    kotlin("multiplatform") version "1.5.32"
    id("com.louiscad.complete-kotlin")
}

kotlin {
    macosX64()
    linuxMips32()
}
