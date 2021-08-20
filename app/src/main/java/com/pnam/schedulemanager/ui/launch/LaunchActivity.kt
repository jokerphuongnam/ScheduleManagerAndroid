package com.pnam.schedulemanager.ui.launch

import android.content.Intent
import androidx.activity.viewModels
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityLaunchBinding
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.dashboard.DashboardActivity
import com.pnam.schedulemanager.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LaunchActivity: BaseActivity<ActivityLaunchBinding, LaunchViewModel>(R.layout.activity_launch) {
    override val viewModel: LaunchViewModel by viewModels()

    override fun createUI() {
        startActivity(Intent(this, DashboardActivity::class.java))
    }
}