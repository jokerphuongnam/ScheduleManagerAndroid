package com.pnam.schedulemanager.ui.main.schedules

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.usecase.SchedulesUseCase
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SchedulesViewModel @Inject constructor(private val useCase: SchedulesUseCase) : BaseViewModel() {

    private val _scheduleLiveData: MutableLiveData<Resource<MutableList<Schedule>>> by lazy {
        MutableLiveData<Resource<MutableList<Schedule>>>()
    }

    /**
     * when call userLiveData for set observer will action
     * set loading for livedata
     * disposable
     * remove disposable from composite
     * observer from retrofit
     * add disposable to composite
     * */
    internal val scheduleLiveData: MutableLiveData<Resource<MutableList<Schedule>>>
        get() {
            getSchedules()
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

    /**
     * when call userLiveData for set observer will action
     * set loading for livedata
     * disposable
     * observer from retrofit
     * when success or error will dispose
     * */
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
}
