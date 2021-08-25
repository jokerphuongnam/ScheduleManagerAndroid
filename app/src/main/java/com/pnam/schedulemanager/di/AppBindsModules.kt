package com.pnam.schedulemanager.di

import com.pnam.schedulemanager.model.database.local.CurrentUser
import com.pnam.schedulemanager.model.database.local.LocalReference
import com.pnam.schedulemanager.model.database.local.impl.DataStoreCurrentUserImpl
import com.pnam.schedulemanager.model.database.network.NetworkConnectionInterceptor
import com.pnam.schedulemanager.model.database.network.SchedulesNetwork
import com.pnam.schedulemanager.model.database.network.UsersNetwork
import com.pnam.schedulemanager.model.database.network.impl.SchedulesRetrofitServiceImpl
import com.pnam.schedulemanager.model.database.network.impl.UsersRetrofitServiceImpl
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.repository.impl.DefaultSchedulesRepositoryImpl
import com.pnam.schedulemanager.model.repository.impl.DefaultUsersRepositoryImpl
import com.pnam.schedulemanager.model.usecase.*
import com.pnam.schedulemanager.model.usecase.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Interceptor

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindsModules {
    //interceptor
    @Binds
    abstract fun getInterceptor(interceptor: NetworkConnectionInterceptor): Interceptor

    //database
    @Binds
    abstract fun getSettingLocal(local: LocalReference): LocalReference

    @Binds
    abstract fun getCurrentUser(local: DataStoreCurrentUserImpl): CurrentUser

    @Binds
    abstract fun getNoteNetwork(network: SchedulesRetrofitServiceImpl): SchedulesNetwork

    @Binds
    abstract fun getUserNetwork(network: UsersRetrofitServiceImpl): UsersNetwork

    //repository
    @Binds
    abstract fun getNoteRepository(repository: DefaultSchedulesRepositoryImpl): SchedulesRepository

    @Binds
    abstract fun getUserRepository(repository: DefaultUsersRepositoryImpl): UsersRepository

    //use case
    @Binds
    abstract fun getLoginUseCase(useCase: DefaultLoginUseCaseImpl): LoginUseCase

    @Binds
    abstract fun getEditNoteUseCase(useCase: DefaultEditScheduleUseCaseImpl): EditScheduleUseCase

    @Binds
    abstract fun getNoteInfoUseCase(useCase: DefaultScheduleInfoUseCaseImpl): ScheduleInfoUseCase

    @Binds
    abstract fun getNotesUseCase(useCase: DefaultSchedulesUseCaseImpl): SchedulesUseCase

    @Binds
    abstract fun getSettingUseCase(useCase: DefaultSettingUseCaseImpl): SettingUseCase

    @Binds
    abstract fun getForgotPasswordUseCase(useCase: DefaultForgotPasswordUseCaseImpl): ForgotPasswordUseCase

    @Binds
    abstract fun getChangePasswordUseCase(useCase: DefaultChangePasswordUseCaseImpl): ChangePasswordUseCase

    @Binds
    abstract fun getRegisterUseCase(useCase: DefaultRegisterUseCaseImpl): RegisterUseCase

    @Binds
    abstract fun getDashboardUseCase(useCase: DefaultDashboardUseCaseImpl): DashboardUseCase

    @Binds
    abstract fun getOptionsAvatarUseCase(useCase: DefaultOptionsAvatarUseCaseImpl): OptionsAvatarUseCase

    @Binds
    abstract fun getReviewAvatarUseCase(useCase: DefaultReviewAvatarUseCaseImpl): ReviewAvatarUseCase

    @Binds
    abstract fun getInputTaskUseCase(useCase: DefaultInputTaskUseCaseImpl): InputTaskUseCase

    @Binds
    abstract fun getMembersUseCase(useCase: DefaultMembersUseCaseImpl): MembersUseCase

    @Binds
    abstract fun getEditProfileUseCase(useCase: DefaultEditProfileUseCaseImpl): EditProfileUseCase
}