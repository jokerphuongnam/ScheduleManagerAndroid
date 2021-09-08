package com.pnam.schedulemanager.ui.scheduleInfo

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityScheduleInfoBinding
import com.pnam.schedulemanager.foreground.AlarmBroadcastReceiver
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.base.BaseFragment
import com.pnam.schedulemanager.ui.base.putParcelableExtra
import com.pnam.schedulemanager.ui.dashboard.DashboardActivity.Companion.SCHEDULE
import com.pnam.schedulemanager.ui.scheduleInfo.inputtask.InputTaskDialog
import com.pnam.schedulemanager.ui.scheduleInfo.members.MembersDialog
import com.pnam.schedulemanager.utils.Resource
import com.pnam.schedulemanager.utils.toCalendar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScheduleInfoActivity : BaseActivity<ActivityScheduleInfoBinding, ScheduleInfoViewModel>(
    R.layout.activity_schedule_info
) {
    private val takePhotoFromCamera: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                (result.data?.extras?.get("data") as Bitmap).apply {
                    viewModel.insertMedia(this)
                }
            }
        }

    private val fileChoose: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    viewModel.insertFile(uri)
                }
            }
        }

    private val tasksInfoAdapter: TasksAdapter by lazy {
        TasksAdapter({ task ->
            viewModel.deleteTask(task.taskId)
        }, { isChecked, task ->
            viewModel.toggleTask(task, isChecked)
        }, { task ->
            BaseFragment.create(InputTaskDialog()) {
                putParcelable(TASK, task)
                putString(COLOR, viewModel.newSchedule.value?.color)
            }.show(supportFragmentManager, InputTaskDialog::class.simpleName)
        })
    }

    private val colorsAdapter: ColorsAdapter by lazy {
        ColorsAdapter { color ->
            colorsAdapter.selectedColor = color
            binding.background.setBackgroundColor(Color.parseColor(color.rawValue))
        }
    }

    private val imagesAdapter: ImagesAdapter by lazy {
        ImagesAdapter { image ->
            viewModel.deleteMedia(image.mediaId)
        }
    }

    private val filesAdapter: FilesAdapter by lazy {
        FilesAdapter({ file ->
            viewModel.deleteMedia(file.mediaId)
        }) { file ->
            viewModel.downloadFile(file)
        }
    }

    private fun recyclerViewSetUp() {
        binding.apply {
            tasks.apply {
                adapter = tasksInfoAdapter
                layoutManager = LinearLayoutManager(
                    this@ScheduleInfoActivity,
                    RecyclerView.VERTICAL,
                    false
                )
            }
            images.apply {
                adapter = imagesAdapter
                layoutManager = LinearLayoutManager(
                    this@ScheduleInfoActivity,
                    RecyclerView.VERTICAL,
                    false
                )
            }
            colors.apply {
                adapter = colorsAdapter
                layoutManager = LinearLayoutManager(
                    this@ScheduleInfoActivity,
                    RecyclerView.HORIZONTAL,
                    false
                )
            }
            files.apply {
                adapter = filesAdapter
                layoutManager = LinearLayoutManager(
                    this@ScheduleInfoActivity,
                    RecyclerView.VERTICAL,
                    false
                )
            }
        }
    }

    private fun setBackgroundColor(hexColor: String) {
        val color = Color.parseColor(hexColor)
        val colorStateList = ColorStateList.valueOf(color)
        binding.changeMode.setTextColor(color)
        binding.changeMode.rippleColor = colorStateList
        binding.background.setBackgroundColor(color)
    }

    private fun setActionBarAttr(title: String) {
        supportActionBar?.let {
            it.title = title
            it.setDisplayShowTitleEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
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

    private fun setupViewModel() {
        viewModel.apply {
            newSchedule.observe { schedule ->
                binding.schedule = schedule
                setBackgroundColor(schedule.color)
                tasksInfoAdapter.submitList(
                    schedule.tasks.sortedWith(
                        compareBy(
                            { it.finishBy },
                            { it.createAt }
                        )
                    ).toMutableList()
                )
                imagesAdapter.submitList(schedule.images.toMutableList())
                filesAdapter.submitList(schedule.applications.toMutableList())
                setActionBarAttr("${getString(R.string.edit_schedule)} ${schedule.title}")
                colorsAdapter.selectedColor = ColorsAdapter.ColorElement.values().first {
                    it.rawValue == schedule.color
                }
                if (schedule.scheduleId.isEmpty()) {
                    binding.cancel.visibility = View.GONE
                } else {
                    binding.cancel.visibility = View.VISIBLE
                }
                tasksInfoAdapter.notifyDataSetChanged()
                filesAdapter.notifyDataSetChanged()
                val scheduleAlarm = schedule.scheduleTime.toCalendar
                scheduleAlarm[Calendar.SECOND] = 0
                scheduleAlarm[Calendar.MILLISECOND] = 0
                if (scheduleAlarm.timeInMillis > System.currentTimeMillis()) {
                    scheduleAlarm(schedule)
                }
            }
            updateSchedule.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        binding.isEditMode = false
                    }
                    is Resource.Error -> {
                        AlertDialog.Builder(this@ScheduleInfoActivity)
                            .setMessage(R.string.has_error_when_save_schedule)
                    }
                }
            }
            downloadMedia.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        showToast(String.format(getString(R.string.start_download), resource.data))
                    }
                    is Resource.Error -> {
                        showToast(R.string.error_download)
                    }
                }
            }
            toggleTaskLiveData.observe {
                tasksInfoAdapter.submitList(
                    viewModel.newSchedule.value?.tasks?.toMutableList() ?: emptyList()
                )
            }
            isEditModeLiveData.observe { isEditMode ->
                if (isEditMode == true) {
                    binding.schedule = viewModel.newSchedule.value
                    binding.background.setBackgroundColor(
                        Color.parseColor(
                            binding.schedule?.color ?: ColorsAdapter.ColorElement.YELLOW.rawValue
                        )
                    )
                }
                tasksInfoAdapter.isEditMode = isEditMode
                filesAdapter.isEditMode = isEditMode
                binding.isEditMode = isEditMode
            }
        }
    }

    private fun setupBinding() {
        setSupportActionBar(binding.addToolBar)
        val scheduleReceiver: Schedule? = intent.getParcelableExtra(SCHEDULE)
        viewModel.initSchedule(scheduleReceiver?.scheduleId)
        val hexColor = if (scheduleReceiver == null) {
            viewModel.isEditModeLiveData.value = true
            binding.cancel.visibility = View.GONE
            setActionBarAttr(getString(R.string.create_new_schedule))
            colorsAdapter.selectedColor = ColorsAdapter.ColorElement.YELLOW
            ColorsAdapter.ColorElement.YELLOW.rawValue
        } else {
            viewModel.isEditModeLiveData.value = false
            binding.schedule = scheduleReceiver
            binding.cancel.visibility = View.VISIBLE
            setActionBarAttr("${getString(R.string.edit_schedule)} ${scheduleReceiver.title}")
            scheduleReceiver.color
        }
        setBackgroundColor(hexColor)
        binding.apply {
            tabWrap.setOnMenuItemClickListener {
                if (
                    viewModel.newSchedule.value?.scheduleId?.isEmpty() == true
                ) {
                    return@setOnMenuItemClickListener false
                }
                viewModel.isEditModeLiveData.value = false
                when (it.itemId) {
                    R.id.image_choose -> {
                        fileChoose.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            type = "image/*"
                            addCategory(Intent.CATEGORY_OPENABLE)
                        })
                        true
                    }
                    R.id.take_photo -> {
                        takePhotoFromCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                        true
                    }
                    R.id.take_file -> {
                        fileChoose.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            type = "application/*"
                            addCategory(Intent.CATEGORY_OPENABLE)
                        })
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            addTask.setOnClickListener {
                BaseFragment.create(InputTaskDialog()) {
                    putParcelable(TASK, Task().apply {
                        viewModel.newSchedule.value?.let { schedule ->
                            scheduleId = schedule.scheduleId
                            putString(COLOR, schedule.color)
                        }
                    })
                }.show(supportFragmentManager, InputTaskDialog::class.simpleName)
            }
            addFile.setOnClickListener {
                fileChoose.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "application/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                })
            }
            cancel.setOnClickListener {
                viewModel.newSchedule.value?.let { s ->
                    schedule = s
                    binding.background.setBackgroundColor(Color.parseColor(s.color))
                }
                viewModel.isEditModeLiveData.value = false
            }
            save.setOnClickListener {
                if (scheduleReceiver?.title?.isEmpty() == true) {
                    AlertDialog.Builder(this@ScheduleInfoActivity)
                        .setMessage(R.string.title_cannot_be_empty)
                        .show()
                } else {
                    viewModel.newSchedule.value!!.color = colorsAdapter.selectedColor.rawValue
                    viewModel.saveSchedule()
                }
            }
            scheduleTime.setOnClickListener {
                val scheduleCalendar = viewModel.newSchedule.value!!.scheduleTimeCalendar
                DatePickerDialog(
                    this@ScheduleInfoActivity, { _, year, month, dayOfMonth ->
                        scheduleCalendar.set(Calendar.YEAR, year)
                        scheduleCalendar.set(Calendar.MONTH, month)
                        scheduleCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        TimePickerDialog(
                            this@ScheduleInfoActivity, { _, hourOfDay, minute ->
                                scheduleCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                scheduleCalendar.set(Calendar.MINUTE, minute)
                                viewModel.newSchedule.value?.let { s ->
                                    s.scheduleTime = scheduleCalendar.timeInMillis
                                    schedule = s
                                }
                            },
                            scheduleCalendar.get(Calendar.HOUR_OF_DAY),
                            scheduleCalendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    scheduleCalendar.get(Calendar.YEAR),
                    scheduleCalendar.get(Calendar.MONTH),
                    scheduleCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            changeMode.setOnClickListener {
                viewModel.isEditModeLiveData.value = !(viewModel.isEditModeLiveData.value ?: true)
            }
            mutableListOf(first, second, third).setOnClickListener {
                BaseFragment.create(MembersDialog()) {
                    viewModel.newSchedule.value?.let { schedule ->
                        putParcelableArray(MEMBERS, schedule.members.toTypedArray())
                        putString(COLOR, schedule.color)
                    }
                }.show(supportFragmentManager, MembersDialog::class.simpleName)
                viewModel.isEditModeLiveData.value = false
            }
        }
    }

    override fun createUI() {
        recyclerViewSetUp()
        setupViewModel()
        setupBinding()
        noInternetError()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override val viewModel: ScheduleInfoViewModel by viewModels()

    companion object {
        const val TASK: String = "task"
        const val COLOR: String = "color"
        const val MEMBERS: String = "members"
    }
}