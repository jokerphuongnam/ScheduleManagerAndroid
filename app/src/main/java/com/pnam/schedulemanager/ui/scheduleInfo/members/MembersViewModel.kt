package com.pnam.schedulemanager.ui.scheduleInfo.members

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Search
import com.pnam.schedulemanager.model.usecase.MembersUseCase
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MembersViewModel @Inject constructor(
    private val useCase: MembersUseCase
) : BaseViewModel() {
    private val _searchUserLiveData: MutableLiveData<Resource<List<Search>>> by lazy { MutableLiveData() }
    internal val searchUserLiveData: MutableLiveData<Resource<List<Search>>> get() = _searchUserLiveData

    internal fun searchUser(searchWord: String, scheduleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchUserLiveData.postValue(Resource.Loading())
            try {
                useCase.searchUser(searchWord, scheduleId).let { flow ->
                    flow.collect { searches ->
                        _searchUserLiveData.postValue(Resource.Success(searches))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _searchUserLiveData.postValue(Resource.Error(e.message ?: ""))
            }
        }
    }

    private val _searchResultsLiveData: MutableLiveData<Resource<List<Search>>> by lazy { MutableLiveData() }
    internal val searchResultsLiveData: MutableLiveData<Resource<List<Search>>> get() = _searchResultsLiveData

    internal fun submitSearch(searchWord: String, scheduleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchResultsLiveData.postValue(Resource.Loading())
                _searchResultsLiveData.postValue(
                    Resource.Success(
                        useCase.getSearchResultUser(
                            searchWord,
                            scheduleId
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResultsLiveData.postValue(Resource.Error(e.message ?: ""))
            }
        }
    }

    private val _deleteSearchLiveData: MutableLiveData<Resource<Boolean>> by lazy { MutableLiveData() }
    internal val deleteSearchLiveData: MutableLiveData<Resource<Boolean>> get() = _deleteSearchLiveData

    internal fun deleteSearch(searchId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _deleteSearchLiveData.postValue(Resource.Loading())
                useCase.deleteSearch(searchId)
                _deleteSearchLiveData.postValue(Resource.Success(true))
            } catch (e: Exception) {
                e.printStackTrace()
                _deleteSearchLiveData.postValue(Resource.Error(e.message ?: ""))
            }
        }
    }

    internal fun clearSearch() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    private val _addMemberLiveData: MutableLiveData<Resource<Boolean>> by lazy { MutableLiveData() }
    internal val addMemberLiveData: MutableLiveData<Resource<Boolean>> get() = _addMemberLiveData

    internal fun addMember(userIdBeAdded: String, scheduleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _addMemberLiveData.postValue(Resource.Loading())
                useCase.addMember(userIdBeAdded, scheduleId)
                _addMemberLiveData.postValue(Resource.Success(true))
            } catch (e: Exception) {
                _addMemberLiveData.postValue(Resource.Error(e.message ?: ""))
            }
        }
    }

    internal fun leaveGroup(schedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.leaveGroup(schedule.scheduleId)
        }
    }

    internal fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.deleteSchedule(schedule)
        }
    }
}