package com.example.kreartsi.screens.detail

import android.content.ContentValues
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kreartsi.data.response.DeletePostResponse
import com.example.kreartsi.data.response.DetailArtResponseItem
import com.example.kreartsi.data.response.DonateRequest
import com.example.kreartsi.data.response.DonateResponse
import com.example.kreartsi.data.response.EditPostRequestBody
import com.example.kreartsi.data.response.EditPostResponse
import com.example.kreartsi.data.response.LikeResponse
import com.example.kreartsi.data.response.LikeStatusResponse
import com.example.kreartsi.data.response.SaveResponse
import com.example.kreartsi.data.response.SaveStatusResponse
import com.example.kreartsi.data.response.UserDataResponseItem
import com.example.kreartsi.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _likeCount = MutableStateFlow(0)
    val likeCount: StateFlow<Int> get() = _likeCount

    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> get() = _isLiked

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> get() = _isSaved

    private val _isDonated = MutableLiveData<Boolean>()
    val isDonated: LiveData<Boolean> = _isDonated

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    private val _isChanged = MutableLiveData<Boolean>()
    val isChanged: LiveData<Boolean> = _isChanged

    fun getDetailArt(token: String, artId: Int, callback: (String, String, Int, String, Int) -> Unit){
        apiService.getArtDetails(token, artId).enqueue(object : Callback<List<DetailArtResponseItem>>{
            override fun onResponse(
                call: Call<List<DetailArtResponseItem>>,
                response: Response<List<DetailArtResponseItem>>
            ) {
                Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                val artData = response.body()?.first()
                artData?.let {
                    callback.invoke(it.imageUrl, it.caption, it.likesCount, it.uploadDate, it.userId)
                    _likeCount.value = it.likesCount
                }
            }

            override fun onFailure(call: Call<List<DetailArtResponseItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getUsername(token: String, userId: Int, callback: (String) -> Unit){
        apiService.getUserbyId(token, userId).enqueue(object: Callback<List<UserDataResponseItem>>{
            override fun onResponse(
                call: Call<List<UserDataResponseItem>>,
                response: Response<List<UserDataResponseItem>>
            ) {
                Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                val userData = response.body()?.first()
                userData?.let {
                    // Invoke the callback with username and coins
                    callback.invoke(it.username)
                }
            }

            override fun onFailure(call: Call<List<UserDataResponseItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getIsliked(token: String, artworkId: Int, callback: (Boolean) -> Unit){
        apiService.likedArt(token, artworkId).enqueue(object: Callback<LikeStatusResponse>{
            override fun onResponse(
                call: Call<LikeStatusResponse>,
                response: Response<LikeStatusResponse>
            ) {
                Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                response.body()?.let {
                    callback.invoke(it.isLiked)
                    _isLiked.value = it.isLiked

                }
            }

            override fun onFailure(call: Call<LikeStatusResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun postSave(token: String, artworkId: Int){
        apiService.save(token, artworkId).enqueue(object: Callback<SaveResponse>{
            override fun onResponse(call: Call<SaveResponse>, response: Response<SaveResponse>) {
                if (response.isSuccessful){
                    Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                    _isSaved.value = true
                }
                else {
                    _isSaved.value = true
                }
            }

            override fun onFailure(call: Call<SaveResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun postUnsave(token: String, artworkId: Int){
        apiService.unsave(token, artworkId).enqueue(object: Callback<SaveResponse>{
            override fun onResponse(call: Call<SaveResponse>, response: Response<SaveResponse>) {
                if(response.isSuccessful){
                    Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                    _isSaved.value = false
                } else{
                    _isSaved.value = true
                }
            }

            override fun onFailure(call: Call<SaveResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getIssaved(token: String, artworkId: Int, callback: (Boolean) -> Unit){
        apiService.savedArt(token, artworkId).enqueue(object: Callback<SaveStatusResponse>{
            override fun onResponse(
                call: Call<SaveStatusResponse>,
                response: Response<SaveStatusResponse>
            ) {
                Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                response.body()?.let {
                    callback.invoke(it.isSaved)
                    _isSaved.value = it.isSaved

                }
            }

            override fun onFailure(call: Call<SaveStatusResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun postLike(token: String, artworkId: Int){
        apiService.like(token, artworkId).enqueue(object: Callback<LikeResponse>{
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                if (response.isSuccessful){
                    _isLiked.value = true
                    _likeCount.value ++
                } else {
                    _isLiked.value = false
                }
            }

            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun postUnlike(token: String, artworkId: Int){
        apiService.unlike(token, artworkId).enqueue(object: Callback<LikeResponse>{
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                if (response.isSuccessful){
                    Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                    _isLiked.value = false
                    _likeCount.value --
                } else {
                    _isLiked.value = true
                }
            }

            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun postDonate(token: String, userId: Int, donateRequest: DonateRequest){
        apiService.donatePost(token, userId, donateRequest).enqueue(object: Callback<DonateResponse>{
            override fun onResponse(
                call: Call<DonateResponse>,
                response: Response<DonateResponse>
            ) {
                if (response.isSuccessful){
                    Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                    response.body()?.let {
                        _isDonated.value = true
                    }
                } else{
                    _isDonated.value = false
                }
            }

            override fun onFailure(call: Call<DonateResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                _isDonated.value = false
            }
        })
    }

    fun deletePost(token: String, artworkId: Int){
        apiService.deletePost(token, artworkId).enqueue(object: Callback<DeletePostResponse>{
            override fun onResponse(
                call: Call<DeletePostResponse>,
                response: Response<DeletePostResponse>
            ) {
                if(response.isSuccessful){
                    Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                    _isDeleted.value = true
                } else{
                    _isDeleted.value = false
                }
            }

            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                _isDeleted.value = false
            }

        })
    }

    fun editPost(token: String, artworkId: Int, editPostRequestBody: EditPostRequestBody){
        apiService.editPost(token, artworkId, editPostRequestBody).enqueue(object: Callback<EditPostResponse>{
            override fun onResponse(
                call: Call<EditPostResponse>,
                response: Response<EditPostResponse>
            ) {
                if (response.isSuccessful){
                    Log.d(ContentValues.TAG, "onSuccess: ${response.body()}")
                    _isChanged.value = true
                } else{
                    _isChanged.value = false
                }
            }

            override fun onFailure(call: Call<EditPostResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                _isChanged.value = false
            }

        })
    }

    fun getToken(): String? {
        // Retrieve the token from SharedPreferences
        return sharedPreferences.getString("token", null)
    }
}