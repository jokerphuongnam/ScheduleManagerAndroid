package com.pnam.schedulemanager.ui.scheduleInfo.members

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ItemSearchBinding
import com.pnam.schedulemanager.model.database.domain.Search

class SearchesAdapter(
    private val deleteItem: (Search) -> Unit,
    private val searchItem: (Search) -> Unit
) : ListAdapter<Search, SearchesAdapter.SearchViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.create(parent, viewType, deleteItem, searchItem)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchViewHolder(
        private val binding: ItemSearchBinding,
        private val deleteItem: (Search) -> Unit,
        private val searchItem: (Search) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(search: Search) {
            binding.search = search
            binding.container.setOnClickListener {
                searchItem(search)
            }
            binding.deleteSearch.setOnClickListener {
                AlertDialog.Builder(it.context)
                    .setMessage("${it.context.getString(R.string.delete)} ${search.word}")
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(R.string.ok) { dialog, _ ->
                        deleteItem(search)
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                viewType: Int,
                deleteItem: (Search) -> Unit,
                searchItem: (Search) -> Unit
            ): SearchViewHolder {
                return SearchViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_search,
                        parent,
                        false
                    ),
                    deleteItem,
                    searchItem
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