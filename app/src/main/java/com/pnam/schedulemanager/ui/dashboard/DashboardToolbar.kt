package com.pnam.schedulemanager.ui.dashboard

import android.view.View
import androidx.core.view.isVisible
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.binding.loading
import com.pnam.schedulemanager.databinding.LayoutAvatarDashboardBinding
import com.pnam.schedulemanager.model.database.domain.User

class DashboardToolbar {
    private var _binding: LayoutAvatarDashboardBinding? = null
    val binding: LayoutAvatarDashboardBinding get() = _binding!!

    internal fun setBinding(view: View) {
        _binding = LayoutAvatarDashboardBinding.bind(view)
    }

    internal fun onCreate() {
        loading()
    }

    var avatarClick: View.OnClickListener?
        get() = null
        set(value) {
            binding.avatar.apply {
                setOnClickListener(value)
            }
        }

    internal fun setUser(user: User?) {
        binding.user = user
    }

    internal fun cancelAnimation() {
        _binding?.avatar?.animate()?.cancel()
    }

    internal fun loading() {
        _binding?.avatar?.apply {
            setImageResource(R.drawable.ic_loading)
            loading()
        }
    }

    internal fun emptyAvatar() {
        _binding?.avatar?.apply {
            setImageResource(R.drawable.ic_empty)
            animate().cancel()
        }
    }

    internal fun setVisible(isVisible: Boolean) {
        _binding?.avatar?.let {
            it.isVisible = isVisible
        }
    }
}