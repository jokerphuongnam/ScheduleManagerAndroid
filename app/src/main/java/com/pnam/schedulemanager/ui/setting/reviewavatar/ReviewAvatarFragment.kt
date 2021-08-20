package com.pnam.schedulemanager.ui.setting.reviewavatar

import android.graphics.Bitmap
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.FragmentReviewAvatarBinding
import com.pnam.schedulemanager.ui.base.BaseBottomSheetDialogFragment
import com.pnam.schedulemanager.ui.base.BaseFragment
import com.pnam.schedulemanager.ui.base.uriToBitmap
import com.pnam.schedulemanager.ui.setting.SettingActivity
import com.pnam.schedulemanager.ui.setting.SettingViewModel
import com.pnam.schedulemanager.ui.setting.optionsavatar.OptionsAvatarFragment
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ReviewAvatarFragment :
    BaseBottomSheetDialogFragment<FragmentReviewAvatarBinding, ReviewAvatarViewModel>(
        R.layout.fragment_review_avatar
    ) {
    private val activityViewModel: SettingViewModel by activityViewModels()
    override val viewModel: ReviewAvatarViewModel by viewModels()

    private fun loadAvatar(bitmap: Bitmap?) {
        binding.avatar.load(bitmap) {
            transformations(CircleCropTransformation())
            crossfade(true)
            placeholder(R.drawable.ic_error)
        }
    }

    private fun setUpViewModel() {
        viewModel.apply {
            uploadAvatarLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        showToast(R.string.upload_avatar_success)
                        dismiss()
                    }
                    is Resource.Error -> {
                    }
                }
            }
        }
    }

    private fun setAction() {
        binding.apply {
            avatar.setOnClickListener {
                BaseFragment.create(
                    OptionsAvatarFragment()
                ) {
                    putString(SettingActivity.AVATAR_URL, getString(SettingActivity.AVATAR_URL))
                }.show(
                    requireActivity().supportFragmentManager,
                    OptionsAvatarFragment::javaClass.name
                )
            }
            uploadAvatar.setOnClickListener {
                viewModel.uploadAvatar()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityViewModel.getUser()
    }

    override fun createUI() {
        setUpViewModel()
        setAction()
    }

    override fun onResume() {
        super.onResume()
        arguments?.apply {
            getString(SettingActivity.USER_ID)?.let { userId ->
                viewModel.userId = userId
            }
            (getParcelable(SettingActivity.AVATAR_BITMAP) as Bitmap?)?.let { avatarBitmap ->
                loadAvatar(avatarBitmap)
                requireActivity().uriToBitmap(avatarBitmap)?.let { avatarUri ->
                    viewModel.avatar = avatarUri
                }
            }
        }
    }
}