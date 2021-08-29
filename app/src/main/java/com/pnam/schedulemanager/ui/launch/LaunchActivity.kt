package com.pnam.schedulemanager.ui.launch

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityLaunchBinding
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.ui.dashboard.DashboardActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LaunchActivity : BaseActivity<ActivityLaunchBinding, BaseViewModel>(
    R.layout.activity_launch
) {
    private val duration: Long = 1500

    override fun createUI() {
        binding.title.animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
        binding.logo.animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_top)

        binding.title.animation.duration = duration
        binding.logo.animation.duration = duration

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(this, DashboardActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                },
                makeSceneTransitionAnimation(binding.title).toBundle()
            )
            finish()
        }, duration)
    }

    @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
    override val viewModel by lazy { TODO() }
}