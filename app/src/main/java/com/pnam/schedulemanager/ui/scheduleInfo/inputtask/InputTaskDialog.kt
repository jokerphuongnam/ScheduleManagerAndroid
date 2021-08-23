package com.pnam.schedulemanager.ui.scheduleInfo.inputtask

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.DialogInputTaskBinding
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.ui.base.BaseDialogFragment
import com.pnam.schedulemanager.ui.base.showToastActivity
import com.pnam.schedulemanager.ui.scheduleInfo.ScheduleInfoActivity.Companion.COLOR
import com.pnam.schedulemanager.ui.scheduleInfo.ScheduleInfoActivity.Companion.TASK
import com.pnam.schedulemanager.ui.scheduleInfo.ScheduleInfoViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class InputTaskDialog : BaseDialogFragment<DialogInputTaskBinding, InputTaskViewModel>(
    R.layout.dialog_input_task
) {
    private val saveTaskObserver: Observer<Resource<InputTaskViewModel.SaveTaskType>> by lazy {
        Observer { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    try {
                        val successMessage = when (resource.data) {
                            InputTaskViewModel.SaveTaskType.INSERT -> {
                                R.string.create_task_success
                            }
                            InputTaskViewModel.SaveTaskType.UPDATE -> {
                                R.string.update_task_success
                            }
                        }
                        showToastActivity(successMessage)
                        activityViewModel.getScheduleInfo()
                        viewModel.saveTaskLiveData.removeObserver(saveTaskObserver)
                        dismiss()
                    } catch (e: Exception) {

                    }
                }
                is Resource.Error -> {
                }
            }
        }
    }

    override fun createUI() {
        arguments?.let { bundle ->
            bundle.getParcelable<Task>(TASK)?.let { task ->
                binding.task = task
            }
            bundle.getString(COLOR)?.let { hexColor ->
                val color = Color.parseColor(hexColor)
                val colorStateList = ColorStateList.valueOf(color)
                binding.cancel.setTextColor(color)
                binding.cancel.rippleColor = colorStateList
                binding.save.setTextColor(color)
                binding.save.rippleColor = colorStateList
            }
            binding.apply {
                cancel.setOnClickListener {
                    dismiss()
                }
                save.setOnClickListener {
                    binding.task?.let { task ->
                        viewModel.saveTask(task)
                    }
                }
            }
        }
        viewModel.saveTaskLiveData.observeForever(saveTaskObserver)

    }

    override val viewModel: InputTaskViewModel by viewModels()
    private val activityViewModel: ScheduleInfoViewModel by activityViewModels()
}