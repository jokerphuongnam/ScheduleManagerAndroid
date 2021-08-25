package com.pnam.schedulemanager.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.pnam.schedulemanager.R
import kotlinx.parcelize.Parcelize
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

fun Fragment.showToastActivity(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireActivity(), message, duration).show()
}


fun Activity.slideHActivity(intent: Intent, bundle: Bundle? = null) {
    intent.putParcelableExtra(BaseActivity.START_TYPE, StartType.SLIDE_HORIZONTAL)
    startActivity(intent, bundle)
    overridePendingTransition(
        R.anim.slide_in_right,
        R.anim.slide_out_left
    )
}

fun Activity.slideSecondActivity(intent: Intent, bundle: Bundle? = null) {
    intent.putParcelableExtra(BaseActivity.START_TYPE, StartType.SLIDE_SECOND)
    startActivity(intent, bundle)
    overridePendingTransition(
        R.anim.slide_in_top,
        R.anim.static_inout
    )
}

fun Intent.putParcelableExtra(key: String, value: Parcelable) {
    putExtra(key, value)
}

@Parcelize
internal enum class StartType : Parcelable {
    SLIDE_HORIZONTAL, SLIDE_SECOND
}


var TextInputLayout.text: String
    get() {
        editText ?: return ""
        return editText!!.text.toString()
    }
    set(value) {
        editText?.setText(value)
    }