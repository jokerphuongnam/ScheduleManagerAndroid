package com.pnam.schedulemanager.ui.main.schedules

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ItemScheduleBinding
import com.pnam.schedulemanager.model.database.domain.Schedule

class SchedulesAdapter(
    private val itemClick: (Schedule, List<View>) -> Unit,
    private val deleteCallback: (Schedule) -> Unit
) :
    ListAdapter<Schedule, SchedulesAdapter.NoteViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder.create(parent, viewType, itemClick, deleteCallback)

    class NoteViewHolder private constructor(
        private val binding: ItemScheduleBinding,
        private val itemClick: (Schedule, List<View>) -> Unit,
        private val deleteCallback: (Schedule) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(scheduleOptional: Schedule?) {
            binding.schedule = scheduleOptional
            scheduleOptional?.let { note ->
                binding.background.setBackgroundColor(Color.parseColor("#${note.color}"))
                binding.scheduleItem.apply {
                    setOnClickListener {
                        itemClick(
                            note,
                            mutableListOf(
                                binding.scheduleItem,
                                binding.title,
                                binding.description
                            )
                        )
                    }
                    setOnLongClickListener {
                        PopupMenu(context, it).apply {
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.edit -> {
                                        itemClick(
                                            note,
                                            mutableListOf(
                                                binding.title,
                                                binding.description
                                            )
                                        )
                                        true
                                    }
                                    R.id.delete -> {
                                        deleteCallback(note)
                                        true
                                    }
                                    else -> false
                                }
                            }
                            inflate(R.menu.schedule_item_control)
                            show()
                        }
                        true
                    }
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                viewType: Int,
                itemClick: (Schedule, List<View>) -> Unit,
                longClick: (Schedule) -> Unit
            ): NoteViewHolder {
                return NoteViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_schedule,
                        parent,
                        false
                    ),
                    itemClick,
                    longClick
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