package com.pnam.schedulemanager.ui.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.usecase.ChangePasswordUseCase
import com.pnam.schedulemanager.throwable.NoConnectivityException
import com.pnam.schedulemanager.throwable.NotFoundException
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val useCase: ChangePasswordUseCase
) : BaseViewModel() {
    private val _changePasswordLiveData: MutableLiveData<Resource<Boolean>> by lazy { MutableLiveData<Resource<Boolean>>() }

    internal val changePasswordLiveData: MutableLiveData<Resource<Boolean>> get() = _changePasswordLiveData

    internal fun changePassword(oldPassword: String, newPassword: String){
        _changePasswordLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.changePassword(oldPassword, newPassword)
                _changePasswordLiveData.postValue(Resource.Success(true))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                    is NotFoundException ->{
                        _changePasswordLiveData.postValue(Resource.Error("NotFound"))
                    }
                    else -> {
                        _changePasswordLiveData.postValue(Resource.Error(""))
                    }
                }
            }
        }
    }
}