package com.pnam.schedulemanager.utils

object RetrofitUtils {
    const val BASE_URL: String = "https://schedulemanagerapis.herokuapp.com/"

    const val SUCCESS: Int = 200
    const val BAD_REQUEST: Int = 400
    const val NOT_FOUND: Int = 404
    const val CONFLICT: Int = 409
    const val INTERNAL_SERVER_ERROR: Int = 500

    fun getMediaUrl(mediaName: String): String {
        return "${BASE_URL}media/$mediaName"
    }

    const val IMAGES: String = "images"
    const val AUDIOS: String = "audios"
    const val VIDEOS: String = "videos"
    const val APPLICATIONS: String = "applications"
    const val AVATAR: String = "avatar"
}