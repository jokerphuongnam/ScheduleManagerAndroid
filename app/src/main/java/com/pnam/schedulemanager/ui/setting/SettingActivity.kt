package com.pnam.schedulemanager.ui.setting

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivitySettingBinding
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.base.BaseFragment
import com.pnam.schedulemanager.ui.changepassword.ChangePasswordActivity
import com.pnam.schedulemanager.ui.dashboard.DashboardActivity
import com.pnam.schedulemanager.ui.login.LoginActivity
import com.pnam.schedulemanager.ui.setting.optionsavatar.OptionsAvatarFragment
import com.pnam.schedulemanager.ui.setting.reviewavatar.ReviewAvatarFragment
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SettingActivity :
    BaseActivity<ActivitySettingBinding, SettingViewModel>(R.layout.activity_setting) {

    internal val imageChoose: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                MediaStore.Images.Media.getBitmap(contentResolver, uri).let { bitmap ->
                    openReviewAvatar(bitmap)
                }
            }
        }
    }

    internal val takePhotoFromCamera: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                (result.data?.extras?.get("data") as Bitmap).apply {
                    openReviewAvatar(this)
                }
            }
        }

    private fun openReviewAvatar(bitmap: Bitmap) {
        BaseFragment.create(ReviewAvatarFragment()) {
            when (val user = viewModel.userLiveData.value) {
                is Resource.Success -> {
                    putString(USER_ID, user.data.userId)
                    putParcelable(AVATAR_BITMAP, bitmap)
                    putString(AVATAR_URL, user.data.avatar)
                }
                else -> {

                }
            }
        }.show(
            supportFragmentManager.beginTransaction(),
            ReviewAvatarFragment::class.java.simpleName
        )
    }

    private fun setupLiveData() {
        viewModel.apply {
            logoutLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showProgressDialog()
                    }
                    is Resource.Success -> {
                        val intent: Intent = Intent(
                            this@SettingActivity,
                            LoginActivity::class.java
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        dismissProgressDialog()
                        startActivity(intent)
                    }
                    is Resource.Error -> {
                    }
                }
            }
            userLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.changeAvatar.isEnabled = false
                        binding.avatar.apply {
                            setImageResource(R.drawable.ic_loading)
                        }
                    }
                    is Resource.Success -> {
                        binding.changeAvatar.isEnabled = true
                        binding.user = resource.data
                    }
                    is Resource.Error -> {
                        binding.changeAvatar.isEnabled = true
                        binding.avatar.apply {
                            setImageResource(R.drawable.ic_empty)
                            animate().cancel()
                        }
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            changeAvatar.setOnClickListener {
                when (val resource = viewModel.userLiveData.value) {
                    is Resource.Success -> {
                        BaseFragment.create(
                            OptionsAvatarFragment()
                        ) {
                            putString(AVATAR_URL, resource.data.avatar)
                        }.show(
                            supportFragmentManager,
                            OptionsAvatarFragment::javaClass.name
                        )
                    }
                    else -> {

                    }
                }
            }
            showProfile.setOnClickListener {

            }
            changePassword.setOnClickListener {
                slideHActivity(Intent(this@SettingActivity, ChangePasswordActivity::class.java))
            }
            logout.setOnClickListener {
                viewModel.logout()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun setupAppbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun createUI() {
        setupAction()
        setupLiveData()
        setupAppbar()
        intent.getParcelableExtra<User>(DashboardActivity.USER)?.let { user ->
            viewModel.userLiveData.value = Resource.Success(user)
        }
    }

    override val viewModel: SettingViewModel by viewModels()

    companion object {
        const val AVATAR_BITMAP = "image_bitmap"
        const val AVATAR_URL = "image_url"
        const val USER_ID = "user_id"
    }
}