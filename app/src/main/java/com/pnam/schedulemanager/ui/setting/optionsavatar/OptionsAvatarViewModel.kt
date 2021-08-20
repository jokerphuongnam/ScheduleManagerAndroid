package com.pnam.schedulemanager.ui.setting.optionsavatar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.usecase.OptionsAvatarUseCase
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsAvatarViewModel @Inject constructor(
    private val useCase: OptionsAvatarUseCase
) : BaseViewModel() {
    internal val deleteAvatarLiveData: MutableLiveData<Resource<Boolean>> by lazy { MutableLiveData() }

    internal fun deleteAvatar(uid: String) {
        deleteAvatarLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            try {
                useCase.deleteAvatar(uid)
                deleteAvatarLiveData.postValue(Resource.Success(true))
            } catch (e: Exception) {
                e.printStackTrace()
                deleteAvatarLiveData.postValue(Resource.Error(e.message.toString()))
            }
        }
    }
}