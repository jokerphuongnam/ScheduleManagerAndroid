package com.pnam.schedulemanager.ui.setting.profile

import android.content.Intent
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.DialogProfileBinding
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.ui.base.BaseDialogFragment
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.ui.base.putParcelableExtra
import com.pnam.schedulemanager.ui.base.slideHActivity
import com.pnam.schedulemanager.ui.editprofile.EditProfileActivity
import com.pnam.schedulemanager.ui.setting.SettingActivity.Companion.USER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProfileDialog : BaseDialogFragment<DialogProfileBinding, BaseViewModel>(
    R.layout.dialog_profile
) {
    private fun setupSize() {
        dialog?.window?.let { window ->
            resources.displayMetrics.let { metrics ->
                val width = (metrics.widthPixels * 0.80).toInt()
                val height = (metrics.heightPixels * 0.70).toInt()
                window.setLayout(width, height)
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            inflateMenu(R.menu.show_profile_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit -> {
                        arguments?.getParcelable<User>(USER)?.let { user ->
                            requireActivity().slideHActivity(
                                Intent(
                                    requireActivity(),
                                    EditProfileActivity::class.java
                                ).apply {
                                    putParcelableExtra(USER, user)
                                }
                            )
                        }
                        dismiss()
                    }
                    R.id.close -> {
                        dismiss()
                    }
                }
                true
            }
        }
    }

    override fun createUI() {
        setupSize()
        arguments?.getParcelable<User>(USER)?.let { user ->
            binding.user = user
        }
        setupToolbar()
    }

    @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
    override val viewModel by lazy { TODO() }
}