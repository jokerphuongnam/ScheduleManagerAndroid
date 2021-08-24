package com.pnam.schedulemanager.model.database.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Search(
    var searchId: String? = null,
    var avatar: String? = null,
    var word: String = "",
    var searchTime: Long? = null,
    var userId: String? = null
) : Parcelable