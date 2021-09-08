package com.pnam.schedulemanager.ui.scheduleInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ItemFileBinding
import com.pnam.schedulemanager.model.database.domain.Media

class FilesAdapter(
    private val deleteFile: (Media) -> Unit,
    private val downloadFile: (Media) -> Unit
) : ListAdapter<Media, FilesAdapter.FileViewHolder>(
    DIFF_CALLBACK
) {
    var isEditMode: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder =
        FileViewHolder.create(parent, viewType, deleteFile, downloadFile)

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(getItem(position), isEditMode)
    }

    class FileViewHolder private constructor(
        private val binding: ItemFileBinding,
        private val deleteFile: (Media) -> Unit,
        private val downloadFile: (Media) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(file: Media, isEditMode: Boolean) {
            binding.file = file
            binding.isEditMode = isEditMode
            binding.downloadFileBtn.setOnClickListener {
                downloadFile(file)
            }
            binding.deleteFileBtn.setOnClickListener {
                deleteFile(file)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                viewType: Int,
                deleteFile: (Media) -> Unit,
                downloadFile: (Media) -> Unit
            ): FileViewHolder = FileViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_file,
                    parent,
                    false
                ),
                deleteFile,
                downloadFile
            )
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Media> by lazy {
            object : DiffUtil.ItemCallback<Media>() {
                override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
                    return oldItem.mediaId == newItem.mediaId
                }

                override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
                    return oldItem.equals(newItem)
                }
            }
        }
    }
}