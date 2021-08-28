package com.pnam.schedulemanager.model.database.network.impl

import android.graphics.Bitmap
import com.pnam.schedulemanager.model.database.domain.Search
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.database.network.UsersNetwork
import com.pnam.schedulemanager.utils.toMultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Inject

class UsersRetrofitServiceImpl @Inject constructor(
    private val service: Service
) : UsersNetwork {
    override suspend fun login(
        email: String?,
        password: String?,
        userId: String?,
        loginId: String?
    ): Response<User> {
        return service.login(email, password, userId, loginId)
    }

    override suspend fun register(
        user: User,
        email: String?,
        password: String?,
        loginId: String?,
        loginType: String?,
        avatar: Bitmap?
    ): Response<User> {
        return service.register(
            user.firstName.toRequestBody("text/plain".toMediaTypeOrNull()),
            user.lastName.toRequestBody("text/plain".toMediaTypeOrNull()),
            user.birthday.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            user.gender.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            email?.toRequestBody("text/plain".toMediaTypeOrNull()),
            password?.toRequestBody("text/plain".toMediaTypeOrNull()),
            loginId?.toRequestBody("text/plain".toMediaTypeOrNull()),
            loginType?.toRequestBody("text/plain".toMediaTypeOrNull()),
            avatar?.toMultipartBody("avatar")
        )
    }

    override suspend fun editProfile(user: User): Response<User> {
        return service.editProfile(
            user.userId,
            user.firstName,
            user.lastName,
            user.birthday,
            user.gender
        )
    }

    override suspend fun changeAvatar(userId: String, avatar: Bitmap?): Response<User> {
        return service.changeAvatar(
            userId.toRequestBody("text/plain".toMediaTypeOrNull()),
            avatar?.toMultipartBody("avatar")
        )
    }

    override suspend fun deleteAvatar(userId: String): Response<User> {
        return service.editProfile(
            userId,
            avatar = "delete avatar"
        )
    }

    override suspend fun changePassword(
        userId: String,
        oldPassword: String,
        newPassword: String
    ): Response<Unit> {
        return service.changePassword(userId, oldPassword, newPassword)
    }

    override suspend fun forgotPassword(email: String): Response<Unit> {
        return service.forgotPassword(email)
    }

    override suspend fun searchUser(
        userId: String,
        scheduleId: String,
        searchWord: String,
        isInsert: Boolean?
    ): Response<List<Search>> {
        return service.searchUser(userId, scheduleId, searchWord, isInsert)
    }

    override suspend fun deleteSearch(searchId: String?, userId: String?): Response<Unit> {
        return service.deleteSearch(searchId, userId)
    }

    interface Service {
        @GET("${PATH}login")
        suspend fun login(
            @Query("email") email: String?,
            @Query("password") password: String?,
            @Query("userId") userId: String?,
            @Query("loginId") loginId: String?
        ): Response<User>

        @Multipart
        @POST("${PATH}register")
        suspend fun register(
            @Part("firstName") firstName: RequestBody,
            @Part("lastName") lastName: RequestBody,
            @Part("birthday") birthday: RequestBody,
            @Part("gender") gender: RequestBody,
            @Part("email") email: RequestBody?,
            @Part("password") password: RequestBody?,
            @Part("loginId") loginId: RequestBody?,
            @Part("loginType") loginType: RequestBody?,
            @Part avatar: MultipartBody.Part?
        ): Response<User>

        @FormUrlEncoded
        @PUT("${PATH}editprofile")
        suspend fun editProfile(
            @Field("userId") userId: String,
            @Field("firstName") firstName: String? = null,
            @Field("lastName") lastName: String? = null,
            @Field("birthday") birthday: Long? = null,
            @Field("gender") gender: Boolean? = null,
            @Field("avatar") avatar: String? = null
        ): Response<User>

        @Multipart
        @PUT("${PATH}editprofile")
        suspend fun changeAvatar(
            @Part("userId") userId: RequestBody,
            @Part avatar: MultipartBody.Part?
        ): Response<User>

        @FormUrlEncoded
        @PUT("${PATH}changepassword")
        suspend fun changePassword(
            @Field("userId") userId: String,
            @Field("oldPassword") oldPassword: String,
            @Field("newPassword") newPassword: String
        ): Response<Unit>

        @FormUrlEncoded
        @PUT("${PATH}forgotpassword")
        suspend fun forgotPassword(@Field("email") email: String): Response<Unit>

        @FormUrlEncoded
        @POST("${PATH}search")
        suspend fun searchUser(
            @Field("userId") userId: String,
            @Field("scheduleId") scheduleId: String,
            @Field("searchWord") searchWord: String,
            @Field("isInsert") isInsert: Boolean? = null
        ): Response<List<Search>>

        @FormUrlEncoded
        @DELETE("${PATH}deleteSearch")
        suspend fun deleteSearch(
            @Field("searchId") searchId: String?,
            @Field("userId") userId: String?
        ): Response<Unit>
    }

    private companion object {
        private const val PATH = "user/"
    }
}