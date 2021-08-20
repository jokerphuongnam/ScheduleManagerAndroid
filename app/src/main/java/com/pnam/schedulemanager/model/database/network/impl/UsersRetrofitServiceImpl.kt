package com.pnam.schedulemanager.model.database.network.impl

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.database.network.UsersNetwork
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File
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
        avatar: File?
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
            if (avatar != null) {
                MultipartBody.Part.createFormData(
                    "avatar",
                    "avatar",
                    avatar.asRequestBody("image/*".toMediaTypeOrNull())
                )
            } else {
                null
            }
        )
    }

    override suspend fun editProfile(user: User, avatar: File?): Response<User> {
        TODO("Not yet implemented")
    }

    override suspend fun changeAvatar(userId: String, avatar: File?): Response<User> {
        return service.changeAvatar(
            userId.toRequestBody("text/plain".toMediaTypeOrNull()),
            if (avatar != null) {
                MultipartBody.Part.createFormData(
                    "avatar",
                    "avatar",
                    avatar.asRequestBody("image/*".toMediaTypeOrNull())
                )
            } else {
                null
            }
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
            @Field("firstName") firstName: String?,
            @Field("lastName") lastName: String?,
            @Field("birthday") birthday: Long?,
            @Field("gender") gender: Boolean?,
            @Field("avatar") avatar: String?
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
    }

    private companion object {
        private const val PATH = "user/"
    }
}