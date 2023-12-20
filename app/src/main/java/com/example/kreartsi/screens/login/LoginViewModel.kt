package com.example.kreartsi.screens.login

import android.content.ContentValues
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kreartsi.data.response.LoginRequest
import com.example.kreartsi.data.response.LoginResponse
import com.example.kreartsi.data.response.RegisterResponse
import com.example.kreartsi.network.ApiService
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private var _errorLiveData = MutableLiveData<String>()
    var errorLiveData: LiveData<String> = _errorLiveData

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    fun login(loginRequest: LoginRequest) {
        _errorLiveData.value = ""
        _isLogin.value = false

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    Log.d(ContentValues.TAG, "onSuccess: ${response.message()}")
                    val token = response.body()?.token
                    saveTokenToSharedPreferences(token)
                    _isLogin.value = true
                } else {
//                    val errorBody = response.errorBody()
//                    if (errorBody != null) {
//                        Log.e(ContentValues.TAG, "onFailed: ${response.body()}")
//                        _errorLiveData.value = "errorMessage"
//                    } else {
//                        _errorLiveData.value = "Error response body is null"
//                    }
//                    _isLogin.value = false
                    _isLogin.value = false
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        _errorLiveData.value = JSONObject(errorBody!!).getString("message")
                    } catch (e: JSONException) {
                        "Error parsing JSON"
                    }
                    println("Error message: $errorMessage")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                _isLogin.value = false
            }
        })
    }

    private fun saveTokenToSharedPreferences(token: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String? {
        // Retrieve the token from SharedPreferences
        return sharedPreferences.getString("token", null)
    }
}