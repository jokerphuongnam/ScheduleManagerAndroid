package com.pnam.schedulemanager.utils

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Source
import okio.source
import java.io.IOException
import java.io.InputStream


class FileRequestBody(private val inputStream: InputStream, private val type: MediaType?) :
    RequestBody() {

    override fun contentType(): MediaType? {
        return type
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return inputStream.available().toLong()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        var source: Source? = null
        try {
            source = inputStream.source()
            sink.writeAll(source)
        } catch (e: Exception) {
            if (source != null) {
                source.close()
            }
        }
    }
}