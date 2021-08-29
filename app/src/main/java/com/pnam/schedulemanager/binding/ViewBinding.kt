package com.pnam.schedulemanager.binding

import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.utils.RetrofitUtils.getMediaUrl

@BindingAdapter("circle_image_from_url")
fun imageCircleFromUrl(imageView: ImageView, url: String?) {
    if (url == null) {
        imageView.setImageResource(R.drawable.ic_empty)
        imageView.animate().cancel()
    } else {
        imageView.load(getMediaUrl(url)) {
            transformations(CircleCropTransformation())
            scale(Scale.FILL)
            crossfade(true)
            error(R.drawable.ic_error)
            placeholder(R.drawable.ic_loading)
            listener(onSuccess = { _, _ ->
                imageView.animate().cancel()
            }, onError = { _, _ ->
                imageView.animate().cancel()
                error(R.drawable.ic_error)
            })
        }
    }
}

@BindingAdapter("image_from_url")
fun imageFromUrl(imageView: ImageView, url: String?) {
    if (url == null) {
        imageView.setImageResource(R.drawable.ic_empty)
        imageView.animate().cancel()
    } else {
        imageView.load(getMediaUrl(url)) {
            scale(Scale.FILL)
            crossfade(true)
            error(R.drawable.ic_error)
            listener(onSuccess = { _, _ ->
                imageView.animate().cancel()
            }, onError = { _, _ ->
                imageView.animate().cancel()
                error(R.drawable.ic_error)
            })
        }
    }
}

fun ImageView.loading() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate))
}