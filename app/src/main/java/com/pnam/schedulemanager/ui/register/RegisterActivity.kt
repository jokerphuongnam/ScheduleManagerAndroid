package com.pnam.schedulemanager.ui.register

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityRegisterBinding
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.base.uriToBitmap
import com.pnam.schedulemanager.ui.login.LoginActivity
import com.pnam.schedulemanager.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RegisterActivity :
    BaseActivity<ActivityRegisterBinding, RegisterViewModel>(R.layout.activity_register) {

    private val imageChoose: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    viewModel.avatar = uri
                    binding.image.setImageURI(uri)
                    binding.imageLayout.displayedChild = 1
                }
            }
        }

    private val takePhotoFromCamera: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                (result.data?.extras?.get("data") as Bitmap).apply {
                    binding.image.setImageBitmap(this)
                    binding.imageLayout.displayedChild = 1
                    viewModel.avatar = uriToBitmap(this)
                }
            }
        }

    private lateinit var actionBar: ActionBar

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * when click ok button of date picker dialog will set time for birthday of user
     * */
    private val datePickerCallBack: DatePickerDialog.OnDateSetListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.currentUser.value!!.birthday = calendar.timeInMillis
            viewModel.currentUser.postValue(viewModel.currentUser.value!!)
        }
    }

    private val datePicker: DatePickerDialog
        get() {
            val calendar: Calendar = viewModel.currentUser.value!!.birthdayCalendar
            return DatePickerDialog(
                this,
                datePickerCallBack,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            )
        }

    /**
     * check regex for user
     * if satisfy will to catch
     * */
    private val registerAction: View.OnClickListener by lazy {
        View.OnClickListener {
            binding.apply {
                var isSuccess = true
                if (viewModel.loginInfo == null) {
                    try {
                        password.error =
                            getString(password.editText!!.text.toString().passwordRegex())
                        email.error = getString(email.editText?.text.toString().usernameRegex())
                        isSuccess = false
                    } catch (e: Exception) {
                        if (!password.editText?.text.toString()
                                .equals(repeatPassword.editText?.text.toString())
                        ) {
                            isSuccess = false
                            repeatPassword.error =
                                getString(R.string.repeat_password_other_new_password)
                        }
                    }
                }

                try {
                    firstName.error = getString(user!!.firstName.nameRegex())
                    lastName.error = getString(user!!.lastName.nameRegex())
                    errorBirthday.text = getString(user!!.birthday.birthdayRegex())
                    errorBirthday.isVisible = true
                    isSuccess = false
                } catch (lastNameError: Exception) {
                    if (isSuccess) {
                        if (viewModel.loginInfo == null) {
                            viewModel.registerWithEmailPass(
                                email.editText!!.text.toString(),
                                password.editText!!.text.toString()
                            )
                        } else {
                            viewModel.registerWithLoginId()
                        }
                    }
                }
            }
        }
    }

    private val chooseCalendar: View.OnClickListener by lazy {
        View.OnClickListener {
            datePicker.show()
        }
    }

    private fun setupSignIn() {
        intent.getStringExtra(LoginActivity.LOGIN_ID)?.let { loginId ->
            binding.apply {
                email.isVisible = false
                password.isVisible = false
                repeatPassword.isVisible = false
                viewModel.loginInfo =
                    (loginId to intent.getParcelableExtra(LoginActivity.LOGIN_TYPE)!!)
            }
        }
    }

    override fun createUI() {
        setUpToolbar()
        setupSignIn()
        binding.apply {
            register.setOnClickListener(registerAction)
            calendarChoose.setOnClickListener(chooseCalendar)
            viewModel.currentUser.observe {
                user = it
            }
        }
        viewModel.registerLiveData.observe { resource ->
            binding.apply {
                when (resource) {
                    is Resource.Loading -> {
                        resetError(
                            email,
                            password,
                            repeatPassword,
                            firstName,
                            lastName
                        )
                    }
                    is Resource.Success -> {
                        finish()
                    }
                    is Resource.Error -> {
                        email.error = getString(R.string.the_email_same)
                    }
                }
            }
        }
        binding.apply {
            addImage.setOnClickListener {
                imageChoose.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "image/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                })
            }
            openCamera.setOnClickListener {
                takePhotoFromCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            }
            deleteImage.setOnClickListener {
                imageLayout.displayedChild = 0
                image.setImageBitmap(null)
                viewModel.avatar = null
            }
        }
    }

    private fun resetError(vararg textInputLayouts: TextInputLayout) {
        textInputLayouts.forEach { textInputLayout ->
            textInputLayout.error = null
        }
    }

    /**
     * this event will enable the back
     * function to the button on press
     * */
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

    override val viewModel: RegisterViewModel by viewModels()
}