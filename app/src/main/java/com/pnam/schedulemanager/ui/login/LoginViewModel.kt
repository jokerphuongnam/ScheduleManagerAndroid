package com.pnam.schedulemanager.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.usecase.LoginUseCase
import com.pnam.schedulemanager.throwable.NoConnectivityException
import com.pnam.schedulemanager.throwable.NotFoundException
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.LoginType
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: LoginUseCase
) : BaseViewModel() {
    private val _login: MutableLiveData<Resource<User>> by lazy {
        MutableLiveData<Resource<User>>()
    }

    internal val login: MutableLiveData<Resource<User>> get() = _login

    internal fun loginLoginId(email: String, password: String) {
        _login.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = useCase.loginEmailPass(email, password)
                _login.postValue(Resource.Success(user))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                    else -> {
                        _login.postValue(Resource.Error(e.message ?: ""))
                    }
                }
            }
        }
    }

    private val _register: MutableLiveData<Resource<Pair<String, LoginType>>> by lazy {
        MutableLiveData<Resource<Pair<String, LoginType>>>()
    }

    internal val register: MutableLiveData<Resource<Pair<String, LoginType>>> get() = _register

    internal fun loginLoginId(loginId: String) {
        _login.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = useCase.loginWithLoginId(loginId)
                _register.postValue(Resource.Success(user.userId to LoginType.NONE))
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                    is NotFoundException -> {
                        _register.postValue(Resource.Success(loginId to LoginType.GOOGLE_SIGN_IN))
                    }
                    else -> {
                        _login.postValue(Resource.Error(e.message ?: ""))
                    }
                }
            }
        }
    }
}