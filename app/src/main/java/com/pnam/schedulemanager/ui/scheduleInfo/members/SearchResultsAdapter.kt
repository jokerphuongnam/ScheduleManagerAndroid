package com.pnam.schedulemanager.ui.scheduleInfo.members

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ItemSearchResultBinding
import com.pnam.schedulemanager.model.database.domain.Search

class SearchResultsAdapter(
    private val selectedItem: (Search) -> Unit
) : ListAdapter<Search, SearchResultsAdapter.SearchResultViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder.create(parent, viewType, selectedItem)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchResultViewHolder(
        private val binding: ItemSearchResultBinding,
        private val selectedItem: (Search) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(search: Search) {
            binding.search = search
            binding.container.setOnClickListener {
                selectedItem(search)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                viewType: Int,
                selectedItem: (Search) -> Unit
            ): SearchResultViewHolder {
                return SearchResultViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_search_result,
                        parent,
                        false
                    ),
                    selectedItem
                )
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Search> by lazy {
            object : DiffUtil.ItemCallback<Search>() {
                override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean =
                    oldItem.word.trim().equals(newItem.word.trim(), ignoreCase = true)
            }
        }
    }
}