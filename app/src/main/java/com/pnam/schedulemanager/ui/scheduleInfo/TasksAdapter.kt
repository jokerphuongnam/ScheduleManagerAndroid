package com.pnam.schedulemanager.ui.scheduleInfo

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ItemTaskBinding
import com.pnam.schedulemanager.model.database.domain.Task


class TasksAdapter(
    private val deleteCallBack: (Task) -> Unit,
    private val toggleCallBack: (Boolean, Task) -> Unit,
    private val editTaskCallback: (Task) -> Unit
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(
    DIFF_CALLBACK
) {
    var isEditMode: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder.create(parent, viewType, deleteCallBack, toggleCallBack, editTaskCallback)

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position), isEditMode)
    }

    class TaskViewHolder private constructor(
        private val binding: ItemTaskBinding,
        private val deleteCallBack: (Task) -> Unit,
        private val toggleCallBack: (Boolean, Task) -> Unit,
        private val editTaskCallback: (Task) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, isEditMode: Boolean) {
            binding.task = task
            binding.isEditMode = isEditMode
            binding.apply {
                deleteTaskBtn.setOnClickListener {
                    deleteCallBack.invoke(task)
                }
                isFinish.setOnClickListener {
                    toggleCallBack(isFinish.isChecked, task)
                }
                if (isFinish.isChecked) {
                    describe.paintFlags = describe.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    describe.paintFlags = describe.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
                editTask.setOnClickListener {
                    editTaskCallback(task)
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                viewType: Int,
                deleteCallBack: (Task) -> Unit,
                toggleCallBack: (Boolean, Task) -> Unit,
                editTaskCallback: (Task) -> Unit
            ): TaskViewHolder = TaskViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_task,
                    parent,
                    false
                ),
                deleteCallBack,
                toggleCallBack,
                editTaskCallback
            )
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Task> by lazy {
            object : DiffUtil.ItemCallback<Task>() {
                override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                    oldItem.taskId == newItem.taskId

                override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                    oldItem.detail.trim().equals(newItem.detail.trim(), ignoreCase = true)
            }
        }
    }
}