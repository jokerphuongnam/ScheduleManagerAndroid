package com.pnam.schedulemanager.ui.editprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.usecase.EditProfileUseCase
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val useCase: EditProfileUseCase
) : BaseViewModel() {
    private val _editProfileLiveData: MutableLiveData<Resource<Boolean>> by lazy { MutableLiveData() }
    internal val editProfileLiveData: MutableLiveData<Resource<Boolean>> get() = _editProfileLiveData

    fun editProfile(user: User) {
        editProfileLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.editProfile(user)
                editProfileLiveData.postValue(Resource.Success(true))
            } catch (e: Exception) {
                editProfileLiveData.postValue(Resource.Error(e.message ?: ""))
            }
        }
    }
}