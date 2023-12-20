package com.example.kreartsi.screens.profile

import android.content.ContentValues
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kreartsi.data.response.EditProfileResponse
import com.example.kreartsi.data.response.GetArtResponseItem
import com.example.kreartsi.data.response.UserDataResponse
import com.example.kreartsi.data.response.UserDataResponseItem
import com.example.kreartsi.di.NetworkModules.clearPreferences
import com.example.kreartsi.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesEditor: SharedPreferences.Editor
)  : ViewModel() {

    private val _artListState = MutableStateFlow<List<GetArtResponseItem>>(emptyList())
    val artListState: StateFlow<List<GetArtResponseItem>> = _artListState

    private val _saveListState = MutableStateFlow<List<GetArtResponseItem>>(emptyList())
    val saveListState: StateFlow<List<GetArtResponseItem>> = _saveListState

    private val _isChanged = MutableLiveData<Boolean>()
    val isChanged: LiveData<Boolean> = _isChanged

    fun clearPref() {
        clearPreferences(sharedPreferencesEditor)
    }

    fun getUserdata(token : String, callback: (String, String, String?, Int) -> Unit){
        apiService.getUserData(token).enqueue(object : Callback<List<UserDataResponseItem>>{
            override fun onResponse(
                call: Call<List<UserDataResponseItem>>,
                response: Response<List<UserDataResponseItem>>
            ) {
                Log.d(ContentValues.TAG, "onSuccess: ${response.message()}")
                val userData = response.body()?.first()
                userData?.let {
                    callback.invoke(it.username, it.coins.toString(), it.pothoUrl, it.userId)
                }
            }

            override fun onFailure(call: Call<List<UserDataResponseItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getUserbyId(token: String, userId: Int, callback: (String, String, String?, Int) -> Unit){
        apiService.getUserbyId(token, userId).enqueue(object: Callback<List<UserDataResponseItem>>{
            override fun onResponse(
                call: Call<List<UserDataResponseItem>>,
                response: Response<List<UserDataResponseItem>>
            ) {
                Log.d(ContentValues.TAG, "onSuccess: ${response.message()}")
                val userData = response.body()?.first()
                userData?.let {
                    callback.invoke(it.username, it.coins.toString(), it.pothoUrl, it.userId)
                }
            }

            override fun onFailure(call: Call<List<UserDataResponseItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getUserArt(token : String){
        apiService.getUserArt(token).enqueue(object: Callback<List<GetArtResponseItem>>{
            override fun onResponse(
                call: Call<List<GetArtResponseItem>>,
                response: Response<List<GetArtResponseItem>>
            ) {
                Log.d(ContentValues.TAG, "onSuccess: ${response.message()}")
                val userData = response.body()
                _artListState.value = userData ?: emptyList()
            }

            override fun onFailure(call: Call<List<GetArtResponseItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getUserArtById(token: String, userId: Int){
        apiService.getUserArtById(token, userId).enqueue(object: Callback<List<GetArtResponseItem>>{
            override fun onResponse(
                call: Call<List<GetArtResponseItem>>,
                response: Response<List<GetArtResponseItem>>
            ) {
                Log.d(ContentValues.TAG, "onSuccess: ${response.message()}")
                val userData = response.body()
                _artListState.value = userData ?: emptyList()
            }

            override fun onFailure(call: Call<List<GetArtResponseItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getSavedArt(token : String){
        apiService.getSavedArt(token).enqueue(object: Callback<List<GetArtResponseItem>>{
            override fun onResponse(
                call: Call<List<GetArtResponseItem>>,
                response: Response<List<GetArtResponseItem>>
            ) {
                Log.d(ContentValues.TAG, "onSuccess: ${response.message()}")
                val userData = response.body()
                _saveListState.value = userData ?: emptyList()
            }

            override fun onFailure(call: Call<List<GetArtResponseItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun editProfilePhoto(token: String, multipartBody: MultipartBody.Part){
        apiService.updateProfile(token, multipartBody).enqueue(object: Callback<EditProfileResponse>{
            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {
                if(response.isSuccessful){
                    Log.d(ContentValues.TAG, "onSuccess: ${response.message()}")
                    _isChanged.value = true
                } else {
                    _isChanged.value = false
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }
}