package com.pnam.schedulemanager.ui.scheduleInfo

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ItemImageBinding

class ImagesAdapter(
    private val changeSizeData: (Int) -> Unit
) : ListAdapter<ImagesAdapter.ImageType<*>, ImagesAdapter.ImageViewHolder>(DIFF_CALLBACK) {
    private var _images: MutableList<ImageType<*>> = mutableListOf()

    init {
        submitList(_images)
    }

    override fun submitList(list: MutableList<ImageType<*>>?) {
        _images = list ?: mutableListOf()
        super.submitList(_images.toMutableList())
        changeSizeData(_images.size)
    }

    fun addUrl(url: String) {
        _images.add(ImageType.ImageUrl(url))
        submitList(_images)
    }

    fun addUrl(url: List<String>?) {
        (url ?: mutableListOf()).map {
            ImageType.ImageUrl(it)
        }.forEach {
            _images.add(it)
        }
        submitList(_images)
    }

    fun addBitmap(bitmap: Bitmap) {
        _images.add(ImageType.ImageBitMap(bitmap))
        submitList(_images)
    }

    fun addBitmap(bitmap: List<Bitmap>?) {
        (bitmap ?: mutableListOf()).map { ImageType.ImageBitMap(it) }.forEach {
            _images.add(it)
        }
        submitList(_images)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder.create(parent, viewType)

    class ImageViewHolder private constructor(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: ImageType<*>) {
            binding.image = image
        }

        companion object {
            fun create(parent: ViewGroup, viewType: Int): ImageViewHolder = ImageViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_image,
                    parent,
                    false
                )
            )
        }
    }

    sealed class ImageType<T>(open val image: T) {
        class ImageUrl(override val image: String) : ImageType<String>(image)
        class ImageBitMap(override val image: Bitmap) : ImageType<Bitmap>(image)
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ImageType<*>> by lazy {
            object : DiffUtil.ItemCallback<ImageType<*>>() {
                override fun areItemsTheSame(
                    oldItem: ImageType<*>,
                    newItem: ImageType<*>
                ): Boolean {
                    return oldItem.image == newItem.image
                }

                override fun areContentsTheSame(
                    oldItem: ImageType<*>,
                    newItem: ImageType<*>
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}