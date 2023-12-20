package com.example.myapplication.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.kreartsi.data.response.LoginResponse
import com.example.kreartsi.data.response.UserLogin
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: LoginResponse) {
        dataStore.edit { preferences ->
            preferences[USER_DATA] = Gson().toJson(user.user)
            preferences[USER_TOKEN] = user.token
        }
    }

    fun getSession(): Flow<LoginResponse> {

        return dataStore.data.map { preferences ->
            val userDataJson = preferences[USER_DATA]
            val userData = Gson().fromJson(userDataJson, UserLogin::class.java) ?: UserLogin(0,"", "")
            LoginResponse(
                userData,
                preferences[USER_TOKEN] ?: ""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USER_DATA = stringPreferencesKey("userData")
        private val USER_TOKEN = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}