package com.pnam.schedulemanager.ui.scheduleInfo

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityScheduleInfoBinding
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.base.BaseFragment
import com.pnam.schedulemanager.ui.base.uriToBitmap
import com.pnam.schedulemanager.ui.dashboard.DashboardActivity.Companion.SCHEDULE
import com.pnam.schedulemanager.ui.scheduleInfo.inputtask.InputTaskDialog
import com.pnam.schedulemanager.ui.scheduleInfo.members.MembersDialog
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScheduleInfoActivity : BaseActivity<ActivityScheduleInfoBinding, ScheduleInfoViewModel>(
    R.layout.activity_schedule_info
) {
    private val imageChoose: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    viewModel.insertMedia(uri)
                }
            }
        }

    private val takePhotoFromCamera: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                (result.data?.extras?.get("data") as Bitmap).apply {
                    uriToBitmap(this)?.apply {
                        viewModel.insertMedia(this)
                    }
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
        ImagesAdapter { size ->
            if (size == 0) {
                binding.images.visibility = View.GONE
            } else {
                binding.images.visibility = View.VISIBLE
            }
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
                layoutManager =
                    StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            }
            colors.apply {
                adapter = colorsAdapter
                layoutManager = LinearLayoutManager(
                    this@ScheduleInfoActivity,
                    RecyclerView.HORIZONTAL,
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

    private fun setupBinding() {
        setSupportActionBar(binding.addToolBar)
        val scheduleReceiver: Schedule? = intent.getParcelableExtra(SCHEDULE)
        viewModel.initSchedule(scheduleReceiver?.scheduleId)
        val hexColor = if (scheduleReceiver == null) {
            binding.isEditMode = true
            binding.cancel.visibility = View.GONE
            setActionBarAttr(getString(R.string.create_new_schedule))
            colorsAdapter.selectedColor = ColorsAdapter.ColorElement.YELLOW
            ColorsAdapter.ColorElement.YELLOW.rawValue
        } else {
            binding.isEditMode = false
            binding.schedule = scheduleReceiver
            binding.cancel.visibility = View.VISIBLE
            setActionBarAttr("${getString(R.string.edit_schedule)} ${scheduleReceiver.title}")
            scheduleReceiver.color
        }
        setBackgroundColor(hexColor)
        binding.apply {
            tabWrap.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.image_choose -> {
                        imageChoose.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            type = "image/*"
                            addCategory(Intent.CATEGORY_OPENABLE)
                        })
                        true
                    }
                    R.id.take_photo -> {
                        takePhotoFromCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
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
            cancel.setOnClickListener {
                viewModel.newSchedule.value?.let { s ->
                    schedule = s
                    isEditMode = false
                    binding.background.setBackgroundColor(Color.parseColor(s.color))
                }
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
                if (isEditMode == true) {
                    binding.schedule = viewModel.newSchedule.value
                    binding.background.setBackgroundColor(
                        Color.parseColor(
                            binding.schedule?.color ?: ColorsAdapter.ColorElement.YELLOW.rawValue
                        )
                    )
                }
                isEditMode = !(isEditMode ?: false)
                tasksInfoAdapter.isEditMode = isEditMode ?: false
            }
            mutableListOf(first, second, third).setOnClickListener {
                BaseFragment.create(MembersDialog()) {
                    viewModel.newSchedule.value?.let { schedule ->
                        putParcelableArray(MEMBERS, schedule.members.toTypedArray())
                        putString(COLOR, schedule.color)
                    }
                }.show(supportFragmentManager, InputTaskDialog::class.simpleName)
            }
        }
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
                    )
                )
                setActionBarAttr("${getString(R.string.edit_schedule)} ${schedule.title}")
                imagesAdapter.addUrl(schedule.images.map { media -> media.mediaUrl })
                colorsAdapter.selectedColor = ColorsAdapter.ColorElement.values().first {
                    it.rawValue == schedule.color
                }
                if (schedule.scheduleId.isEmpty()) {
                    binding.cancel.visibility = View.GONE
                } else {
                    binding.cancel.visibility = View.VISIBLE
                }
                tasksInfoAdapter.notifyDataSetChanged()
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
            toggleTaskObserver.observe {
                tasksInfoAdapter.submitList(
                    viewModel.newSchedule.value?.tasks?.toMutableList() ?: emptyList()
                )
            }
        }
    }

    override fun createUI() {
        setupBinding()
        setupViewModel()
        noInternetError()
        recyclerViewSetUp()
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