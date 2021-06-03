package com.louiscad.complete_kotlin.internal

/**
 *  [en.wikipedia.org/wiki/Software_versioning](https://en.wikipedia.org/wiki/Software_versioning)
 *  scheme major.minor[.build[.revision]].
 */
internal enum class MetaVersion(val metaString: String) {
    DEV("dev"),
    EAP("eap"),
    ALPHA("alpha"),
    BETA("beta"),
    RC1("rc1"),
    RC2("rc2"),
    RELEASE("release");

    companion object {

        fun findAppropriate(metaString: String): MetaVersion {
            return MetaVersion.values().find { it.metaString.equals(metaString, ignoreCase = true) }
                ?: if (metaString.isBlank()) RELEASE else error("Unknown meta version: $metaString")
        }
    }
}
