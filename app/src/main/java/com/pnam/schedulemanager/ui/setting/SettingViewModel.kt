package com.pnam.schedulemanager.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.usecase.SettingUseCase
import com.pnam.schedulemanager.throwable.NoConnectivityException
import com.pnam.schedulemanager.throwable.NotLoginException
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val useCase: SettingUseCase
) : BaseViewModel() {
    private val _logoutLiveData: MutableLiveData<Resource<Unit?>> by lazy {
        MutableLiveData<Resource<Unit?>>()
    }

    val logoutLiveData: MutableLiveData<Resource<Unit?>> get() = _logoutLiveData

    internal fun logout() {
        _logoutLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.logout()
                _logoutLiveData.postValue(Resource.Success(null))
            } catch (e: Exception) {
                _logoutLiveData.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    private val _userLiveData: MutableLiveData<Resource<User>> by lazy {
        MutableLiveData<Resource<User>>()
    }

    internal val userLiveData: MutableLiveData<Resource<User>>
        get() {
            return _userLiveData
        }

    internal fun getUser() {
        _userLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = useCase.getUser()
                _userLiveData.postValue(Resource.Success(user))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    else -> {
                        _userLiveData.postValue(Resource.Error(e.message!!))
                    }
                }
            }
        }
    }
}