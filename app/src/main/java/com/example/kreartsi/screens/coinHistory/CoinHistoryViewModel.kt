package com.example.kreartsi.screens.coinHistory

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kreartsi.data.response.DonationHistoryResponse
import com.example.kreartsi.data.response.UserDataResponseItem
import com.example.kreartsi.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CoinHistoryViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
): ViewModel() {
    private val _histories = MutableStateFlow<List<DonationHistoryResponse>>(emptyList())
    val histories: StateFlow<List<DonationHistoryResponse>> get() = _histories.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _error = MutableLiveData("")
    val error: LiveData<String> get() = _error

    private val _coinOwned = MutableStateFlow(0)
    val coinOwned: StateFlow<Int> get() = _coinOwned.asStateFlow()

    private var token = ""
    private var userId = 0
    private var users = listOf<UserDataResponseItem>()

    init {
        token = sharedPreferences.getString("token", null)!!
        getCoinHistories()
    }

    private fun getCoinHistories() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = apiService.getCoinHistory(token)
                _histories.value = response

                val userResponse = apiService.getUserDataSuspend(token)
                _coinOwned.value = userResponse[0].coins
                userId = userResponse[0].userId

                val usersResponse = apiService.getUsers(token)
                users = usersResponse

                _isLoading.value = false
            } catch(e: HttpException) {
                _isLoading.value = false

                val errorBody = e.response()?.errorBody()?.string()
                _error.postValue(JSONObject(errorBody!!).getString("message"))
            } catch(e: Exception) {
                _isLoading.value = false
                _error.postValue(e.message ?: e.toString())
            }
        }
    }

    fun getTime(s: String): String {
        val i = Instant.parse(s)
        val date = Date(i.toEpochMilli())

        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    fun checkIsGifted(donorId: Long): String {
        return if(donorId.toInt() == userId) "gifted"
        else "earned"
    }

    fun getDonorReceiver(
        status: String,
        history: DonationHistoryResponse): String {
        val id = if(status == "earned") {
            history.donorUserID
        } else {
            history.recipientUserID
        }

        val userData = users.first { it.userId == id.toInt() }
        Log.d("TAG", "Status: $status")
        Log.d("TAG", "Current Id: $id")
        Log.d("TAG", "Donor Id: $${history.donorUserID}")
        Log.d("TAG", "Recipient Id: $${history.recipientUserID}")
        Log.d("TAG", "Username: ${userData.username}")
        return userData.username
    }
}