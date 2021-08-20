package com.pnam.schedulemanager.ui.changepassword

import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import com.pnam.schedulemanager.throwable.NoErrorException
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityChangePasswordBinding
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.utils.Resource
import com.pnam.schedulemanager.utils.passwordRegex
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity :
    BaseActivity<ActivityChangePasswordBinding, ChangePasswordViewModel>(R.layout.activity_change_password) {

    private lateinit var actionBar: ActionBar

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbar)
        actionBar = supportActionBar!!
    }

    /**
     * after click change password will check repeat password with new password will set error for repeat password
     * if repeat password do not same new password
     * else check password in net work
     * */
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
                        /**
                         * after change password success will back to main activity and show toast
                         * */
                        is Resource.Success -> {
                            finish()
                        }
                        /**
                         * here is unknown error
                         * */
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

    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
        showToast(getString(R.string.change_password_success))
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