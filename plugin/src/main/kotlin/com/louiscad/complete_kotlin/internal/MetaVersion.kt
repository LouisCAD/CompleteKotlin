package com.louiscad.complete_kotlin.internal

enum class MetaVersion(val metaString: String) {
    DEV("dev"),
    DEV_GOOGLE("dev-google-pr"),
    EAP("eap"),
    BETA("beta"),
    M1("M1"),
    M2("M2"),
    RC("RC"),
    PUB("PUB"),
    RELEASE("release");

    companion object {
        fun findAppropriate(metaString: String): MetaVersion {
            return values().find { it.metaString.equals(metaString, ignoreCase = true) }
                ?: if (metaString.isBlank()) RELEASE else error("Unknown meta version: $metaString")
        }
    }
}
