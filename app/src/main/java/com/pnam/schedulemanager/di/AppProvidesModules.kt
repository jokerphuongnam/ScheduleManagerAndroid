package com.pnam.schedulemanager.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pnam.schedulemanager.model.database.domain.Reference
import com.pnam.schedulemanager.model.database.local.AppDatabase
import com.pnam.schedulemanager.model.database.local.SchedulesLocal
import com.pnam.schedulemanager.model.database.local.impl.UserLocal
import com.pnam.schedulemanager.model.database.network.NetworkConnectionInterceptor
import com.pnam.schedulemanager.model.database.network.impl.SchedulesRetrofitServiceImpl
import com.pnam.schedulemanager.model.database.network.impl.UsersRetrofitServiceImpl
import com.pnam.schedulemanager.utils.RetrofitUtils.BASE_URL
import com.pnam.schedulemanager.utils.RoomConstrain.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppProvidesModules {

    @Provides
    @Singleton
    fun providerReference(): Reference = Reference()

    //room
    @Provides
    @Singleton
    fun providerRoomDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()

    @Provides
    @Singleton
    fun providerNoteLocal(appDatabase: AppDatabase): SchedulesLocal = appDatabase.noteDao()

    @Provides
    @Singleton
    fun providerUserLocal(appDatabase: AppDatabase): UserLocal = appDatabase.userDao()

    //retrofit
    @Provides
    @Singleton
    fun providerGson(): Gson =
        GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create()

    @Provides
    @Singleton
    fun providerGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun providerLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun providerOkHttp(
        interceptor: NetworkConnectionInterceptor,
        logging: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(logging)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun providerRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttp: OkHttpClient
    ): Retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttp)
        .build()

    @Provides
    @Singleton
    fun providerNoteService(retrofit: Retrofit): SchedulesRetrofitServiceImpl.Service =
        retrofit.create(SchedulesRetrofitServiceImpl.Service::class.java)

    @Provides
    @Singleton
    fun providerUserService(retrofit: Retrofit): UsersRetrofitServiceImpl.Service =
        retrofit.create(UsersRetrofitServiceImpl.Service::class.java)
}