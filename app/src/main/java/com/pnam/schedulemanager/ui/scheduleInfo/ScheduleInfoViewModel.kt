package com.pnam.schedulemanager.ui.scheduleInfo

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.usecase.ScheduleInfoUseCase
import com.pnam.schedulemanager.throwable.NoConnectivityException
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleInfoViewModel @Inject constructor(
    private val useCase: ScheduleInfoUseCase
) : BaseViewModel() {

    internal fun initSchedule(scheduleId: String?) {
        if (scheduleId == null) {
            _newSchedule.postValue(Schedule())
        } else {
            getScheduleInfo(scheduleId)
        }
    }

    internal fun getScheduleInfo(){
        getScheduleInfo(_newSchedule.value!!.scheduleId)
    }

    private fun getScheduleInfo(scheduleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val schedule = useCase.getScheduleInfo(scheduleId)
                _newSchedule.postValue(schedule)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _newSchedule: MutableLiveData<Schedule> by lazy { MutableLiveData() }
    internal val newSchedule: MutableLiveData<Schedule>
        get() = _newSchedule

    private val _updateSchedule: MutableLiveData<Resource<String>> by lazy { MutableLiveData() }
    internal val updateSchedule: MutableLiveData<Resource<String>> get() = _updateSchedule

    internal fun saveSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _updateSchedule.postValue(Resource.Loading())
                val schedule = _newSchedule.value!!
                if (schedule.scheduleId.isNotEmpty()) {
                    useCase.updateSchedule(schedule)
                    getScheduleInfo()
                } else {
                    getScheduleInfo(useCase.insertSchedule(schedule).scheduleId)
                }
                _updateSchedule.postValue(Resource.Success(schedule.scheduleId))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                    else -> {
                        _updateSchedule.postValue(Resource.Error(e.message ?: ""))
                    }
                }
            }
        }
    }

    internal fun insertMedia(media: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val schedule = _newSchedule.value!!
                useCase.addMultiMedia(schedule.scheduleId, mutableListOf(media))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                    else -> {
                    }
                }
            }
        }
    }

    internal fun deleteMedia() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    private val _toggleTaskLiveData: MutableLiveData<Resource<String>> by lazy { MutableLiveData() }
    internal val toggleTaskLiveData: MutableLiveData<Resource<String>> get() = _toggleTaskLiveData

    internal fun toggleTask(task: Task, isFinish: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _toggleTaskLiveData.postValue(Resource.Loading())
            try {
                useCase.toggleTask(task, isFinish)
                _toggleTaskLiveData.postValue(Resource.Success(task.taskId))
                getScheduleInfo()
            } catch (e: Exception) {
                e.printStackTrace()
                _toggleTaskLiveData.postValue(Resource.Error(e.message ?: ""))
            }
        }
    }

    internal fun deleteTask(taskId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.deleteTask(taskId)
                getScheduleInfo(_newSchedule.value!!.scheduleId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}