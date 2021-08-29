package com.pnam.schedulemanager.ui.editprofile

import android.app.DatePickerDialog
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityEditProfileBinding
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.throwable.NoErrorException
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.base.text
import com.pnam.schedulemanager.ui.setting.SettingActivity
import com.pnam.schedulemanager.utils.Resource
import com.pnam.schedulemanager.utils.nameRegex
import com.pnam.schedulemanager.utils.toCalendar
import com.pnam.schedulemanager.utils.toDateTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class EditProfileActivity : BaseActivity<ActivityEditProfileBinding, EditProfileViewModel>(
    R.layout.activity_edit_profile
) {
    private val datePickerCallBack: DatePickerDialog.OnDateSetListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            binding.apply {
                calendar.timeInMillis.let { birthdayLong ->
                    user?.birthday = birthdayLong
                    birthday.setText(birthdayLong.toDateTimeString("dd/MM/yyyy"))
                }
            }
        }
    }

    private val datePicker: DatePickerDialog
        get() {
            val calendar: Calendar = binding.user!!.birthday.toCalendar
            return DatePickerDialog(
                this,
                datePickerCallBack,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            )
        }

    private fun clearError(vararg textInputLayout: TextInputLayout) {
        textInputLayout.forEach {
            it.error = null
        }
    }

    private fun setUpAction() {
        binding.apply {
            calendarChoose.setOnClickListener {
                datePicker.show()
            }
            editProfile.setOnClickListener {
                clearError(
                    firstName,
                    lastName
                )
                errorBirthday.isVisible = false
                var isSuccess = true
                try {
                    firstName.error = getString(firstName.text.nameRegex())
                    isSuccess = false
                } catch (e: NoErrorException) {
                }
                try {
                    lastName.error = getString(lastName.text.nameRegex())
                    isSuccess = false
                } catch (e: NoErrorException) {
                }
                user?.takeIf { it.birthday >= System.currentTimeMillis() }?.let {
                    isSuccess = false
                    birthday.error = getString(R.string.birthday_need_before_present_time)
                }
                if (isSuccess) {
                    user?.let {
                        viewModel.editProfile(it)
                    }
                }
            }
        }
    }

    private fun setUpViewModel() {
        viewModel.apply {
            editProfileLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showProgressDialog()
                        binding.editProfile.isVisible = false
                    }
                    is Resource.Success -> {
                        dismissProgressDialog()
                        finish()
                    }
                    is Resource.Error -> {
                        dismissProgressDialog()
                        binding.editProfile.isVisible = true
                        binding.errorEditProfile.setText(R.string.cannot_edit_profile)
                    }
                }
            }
        }
    }

    private fun setupData() {
        intent.getParcelableExtra<User>(SettingActivity.USER)?.let { user ->
            binding.user = user
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    override fun createUI() {
        setupData()
        setupToolbar()
        setUpAction()
        setUpViewModel()
    }

    override val viewModel: EditProfileViewModel by viewModels()
}