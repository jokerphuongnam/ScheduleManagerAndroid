package com.pnam.schedulemanager.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.usecase.MainUseCase
import com.pnam.schedulemanager.throwable.NoConnectivityException
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: MainUseCase
) : BaseViewModel() {
    private val _uid: MutableLiveData<Resource<String>> by lazy {
        MutableLiveData<Resource<String>>()
    }

    internal val uidLiveData: MutableLiveData<Resource<String>>
        get() {
            _uid.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val uid = useCase.currentUser()
                    _uid.postValue(Resource.Success(uid))
                } catch (e: Exception) {
                    e.printStackTrace()
                    when (e) {
                        is NoConnectivityException -> {
                            internetError.postValue("")
                        }
                        else -> {
                            _uid.postValue(Resource.Error(e.message!!))
                        }
                    }
                }
            }
            return _uid
        }

    internal var uid: String
        get() = TODO()
        set(value) {
            _uid.postValue(Resource.Success(value))
        }

    private val _userLiveData: MutableLiveData<Resource<User>> by lazy {
        MutableLiveData<Resource<User>>()
    }

    internal val userLiveData: MutableLiveData<Resource<User>>
        get() {
            _userLiveData.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val user = useCase.getUser()
                    _userLiveData.postValue(Resource.Success(user))
                } catch (e: Exception) {
                    _userLiveData.postValue(Resource.Error(e.message.toString()))
                }
            }
            return _userLiveData
        }
}