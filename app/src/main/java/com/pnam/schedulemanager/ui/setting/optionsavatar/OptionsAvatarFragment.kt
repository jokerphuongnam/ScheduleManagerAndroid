package com.pnam.schedulemanager.ui.setting.optionsavatar

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.FragmentOptionsAvatarBinding
import com.pnam.schedulemanager.ui.base.BaseBottomSheetDialogFragment
import com.pnam.schedulemanager.ui.base.showToastActivity
import com.pnam.schedulemanager.ui.setting.SettingActivity
import com.pnam.schedulemanager.ui.setting.SettingViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class OptionsAvatarFragment :
    BaseBottomSheetDialogFragment<FragmentOptionsAvatarBinding, OptionsAvatarViewModel>(
        R.layout.fragment_options_avatar
    ) {
    override val viewModel: OptionsAvatarViewModel by viewModels()
    private val activityViewModel: SettingViewModel by activityViewModels()

    private fun setupData() {
        binding.hasAvatar = arguments?.getString(SettingActivity.AVATAR_URL) != null
    }

    private fun setupViewModel() {
        viewModel.apply {
            deleteAvatarLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        showToastActivity(R.string.delete_avatar_success)
                        dismiss()
                    }
                    is Resource.Error -> {
                        dismiss()
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            when (val activity = requireActivity()) {
                is SettingActivity -> {
                    chooseGallery.setOnClickListener {
                        activity.imageChoose.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            type = "image/*"
                            addCategory(Intent.CATEGORY_OPENABLE)
                        })
                        dismiss()
                    }
                    openCamera.setOnClickListener {
                        activity.takePhotoFromCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                        dismiss()
                    }
                }
            }
            delete.setOnClickListener {
                when (val resource = activityViewModel.userLiveData.value) {
                    is Resource.Success -> {
                        resource.data.let { user ->
                            viewModel.deleteAvatar(user.userId)
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityViewModel.getUser()
    }

    override fun createUI() {
        setupData()
        setupViewModel()
        setupAction()
    }
}