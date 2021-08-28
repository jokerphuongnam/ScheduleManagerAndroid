package com.pnam.schedulemanager.utils

import android.graphics.Bitmap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream


fun List<Bitmap>.toMultipartBodies(
    partName: String
): List<MultipartBody.Part> {
    return map {
        it.toMultipartBody(partName)
    }
}

fun Bitmap.toMultipartBody(partName: String): MultipartBody.Part {
    val byteArrayOutputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return MultipartBody.Part.createFormData(
        partName,
        "filename.jpeg",
        RequestBody.create(
            "image/jpeg".toMediaTypeOrNull(),
            byteArray
        )
    )
}