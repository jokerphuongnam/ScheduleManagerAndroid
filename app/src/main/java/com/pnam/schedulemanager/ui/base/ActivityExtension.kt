package com.pnam.schedulemanager.ui.base

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Activity.createImageFile(): File? {
    val imageFileName = "Camera_picker"
    var mFileTemp: File? = null
    val root: String = getDir("camera", Context.MODE_PRIVATE).absolutePath
    val myDir = File("$root/Img")
    if (!myDir.exists()) {
        myDir.mkdirs()
    }
    try {
        mFileTemp = File.createTempFile(imageFileName, ".jpg", myDir.absoluteFile)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return mFileTemp
}

fun Activity.uriToBitmap(bitmap: Bitmap): Uri? {
    val file = createImageFile()
    if (file != null) {
        val fout: FileOutputStream
        try {
            fout = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout)
            fout.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }
    return null
}