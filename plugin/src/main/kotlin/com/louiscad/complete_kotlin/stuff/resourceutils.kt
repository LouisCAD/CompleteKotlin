package com.louiscad.complete_kotlin.stuff

import org.gradle.api.Project
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

@Throws(IOException::class)
internal fun Project.probeRemoteFileLength(url: String, probingTimeoutMs: Int = 0): Long? {
    val connection = URL(url).openConnection()
    if (connection !is HttpURLConnection) {
        logger.debug(::probeRemoteFileLength.name + "($url, $probingTimeoutMs): Failed to obtain content-length. Likely not an HTTP-based URL. URL connection class is ${connection::class.java}.")
        return null
    }

    return try {
        connection.requestMethod = "HEAD"
        connection.connectTimeout = probingTimeoutMs
        connection.readTimeout = probingTimeoutMs
        connection.contentLengthLong.takeIf { it >= 0 }
    } catch (e: SocketTimeoutException) {
        if (probingTimeoutMs == 0)
            throw e
        else {
            logger.debug(::probeRemoteFileLength.name + "($url, $probingTimeoutMs): Failed to obtain content-length during the probing timeout.")
            null
        }
    } finally {
        connection.disconnect()
    }
}
