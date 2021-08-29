package com.pnam.schedulemanager.ui.setting.reviewavatar

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnam.schedulemanager.model.usecase.ReviewAvatarUseCase
import com.pnam.schedulemanager.ui.base.BaseViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewAvatarViewModel @Inject constructor(
    private val useCase: ReviewAvatarUseCase
) : BaseViewModel() {
    internal lateinit var avatar: Bitmap
    internal lateinit var userId: String

    internal val uploadAvatarLiveData: MutableLiveData<Resource<Boolean>> by lazy { MutableLiveData() }

    internal fun uploadAvatar() {
        uploadAvatarLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            try {
                useCase.changeAvatar(userId, avatar)
                uploadAvatarLiveData.postValue(Resource.Success(true))
            } catch (e: Exception) {
                e.printStackTrace()
                uploadAvatarLiveData.postValue(Resource.Error(e.message.toString()))
            }
        }
    }
}