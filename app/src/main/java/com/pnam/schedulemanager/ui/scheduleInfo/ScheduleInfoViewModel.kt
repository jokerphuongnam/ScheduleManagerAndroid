package com.pnam.schedulemanager.ui.scheduleInfo

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.usecase.ScheduleInfoUseCase
import com.pnam.schedulemanager.throwable.NoConnectivityException
import com.pnam.schedulemanager.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleInfoViewModel @Inject constructor(
    private val useCase: ScheduleInfoUseCase
) : BaseViewModel() {

    internal fun initNote(nid: String?, type: InsertType? = null) {
        if (nid == null) {
            _newSchedule.postValue(Schedule().apply {
                type ?: return@apply
                when (type) {
                    InsertType.CHECK_BOX -> {
                        tasks.apply {
                            add(Task())
                        }
                    }
                }
            })
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val schedule = useCase.getSchedule(nid)
                    _newSchedule.postValue(schedule)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private var isUpdate = false

    private val _newSchedule: MutableLiveData<Schedule> by lazy { MutableLiveData<Schedule>() }
    internal val newSchedule: MutableLiveData<Schedule>
        get() = _newSchedule

    internal val images: MutableList<Uri> by lazy { mutableListOf() }
    internal val sounds: MutableList<Uri> by lazy { mutableListOf() }

    internal fun saveNote() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.saveNote(_newSchedule.value!!, images, sounds, isUpdate = isUpdate)
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
}