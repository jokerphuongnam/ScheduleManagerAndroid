package com.pnam.schedulemanager.ui.ring

import android.content.Intent
import androidx.activity.viewModels
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityRingBinding
import com.pnam.schedulemanager.foreground.AlarmBroadcastReceiver
import com.pnam.schedulemanager.foreground.AlarmService
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.base.BaseViewModel

class RingActivity : BaseActivity<ActivityRingBinding, BaseViewModel>(R.layout.activity_ring) {
    override fun createUI() {
        intent.getParcelableExtra<Schedule>(AlarmBroadcastReceiver.SCHEDULE)?.let { schedule ->
            binding.schedule = schedule
        }
        binding.activityRingDismiss.setOnClickListener {
            val intent = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(intent)
            finish()
        }
    }

    override val viewModel: BaseViewModel by viewModels()
}