package com.pnam.schedulemanager.ui.base

import android.animation.ValueAnimator
import android.app.ProgressDialog
import android.content.Intent
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.pnam.schedulemanager.R

interface BaseUI<BD : ViewDataBinding, VM : BaseViewModel> {
    val layoutRes: Int
    fun createUI()
    var _binding: BD?
    val binding: BD get() = _binding!!
    val viewModel: VM

    fun <T> LiveData<T>.observe(observer: Observer<T>) {
        type({
            observe(viewLifecycleOwner, observer)
        }) {
            observe(this, observer)
        }
    }

    fun showToast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
        val context = when (this) {
            is Fragment -> {
                requireActivity()
            }
            is AppCompatActivity -> {
                this
            }
            else -> throw Exception("can inherit by a Fragment or Activity")
        }
        Toast.makeText(context, message, duration).show()
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        type({
            Toast.makeText(requireContext().applicationContext, message, duration).show()
        }) {
            Toast.makeText(applicationContext, message, duration).show()
        }
    }

    private fun type(
        fragment: (Fragment.() -> Unit)? = null,
        activity: (AppCompatActivity.() -> Unit)? = null
    ) {
        when (this) {
            is Fragment -> {
                fragment ?: return
                fragment()
            }
            is AppCompatActivity -> {
                activity ?: return
                activity()
            }
        }
    }

//    val actionBarSize: Int

    fun <VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.setSource() {

    }

    /**
     * observer internet error if Activity or Fragment need this case will call this func
     * */
    fun noInternetError() {
        viewModel.internetError.observe {
            type({
                showToast(getString(R.string.no_internet))
            }) {
                showToast(getString(R.string.no_internet))
            }
        }
    }

    fun View.setPaddingAnimation(left: Int, top: Int, right: Int, bottom: Int) {
        ValueAnimator.ofInt(0, left, top, right, bottom).apply {
            duration = 1000
            addListener {
                setPadding(left, top, right, bottom)
            }
            start()
        }
    }

    fun View.setViewHeightAnimation(height: Int) {
        ValueAnimator.ofInt(0, height).apply {
            val params = layoutParams
            params.height = height
            layoutParams = params
        }
    }

    fun View.setScrollFlag(flags: Int) {
        val params: AppBarLayout.LayoutParams = layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = flags
        layoutParams = params
    }

    fun makeSceneTransitionAnimation(vararg view: View): ActivityOptionsCompat {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
            when (this) {
                is Fragment -> {
                    requireActivity()
                }
                is AppCompatActivity -> {
                    this
                }
                else -> throw Exception("can inherit by a Fragment or Activity")
            },
            *view.map { v ->
                Pair(v, ViewCompat.getTransitionName(v)!!)
            }.toTypedArray()
        )
    }

    fun showProgressDialog(@StringRes message: Int? = null) {
        val context = when (this) {
            is Fragment -> {
                requireActivity()
            }
            is AppCompatActivity -> {
                this
            }
            else -> throw Exception("can inherit by a Fragment or Activity")
        }
        progressDialog = ProgressDialog(context)
        progressDialog?.let { dialog ->
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            message?.let {
                dialog.setMessage(context.getString(it))
            }
            dialog.show()
        }
    }

    fun dismissProgressDialog() {
        progressDialog?.setCancelable(true)
        progressDialog?.setCanceledOnTouchOutside(true)
        progressDialog?.cancel()
        progressDialog?.dismiss()
        progressDialog = null
    }

    fun List<View>.setOnClickListener(l: View.OnClickListener) {
        forEach {
            it.setOnClickListener(l)
        }
    }

    companion object {
        private var progressDialog: ProgressDialog? = null
    }
}