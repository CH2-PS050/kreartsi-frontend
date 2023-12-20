package com.example.myapplication.user

import com.example.kreartsi.data.response.LoginResponse
import com.example.kreartsi.network.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository (
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: LoginResponse) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<LoginResponse> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}