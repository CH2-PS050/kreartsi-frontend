package com.example.kreartsi.screens.upload

import android.content.ContentValues
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kreartsi.data.response.UploadResponse
import com.example.kreartsi.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _isUploaded = MutableLiveData<Boolean>()
    val isUploaded: LiveData<Boolean> = _isUploaded

    fun uploadPost(token: String, multipartBody: MultipartBody.Part, requestBody: RequestBody){
        apiService.uploadArt(token, multipartBody, requestBody).enqueue(object: Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful){
                    Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                    _isUploaded.value = true
                } else {
                    _isUploaded.value = false
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }
}