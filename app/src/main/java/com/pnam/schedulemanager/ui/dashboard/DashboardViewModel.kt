package com.pnam.schedulemanager.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.usecase.DashboardUseCase
import com.pnam.schedulemanager.throwable.NoConnectivityException
import com.pnam.schedulemanager.throwable.NotLoginException
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val useCase: DashboardUseCase
) : BaseViewModel() {

    private val _scheduleLiveData: MutableLiveData<Resource<List<Schedule>>> by lazy {
        MutableLiveData<Resource<List<Schedule>>>()
    }

    internal val scheduleLiveData: MutableLiveData<Resource<List<Schedule>>>
        get() {
            return _scheduleLiveData
        }

    internal fun getSchedules(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val schedules = useCase.getSchedules()
                _scheduleLiveData.postValue(Resource.Success(schedules))
            } catch (e: Exception) {
                e.printStackTrace()
                _scheduleLiveData.postValue(Resource.Error(e.message!!))
            }
        }
    }

    internal val deleteLiveData: MutableLiveData<Resource<Long>> by lazy {
        MutableLiveData<Resource<Long>>()
    }

    internal fun delete(schedule: Schedule) {
        deleteLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.deleteSchedule(schedule)
                deleteLiveData.postValue(Resource.Success(0))
            } catch (e: Exception){
                e.printStackTrace()
                deleteLiveData.postValue(Resource.Error(""))
            }
        }
    }

    private val _userLiveData: MutableLiveData<Resource<User?>> by lazy {
        MutableLiveData<Resource<User?>>()
    }

    internal val userLiveData: MutableLiveData<Resource<User?>>
        get() {
            getUser()
            return _userLiveData
        }

    internal fun getUser(){
        _userLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = useCase.getUser()
                _userLiveData.postValue(Resource.Success(user))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is NotLoginException ->{
                        _userLiveData.postValue(Resource.Success(null))
                    }
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                    else -> {
                        _userLiveData.postValue(Resource.Error(e.message!!))
                    }
                }
            }
        }
    }
}