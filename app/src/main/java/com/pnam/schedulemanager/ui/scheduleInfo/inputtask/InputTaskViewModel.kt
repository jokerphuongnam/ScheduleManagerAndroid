package com.pnam.schedulemanager.ui.scheduleInfo.inputtask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.usecase.InputTaskUseCase
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputTaskViewModel @Inject constructor(
    private val useCase: InputTaskUseCase
) : BaseViewModel() {

    private val _saveTaskLiveData: MutableLiveData<Resource<SaveTaskType>> by lazy { MutableLiveData() }
    internal val saveTaskLiveData: MutableLiveData<Resource<SaveTaskType>> get() = _saveTaskLiveData

    internal fun saveTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            _saveTaskLiveData.postValue(Resource.Loading())
            try {
                if (task.taskId.isEmpty()) {
                    useCase.insertTask(task)
                    _saveTaskLiveData.postValue(Resource.Success(SaveTaskType.INSERT))
                } else {
                    useCase.updateTask(task)
                    _saveTaskLiveData.postValue(Resource.Success(SaveTaskType.UPDATE))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _saveTaskLiveData.postValue(Resource.Error(e.message ?: ""))
            }
        }
    }

    enum class SaveTaskType {
        INSERT, UPDATE
    }
}