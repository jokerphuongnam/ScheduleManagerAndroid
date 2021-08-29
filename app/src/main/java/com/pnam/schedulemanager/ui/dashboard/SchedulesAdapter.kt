package com.pnam.schedulemanager.ui.dashboard

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ItemScheduleBinding
import com.pnam.schedulemanager.model.database.domain.Schedule

class SchedulesAdapter(
    private val itemClick: (Schedule, List<View>) -> Unit
) : ListAdapter<Schedule, SchedulesAdapter.NoteViewHolder>(
    DIFF_CALLBACK
) {
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder.create(parent, viewType, itemClick)

    class NoteViewHolder private constructor(
        private val binding: ItemScheduleBinding,
        private val itemClick: (Schedule, List<View>) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(scheduleOptional: Schedule?) {
            binding.schedule = scheduleOptional
            scheduleOptional?.let { schedule ->
                binding.background.setBackgroundColor(Color.parseColor(schedule.color))
                binding.scheduleItem.apply {
                    setOnClickListener {
                        itemClick(
                            schedule,
                            mutableListOf(
                                binding.scheduleItem,
                                binding.title,
                                binding.description,
                                binding.scheduleTime
                            )
                        )
                    }
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                viewType: Int,
                itemClick: (Schedule, List<View>) -> Unit
            ): NoteViewHolder {
                return NoteViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_schedule,
                        parent,
                        false
                    ),
                    itemClick
                )
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Schedule> by lazy {
            object : DiffUtil.ItemCallback<Schedule>() {
                override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                    return oldItem.scheduleId == newItem.scheduleId
                }

                override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}