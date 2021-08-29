package com.pnam.schedulemanager.throwable

import java.io.IOException

class WrongException(private val _message: String? = null) : IOException(_message) {
    override val message: String
        get() = _message ?: "Wrong"
}