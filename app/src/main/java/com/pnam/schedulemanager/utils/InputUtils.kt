package com.pnam.schedulemanager.utils

import androidx.annotation.StringRes
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.throwable.NoErrorException
import java.util.*

@Throws(NoErrorException::class)
@StringRes
fun String.usernameRegex(): Int = when {
    indexOf('@') == -1 -> {
        R.string.email_need_at
    }

    isEmpty() -> {
        R.string.email_empty
    }
    length < 6 -> {
        R.string.email_length
    }
    indexOf(' ') != -1 -> {
        R.string.email_only_alphabet
    }
    else -> {
        throw NoErrorException()
    }
}

@Throws(NoErrorException::class)
@StringRes
fun String.passwordRegex(): Int = when {
    isEmpty() -> {
        R.string.password_empty
    }
    length < 6 -> {
        R.string.password_length
    }
    indexOf(' ') != -1 -> {
        R.string.password_only_alphabet
    }
    else -> {
        throw NoErrorException()
    }
}

@Throws(NoErrorException::class)
@StringRes
fun String.nameRegex(): Int = when {
    isEmpty() -> {
        R.string.name_empty
    }
    length < 2 -> {
        R.string.name_length
    }
    else -> {
        throw NoErrorException()
    }
}

@Throws(NoErrorException::class)
@StringRes
fun Long.birthdayRegex(): Int = when {
    this > System.currentTimeMillis() -> {
        R.string.great_than_current_date
    }
    else -> {
        throw  NoErrorException()
    }
}