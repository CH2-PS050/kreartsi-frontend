package com.example.kreartsi.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.kreartsi.BuildConfig
import com.example.kreartsi.network.ApiService
import com.example.kreartsi.screens.detail.DetailScreenViewModel
import com.example.kreartsi.screens.home.HomeViewModel
import com.example.kreartsi.screens.login.LoginViewModel
import com.example.kreartsi.screens.profile.ProfileViewModel
import com.example.kreartsi.screens.register.RegisterViewModel
import com.example.kreartsi.screens.upload.UploadViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModules {
    val baseUrl = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Singleton
    @Provides
    fun provideOkHttp(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideApiClient(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    fun provideRegisterViewModel(
        apiService: ApiService
    ): RegisterViewModel {
        return RegisterViewModel(apiService)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("my_app_pref", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideLoginViewModel(apiService: ApiService, sharedPreferences: SharedPreferences): LoginViewModel {
        return LoginViewModel(apiService, sharedPreferences)
    }

    @Provides
    fun provideHomeViewModel(apiService: ApiService, sharedPreferences: SharedPreferences): HomeViewModel {
        return HomeViewModel(apiService, sharedPreferences)
    }

    @Provides
    fun provideProfileViewModel(apiService: ApiService, sharedPreferences: SharedPreferences): ProfileViewModel {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        return ProfileViewModel(apiService, sharedPreferences, editor)
    }

    @Provides
    fun provideDetailScreenViewModel(apiService: ApiService, sharedPreferences: SharedPreferences): DetailScreenViewModel {
        return DetailScreenViewModel(apiService, sharedPreferences)
    }

    @Provides
    fun provideUploadViewModel(apiService: ApiService, sharedPreferences: SharedPreferences): UploadViewModel {
        return UploadViewModel(apiService, sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    fun clearPreferences(editor: SharedPreferences.Editor) {
        editor.clear()
        editor.apply()
    }
}