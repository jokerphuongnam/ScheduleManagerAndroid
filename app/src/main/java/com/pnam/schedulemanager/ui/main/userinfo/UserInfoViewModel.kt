package com.pnam.schedulemanager.ui.main.userinfo

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.usecase.UserInfoUseCase
import com.pnam.schedulemanager.throwable.NotFoundException
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(private val useCase: UserInfoUseCase) :
    BaseViewModel() {
    private val _userLiveData: MutableLiveData<Resource<User>> by lazy {
        MutableLiveData<Resource<User>>()
    }

    /**
     * when call userLiveData for set observer will action
     * set loading for livedata
     * disposable
     * remove disposable from composite
     * observer from retrofit
     * add disposable to composite
     * */
    internal val userLiveData: MutableLiveData<Resource<User>>
        get() {
            _userLiveData.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val user = useCase.getUser()
                    currentUser.postValue(user)
                    _userLiveData.postValue(com.pnam.schedulemanager.utils.Resource.Success(user))
                } catch (e: Exception) {
                    e.printStackTrace()
                    _userLiveData.postValue(com.pnam.schedulemanager.utils.Resource.Error(e.message.toString()))
                }
            }
            return _userLiveData
        }

    internal val currentUser: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    internal lateinit var tempUser: User

    private val _resultEditUserLiveData: MutableLiveData<Resource<User>> by lazy { MutableLiveData<Resource<User>>() }

    internal val resultEditUserLiveData: MutableLiveData<Resource<User>> get() = _resultEditUserLiveData

    internal var avatar: Uri? = null

    internal fun editProfile() {
        _userLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = useCase.editProfile(currentUser.value!!, avatar)
                _resultEditUserLiveData.postValue(Resource.Success(user))
            } catch (e: Exception) {
                when (e) {
                    is NotFoundException -> {
                        internetError.postValue("")
                    }
                    else -> {
                        _resultEditUserLiveData.postValue(Resource.Error(""))
                    }
                }
            }
        }
    }
}