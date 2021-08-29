package com.pnam.schedulemanager.ui.dashboard

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityDashboardBinding
import com.pnam.schedulemanager.foreground.AlarmBroadcastReceiver
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.base.putParcelableExtra
import com.pnam.schedulemanager.ui.base.slideSecondActivity
import com.pnam.schedulemanager.ui.login.LoginActivity
import com.pnam.schedulemanager.ui.scheduleInfo.ScheduleInfoActivity
import com.pnam.schedulemanager.ui.setting.SettingActivity
import com.pnam.schedulemanager.utils.Resource
import com.pnam.schedulemanager.utils.toCalendar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding, DashboardViewModel>(
    R.layout.activity_dashboard
) {
    private val schedulesAdapter: SchedulesAdapter by lazy {
        SchedulesAdapter(selectedItem)
    }

    private val selectedItem: (Schedule, List<View>) -> Unit by lazy {
        { schedule, sharedElements ->
            scheduleInfoLauncher.launch(
                Intent(this@DashboardActivity, ScheduleInfoActivity::class.java).apply {
                    putExtra(SCHEDULE, schedule)
                },
                makeSceneTransitionAnimation(*sharedElements.toTypedArray())
            )
        }
    }

    private fun initNotesRecycler() {
        binding.notesRecycler.apply {
            adapter = schedulesAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
    }

    private val scheduleInfoLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.getSchedules()
            }
        }

    private fun clearAlarm() {
        val updateServiceIntent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        val pendingUpdateIntent = PendingIntent.getService(
            applicationContext,
            0,
            updateServiceIntent,
            0
        )
        try {
            (applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(
                pendingUpdateIntent
            )
        } catch (e: Exception) {
        }
    }

    private fun scheduleAlarm(schedule: Schedule) {
        val alarmBroadCastIntent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        alarmBroadCastIntent.putParcelableExtra(AlarmBroadcastReceiver.SCHEDULE, schedule)
        val scheduleAlarm = schedule.scheduleTime.toCalendar
        scheduleAlarm[Calendar.SECOND] = 0
        scheduleAlarm[Calendar.MILLISECOND] = 0
        val alarmPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            -schedule.scheduleTime.toInt(),
            alarmBroadCastIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        (getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            scheduleAlarm.timeInMillis,
            alarmPendingIntent
        )
    }

    private fun setupViewMode() {
        viewModel.apply {
            scheduleLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        schedulesAdapter.submitList(resource.data.sortedWith(compareBy { it.scheduleTime }))
                        binding.schedulesRefresh.isRefreshing = false
                        clearAlarm()
                        val currentTime = System.currentTimeMillis()
                        resource.data.filter { schedule ->
                            val scheduleAlarm = schedule.scheduleTime.toCalendar
                            scheduleAlarm[Calendar.SECOND] = 0
                            scheduleAlarm[Calendar.MILLISECOND] = 0
                            scheduleAlarm.timeInMillis > currentTime
                        }.forEach { schedule ->
                            scheduleAlarm(schedule)
                        }
                    }
                    is Resource.Error -> {
                        binding.schedulesRefresh.isRefreshing = false
                    }
                }
            }
            userLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        toolbar.loading()
                    }
                    is Resource.Success -> {
                        if (resource.data == null) {
                            toolbar.cancelAnimation()
                            logout()
                        } else {
                            toolbar.setUser(resource.data)
                            viewModel.getSchedules()
                        }
                    }
                    is Resource.Error -> {
                        toolbar.emptyAvatar()
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            addBtn.setOnClickListener {
                scheduleInfoLauncher.launch(
                    Intent(this@DashboardActivity, ScheduleInfoActivity::class.java).apply {

                    }, makeSceneTransitionAnimation(
                        it
                    )
                )
            }
            schedulesRefresh.setOnRefreshListener { viewModel.getSchedules() }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        toolbar.onCreate()
    }

    private val toolbar: DashboardToolbar by lazy {
        DashboardToolbar()
    }

    private var _profileItem: MenuItem? = null
    private val profileItem: MenuItem get() = _profileItem!!

    private val settingActivityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.getUser()
    }

    private fun setupUser() {
        toolbar.setBinding(profileItem.actionView.findViewById(R.id.container))
        toolbar.avatarClick = View.OnClickListener {
            settingActivityResult.launch(
                Intent(this@DashboardActivity, SettingActivity::class.java).apply {
                    when (val resource = viewModel.userLiveData.value) {
                        is Resource.Success -> {
                            putExtra(USER, resource.data)
                        }
                    }
                },
                makeSceneTransitionAnimation(toolbar.binding.avatar)
            )
        }
    }

    private val loginIntent by lazy {
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    fun logout() {
        slideSecondActivity(loginIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        _profileItem = menu.findItem(R.id.profile)
        setupUser()
        return true
    }

    override fun createUI() {
        setupToolbar()
        initNotesRecycler()
        setupViewMode()
        setupAction()
        viewModel.getUser()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSchedules()
    }

    override val viewModel: DashboardViewModel by viewModels()

    companion object {
        const val USER: String = "user"
        const val SCHEDULE: String = "schedule"
    }
}