package com.pnam.schedulemanager.ui.scheduleInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ItemImageBinding
import com.pnam.schedulemanager.model.database.domain.Media

class ImagesAdapter(
    private val deleteImage: (Media) -> Unit
) : ListAdapter<Media, ImagesAdapter.ImageViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder.create(parent, viewType, deleteImage)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImageViewHolder private constructor(
        private val binding: ItemImageBinding,
        private val deleteImage: (Media) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Media) {
            binding.image = image
            binding.container.setOnLongClickListener { view ->
                PopupMenu(view.context, view).let { popupMenu ->
                    popupMenu.inflate(R.menu.image_menu)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.delete -> {
                                deleteImage(image)
                            }
                        }
                        true
                    }
                }
                true
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                viewType: Int,
                deleteImage: (Media) -> Unit
            ): ImageViewHolder = ImageViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_image,
                    parent,
                    false
                ),
                deleteImage
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