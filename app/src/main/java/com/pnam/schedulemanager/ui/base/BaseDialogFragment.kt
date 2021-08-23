package com.pnam.schedulemanager.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.pnam.schedulemanager.R


abstract class BaseDialogFragment<BD : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes override val layoutRes: Int
) : DialogFragment(), BaseUI<BD, VM> {
    override var _binding: BD? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate<BD>(layoutInflater, layoutRes, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.let { window ->
            window.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)
            resources.displayMetrics.let { metrics ->
                val width = (metrics.widthPixels * 0.80).toInt()
                val height = (metrics.heightPixels * 0.70).toInt()
                window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
        createUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}