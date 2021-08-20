package com.pnam.schedulemanager.throwable

class UnknownException(private val _message: String? = null) : Exception(_message) {
    override val message: String
        get() = _message ?: "Unknown exception"
}
