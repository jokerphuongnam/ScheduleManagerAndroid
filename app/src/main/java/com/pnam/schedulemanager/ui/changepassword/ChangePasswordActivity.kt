package com.pnam.schedulemanager.ui.changepassword

import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityChangePasswordBinding
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding, ChangePasswordViewModel>(
    R.layout.activity_change_password
) {
    private lateinit var actionBar: ActionBar

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbar)
        actionBar = supportActionBar!!
    }

    private fun setUpEvent() {
        binding.apply {
            changePasswordBtn.setOnClickListener {
                if (!repeatPassword.editText!!.text.toString()
                        .equals(newPassword.editText!!.text.toString())
                ) {
                    repeatPassword.error = getString(R.string.repeat_password_other_new_password)
                } else {
                    viewModel.changePassword(
                        oldPassword.editText!!.text.toString(),
                        newPassword.editText!!.text.toString()
                    )
                }
            }
        }
    }

    private fun setUpObserver() {
        viewModel.apply {
            changePasswordLiveData.observe { resource ->
                binding.apply {
                    when (resource) {
                        is Resource.Loading -> {
                            showProgressDialog()
                            changePasswordError.isVisible = false
                            changePasswordError.text = ""
                        }
                        is Resource.Success -> {
                            finish()
                        }
                        is Resource.Error -> {
                            if(resource.message == "NotFound"){
                                changePasswordError.isVisible = true
                                changePasswordError.setText(R.string.wrong_password)
                            }
                            dismissProgressDialog()
                        }
                    }
                }
            }
            internetError.observe {
                binding.changePasswordError.apply {
                    visibility = View.VISIBLE
                    setText(R.string.no_internet)
                }
            }
        }
    }

    override fun createUI() {
        setUpActionBar()
        actionBar.setDisplayHomeAsUpEnabled(true)
        setUpEvent()
        setUpObserver()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override val viewModel: ChangePasswordViewModel by viewModels()
}