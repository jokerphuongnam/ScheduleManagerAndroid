package com.pnam.schedulemanager.throwable

import java.io.IOException

/**
 * if device don't have internet (wifi, 3G, 4G, 5G, ...) will throw this exception
 * */
class NoConnectivityException(private val _message: String? = null) : IOException(_message) {
    override val message: String
        get() = _message ?: "No Internet Connection"
}