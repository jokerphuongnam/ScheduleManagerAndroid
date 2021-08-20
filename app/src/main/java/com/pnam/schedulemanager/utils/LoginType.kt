package com.pnam.schedulemanager.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class LoginType(val rawValue: String): Parcelable {
    EMAIL_PASS("email_pass"),
    GOOGLE_SIGN_IN("google_sign_in"),
    NONE("")
}