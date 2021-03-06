package com.pnam.schedulemanager.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.pnam.schedulemanager.R


abstract class BaseActivity<BD : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes override val layoutRes: Int
) : AppCompatActivity(), BaseUI<BD, VM> {

    override var _binding: BD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView<BD>(this, layoutRes).apply {
            lifecycleOwner = this@BaseActivity
        }
        createUI()
    }

    inline fun <reified F : Fragment> navigateFragment(
        @IdRes container: Int,
        fragment: Fragment,
        transactionViews: List<View>? = null
    ) {
        val tag = fragment.javaClass.simpleName
        val fragmentFindByTag: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.commit {
            transactionViews?.forEach { transactionView ->
                addSharedElement(transactionView, transactionView.transitionName)
            }
            setReorderingAllowed(true)
            if (fragmentFindByTag == null) {
                add(container, fragment, tag)
                addToBackStack(tag)
            } else {
                supportFragmentManager.findFragmentById(container)?.let {
                    hide(it)
                }
                show(fragment)
            }
        }
    }

    protected val isEmptyFragmentBackStack: Boolean
        get() = supportFragmentManager.backStackEntryCount == 0

    private var clickFirstTime: Long = 0

    protected fun twiceTimeToExit() {
        if (clickFirstTime == 0L) {
            clickFirstTime = System.currentTimeMillis()
            showToast(getString(R.string.mess_when_click_back_btn))
        } else {
            if (System.currentTimeMillis() - clickFirstTime < 2000L) {
                finishAffinity()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        intent.getParcelableExtra<StartType>(START_TYPE)?.let { effect ->
            when (effect) {
                StartType.SLIDE_HORIZONTAL -> {
                    overridePendingTransition(
                        R.anim.slide_out_right,
                        R.anim.slide_in_left
                    )
                }
                StartType.SLIDE_SECOND -> {
                    overridePendingTransition(
                        R.anim.static_inout,
                        R.anim.slide_out_top
                    )
                }
            }
        }
    }

    companion object {
        internal const val START_TYPE: String = "startType"
    }
}