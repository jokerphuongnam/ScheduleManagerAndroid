package com.pnam.schedulemanager.ui.scheduleInfo

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ItemColorBinding

class ColorsAdapter(
    private val selectedColorHandle: (ColorElement) -> Unit
) : ListAdapter<ColorsAdapter.ColorElement, ColorsAdapter.ColorViewHolder>(DIFF_CALLBACK) {
    init {
        submitList(ColorElement.values().toMutableList())
    }
    var selectedColor: ColorElement = ColorElement.YELLOW
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder.create(parent, viewType, selectedColorHandle)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        getItem(position).let { color ->
            holder.bind(color, selectedColor.rawValue?.equals(color.rawValue, false) ?: false)
        }
    }

    class ColorViewHolder(
        private val binding: ItemColorBinding,
        private val selectedColorHandle: (ColorElement) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(color: ColorElement, isSelectedColor: Boolean) {
            binding.apply {
                container.setOnClickListener {
                    selectedColorHandle(color)
                }
                isSelected = isSelectedColor
                color.rawValue?.let {
                    circle.setBackgroundColor(Color.parseColor(it))
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                viewType: Int,
                selectedColorHandle: (ColorElement) -> Unit
            ): ColorViewHolder {
                return ColorViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_color,
                        parent,
                        false
                    ),
                    selectedColorHandle
                )
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ColorElement> by lazy {
            object : DiffUtil.ItemCallback<ColorElement>() {
                override fun areItemsTheSame(
                    oldItem: ColorElement,
                    newItem: ColorElement
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: ColorElement,
                    newItem: ColorElement
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    enum class ColorElement(val rawValue: String) {
        YELLOW("#FBD000"),
        PURPLE("#d294dd"),
        LIGHT_CYAN("#9acdd0"),
        PINK("#FF208F"),
        SPORT_GREEN("#10893E"),
        WHITE("#FFFFFF"),
//        NONE(null)
    }
}