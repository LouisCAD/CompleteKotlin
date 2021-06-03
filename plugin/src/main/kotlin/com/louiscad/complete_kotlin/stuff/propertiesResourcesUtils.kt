package com.louiscad.complete_kotlin.stuff

import java.io.FileNotFoundException
import java.util.Properties

fun Any.loadPropertyFromResources(propFileName: String, property: String): String {
    val props = Properties()
    val inputStream = javaClass.classLoader!!.getResourceAsStream(propFileName)
        ?: throw FileNotFoundException("property file '$propFileName' not found in the classpath")

    inputStream.use { props.load(it) }
    return props[property] as String
}
