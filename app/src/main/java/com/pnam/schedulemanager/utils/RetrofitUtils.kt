package com.pnam.schedulemanager.utils

object RetrofitUtils {
    const val BASE_URL: String = "https://schedulemanagerapis.herokuapp.com/"

    const val SUCCESS: Int = 200
    const val BAD_REQUEST: Int = 400
    const val NOT_FOUND: Int = 404
    const val CONFLICT: Int = 409
    const val INTERNAL_SERVER_ERROR: Int = 500

    fun getMediaUrl(imageName: String): String {
        return "${BASE_URL}media/$imageName"
    }

    const val IMAGES: String = "images"
    const val SOUNDS: String = "sounds"
    const val AVATAR: String = "avatar"
}