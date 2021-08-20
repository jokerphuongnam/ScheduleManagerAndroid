package com.pnam.schedulemanager.ui.main.setting

import android.content.Context
import android.content.Intent
import androidx.fragment.app.viewModels
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.FragmentSettingBinding
import com.pnam.schedulemanager.ui.base.BaseFragment
import com.pnam.schedulemanager.ui.changepassword.ChangePasswordActivity
import com.pnam.schedulemanager.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SettingFragment :
    BaseFragment<FragmentSettingBinding, SettingViewModel>(R.layout.fragment_setting) {
    override fun createUI() {
        viewModel.logoutLiveData.observe {

        }
        binding.apply {
            changePassword.setOnClickListener {
                startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
            }
            logout.setOnClickListener {
                viewModel.logout()
                logoutCallback?.invoke()
            }
        }
    }

    private var logoutCallback: (()-> Unit)? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity){
            logoutCallback = context::logout
        }
    }

    override val viewModel: SettingViewModel by viewModels()
}