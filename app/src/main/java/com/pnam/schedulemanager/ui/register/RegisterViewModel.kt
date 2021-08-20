package com.pnam.schedulemanager.ui.register

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.usecase.RegisterUseCase
import com.pnam.schedulemanager.throwable.NotFoundException
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.LoginType
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val useCase: RegisterUseCase
) : BaseViewModel() {
    internal val currentUser: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    init {
        currentUser.postValue(User())
    }

    private val _registerLiveData: MutableLiveData<Resource<User>> by lazy { MutableLiveData<Resource<User>>() }

    internal val registerLiveData: MutableLiveData<Resource<User>> get() = _registerLiveData

    internal var avatar: Uri? = null

    internal fun registerWithEmailPass(email: String, password: String) {
        _registerLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user =
                    useCase.register(currentUser.value!!, email, password, null, null, avatar)
                _registerLiveData.postValue(Resource.Success(user))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is NotFoundException -> {
                        internetError.postValue("")
                    }
                    else -> {
                        _registerLiveData.postValue(Resource.Error(""))
                    }
                }
            }
        }
    }

    var loginInfo: Pair<String, LoginType>? = null

    internal fun registerWithLoginId() {
        _registerLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val (loginId, loginType) = loginInfo!!
                val user =
                    useCase.register(
                        currentUser.value!!,
                        null,
                        null,
                        loginId,
                        loginType.rawValue,
                        avatar
                    )
                _registerLiveData.postValue(Resource.Success(user))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is NotFoundException -> {
                        internetError.postValue("")
                    }
                    else -> {
                        _registerLiveData.postValue(Resource.Error(""))
                    }
                }
            }
        }
    }
}