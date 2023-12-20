package com.example.kreartsi.screens.home

import android.content.ContentValues
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kreartsi.data.response.GetArtResponseItem
import com.example.kreartsi.data.response.SearchResponse
import com.example.kreartsi.data.response.SearchResponseItem
import com.example.kreartsi.data.response.UserDataResponseItem
import com.example.kreartsi.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _artListState = MutableStateFlow<List<GetArtResponseItem>>(emptyList())
    val artListState: StateFlow<List<GetArtResponseItem>> = _artListState

    private val _userListState = MutableStateFlow<List<SearchResponseItem>>(emptyList())
    val usertListState: StateFlow<List<SearchResponseItem>> = _userListState

    private var _errorLiveData = MutableLiveData<String>()
    var errorLiveData: LiveData<String> = _errorLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getToken(): String? {
        // Retrieve the token from SharedPreferences
        return sharedPreferences.getString("token", null)
    }

    fun getArts(token : String, query: Boolean) {
        _isLoading.value = true
        apiService.getArtSorted(token, query).enqueue(object : Callback<List<GetArtResponseItem>>{
            override fun onResponse(
                call: Call<List<GetArtResponseItem>>,
                response: Response<List<GetArtResponseItem>>
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                    _artListState.value = response.body() ?: emptyList()
                } else {
                    _isLoading.value = false
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        _errorLiveData.value = JSONObject(errorBody!!).getString("message")
                    } catch (e: JSONException) {
                        "Error parsing JSON"
                    }
                    println("Error message: $errorMessage")
                }
            }

            override fun onFailure(call: Call<List<GetArtResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getSearchUser(token: String, query: String){
        _isLoading.value = true
        apiService.searchUsers(token, query).enqueue(object: Callback<List<SearchResponseItem>>{
            override fun onResponse(
                call: Call<List<SearchResponseItem>>,
                response: Response<List<SearchResponseItem>>
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                    _userListState.value = response.body() ?: emptyList()
                } else {
                    _isLoading.value = false
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        _errorLiveData.value = JSONObject(errorBody!!).getString("message")
                    } catch (e: JSONException) {
                        "Error parsing JSON"
                    }
                    println("Error message: $errorMessage")
                }
            }

            override fun onFailure(call: Call<List<SearchResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}