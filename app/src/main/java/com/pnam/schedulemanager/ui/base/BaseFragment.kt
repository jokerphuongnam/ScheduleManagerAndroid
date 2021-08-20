package com.pnam.schedulemanager.ui.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

abstract class BaseFragment<BD : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes override val layoutRes: Int
) : Fragment(), BaseUI<BD, VM> {
    override var _binding: BD? = null

    override var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate<BD>(layoutInflater, layoutRes, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createUI()
    }

    inline fun <reified F : Fragment> navigateFragmentChild(
        @LayoutRes resFragment: Int,
        vararg params: Any,
        tag: String,
        @AnimRes start: Int? = null,
        @AnimRes end: Int? = null
    ) {
        val supportFragmentManager: FragmentManager = childFragmentManager
        val fragment: Fragment =
            supportFragmentManager.findFragmentByTag(tag) ?: F::class.constructors.find {
                it.parameters.size == params.size
            }?.call(params)!!
        supportFragmentManager.commit {
            addToBackStack(tag)
            if (start != null && end != null) {
                setCustomAnimations(start, end)
            }
            replace(resFragment, fragment, tag)
        }
    }


    inline fun <reified F : Fragment> navigateFragmentParent(
        @LayoutRes resFragment: Int,
        vararg params: Any,
        tag: String,
        @AnimRes start: Int? = null,
        @AnimRes end: Int? = null
    ) {
        val supportFragmentManager: FragmentManager = parentFragmentManager
        val fragment: Fragment =
            supportFragmentManager.findFragmentByTag(tag) ?: F::class.constructors.find {
                it.parameters.size == params.size
            }?.call(params)!!
        supportFragmentManager.commit {
            addToBackStack(null)
            if (start != null && end != null) {
                setCustomAnimations(start, end)
            }
            replace(resFragment, fragment, tag)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override val actionBarSize: Int by lazy {
//        TypedValue.complexToDimensionPixelSize(TypedValue().data, requireActivity().resources.displayMetrics)
//    }

    companion object {
        fun <F : Fragment> create(f: F, bundleHandle: (Bundle.() -> Unit)? = null): F = f.apply {
            val bundle = Bundle()
            bundleHandle?.invoke(bundle)
            arguments = bundle
        }
    }
}