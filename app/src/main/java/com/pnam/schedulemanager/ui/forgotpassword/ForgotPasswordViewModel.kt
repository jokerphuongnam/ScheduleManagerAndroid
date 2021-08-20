package com.pnam.schedulemanager.ui.forgotpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.usecase.ForgotPasswordUseCase
import com.pnam.schedulemanager.throwable.NoConnectivityException
import com.pnam.schedulemanager.throwable.NotFoundException
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val useCase: ForgotPasswordUseCase): BaseViewModel() {

    private val _changePasswordLiveData: MutableLiveData<Resource<Int>> by lazy { MutableLiveData<Resource<Int>>() }

    internal val changePasswordLiveData: MutableLiveData<Resource<Int>> get() = _changePasswordLiveData

    fun recoverPassword(email: String){
        _changePasswordLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.forgotPassword(email)
                _changePasswordLiveData.postValue(Resource.Success(0))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                    is NotFoundException ->{
                        _changePasswordLiveData.postValue(Resource.Error("Cannot find username"))
                    }
                    else -> {
                    }
                }
            }
        }
    }
}