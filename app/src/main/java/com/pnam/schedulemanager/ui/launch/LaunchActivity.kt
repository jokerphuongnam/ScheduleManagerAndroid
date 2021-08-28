package com.pnam.schedulemanager.ui.launch

import android.content.Intent
import android.os.Handler
import androidx.activity.viewModels
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityLaunchBinding
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.dashboard.DashboardActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LaunchActivity: BaseActivity<ActivityLaunchBinding, LaunchViewModel>(R.layout.activity_launch) {
    override val viewModel: LaunchViewModel by viewModels()

    override fun createUI() {
        Handler().postDelayed({
            startActivity(
                Intent(this, DashboardActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                },
                makeSceneTransitionAnimation(binding.title).toBundle()
            )
            overridePendingTransition(
                R.anim.slide_in_top,
                R.anim.static_inout
            )
            finish()
        }, 2000)
    }
}