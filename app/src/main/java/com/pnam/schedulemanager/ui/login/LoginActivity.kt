package com.pnam.schedulemanager.ui.login

import android.content.Intent
import android.os.Parcelable
import android.view.View
import androidx.activity.viewModels
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.firebase.ui.auth.data.model.UserCancellationException
import com.google.firebase.auth.FirebaseAuth
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityLoginBinding
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.dashboard.DashboardActivity
import com.pnam.schedulemanager.ui.forgotpassword.ForgotPasswordActivity
import com.pnam.schedulemanager.ui.register.RegisterActivity
import com.pnam.schedulemanager.utils.LoginType
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    private val loginClick: View.OnClickListener by lazy {
        View.OnClickListener {
            viewModel.loginLoginId(
                binding.username.editText?.text!!.toString(),
                binding.password.editText?.text!!.toString()
            )
        }
    }
    private val registerClick: View.OnClickListener by lazy {
        View.OnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    private val forgotPasswordClick: View.OnClickListener by lazy {
        View.OnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    override fun createUI() {
        noInternetError()
        binding.apply {
            login.setOnClickListener(loginClick)
            register.setOnClickListener(registerClick)
            forgotPassword.setOnClickListener(forgotPasswordClick)
        }
        viewModel.login.observe { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.loginError.visibility = View.INVISIBLE
                    showProgressDialog(R.string.waiting_login)
                }
                is Resource.Success -> {
                    val intent: Intent = Intent(this, DashboardActivity::class.java).apply {
                        putExtra(UID, resource.data.userId)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    dismissProgressDialog()
                    startActivity(intent)
                }
                is Resource.Error -> {
                    dismissProgressDialog()
                    binding.loginError.visibility = View.VISIBLE
                    binding.loginError.setText(R.string.wrong_password)
                }
            }
        }
        viewModel.register.observe { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.loginError.visibility = View.INVISIBLE
                }
                is Resource.Success -> {
                    val (id: String, loginType: LoginType) = resource.data
                    val intent: Intent? = when (loginType) {
                        LoginType.GOOGLE_SIGN_IN -> Intent(
                            this,
                            RegisterActivity::class.java
                        ).apply {
                            putExtra(LOGIN_ID, id)
                            putExtra(LOGIN_TYPE, loginType as Parcelable)
                        }
                        LoginType.NONE -> Intent(this, DashboardActivity::class.java).apply {
                            putExtra(UID, id)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        else -> null
                    }
                    startActivity(intent)
                    dismissProgressDialog()
                }
                is Resource.Error -> {
                    dismissProgressDialog()
                    binding.loginError.visibility = View.VISIBLE
                    binding.loginError.setText(R.string.unknown_error)
                }
            }
        }
        viewModel.internetError.observe {
            binding.loginError.visibility = View.VISIBLE
            binding.loginError.setText(R.string.no_internet)
        }
        binding.signInButton.setOnClickListener {
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }
    }

    private val providers by lazy {
        arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        try {
            if (result.resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                viewModel.loginLoginId(user.uid)
                FirebaseAuth.getInstance().signOut()
            } else {
                if (response == null) {
                    showToast("Cancel")
                }
            }
        } catch (e: UserCancellationException) {
            showToast("Cancel")
        }
    }

    override fun onBackPressed() {
        if (isEmptyFragmentBackStack) {
            twiceTimeToExit()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override val viewModel: LoginViewModel by viewModels()

    companion object {
        const val UID: String = "uid"
        const val LOGIN_ID: String = "loginId"
        const val LOGIN_TYPE: String = "loginType"
    }
}