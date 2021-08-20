package com.pnam.schedulemanager.ui.main.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.usecase.SettingUseCase
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val useCase: SettingUseCase) : BaseViewModel() {
    private val _logoutLiveData: MutableLiveData<Resource<Unit?>> by lazy {
        MutableLiveData<Resource<Unit?>>()
    }

    val logoutLiveData: MutableLiveData<Resource<Unit?>> get() = _logoutLiveData

    fun logout() {
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
}