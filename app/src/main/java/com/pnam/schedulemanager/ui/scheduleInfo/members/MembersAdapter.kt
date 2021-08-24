package com.pnam.schedulemanager.ui.scheduleInfo.members

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ItemMemberBinding
import com.pnam.schedulemanager.model.database.domain.Member

class MembersAdapter : ListAdapter<Member, MembersAdapter.MemberViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MemberViewHolder(
        private val binding: ItemMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(member: Member) {
            binding.member = member
        }

        companion object {
            fun create(
                parent: ViewGroup,
                viewType: Int,
            ): MemberViewHolder {
                return MemberViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_member,
                        parent,
                        false
                    )
                )
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Member> by lazy {
            object : DiffUtil.ItemCallback<Member>() {
                override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean =
                    oldItem.memberId == newItem.memberId

                override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean =
                    oldItem.fullName.trim().equals(newItem.fullName.trim(), ignoreCase = true)
            }
        }
    }
}