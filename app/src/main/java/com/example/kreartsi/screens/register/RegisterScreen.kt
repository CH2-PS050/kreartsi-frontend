package com.example.kreartsi.screens.register

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kreartsi.R
import com.example.kreartsi.common.theme.Blue
import com.example.kreartsi.common.ui.ImageDrawable
import com.example.kreartsi.common.ui.LabeledTextField
import com.example.kreartsi.data.response.RegisterRequest
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



var error = ""

@Composable
fun RegisterScreen(
    navController: NavController = rememberNavController(),
) {
    val viewModel: RegisterViewModel = hiltViewModel()
    var isPasswordVisible by remember { mutableStateOf(false) }

    var username by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    Column(
        modifier = Modifier
//            .padding(horizontal = 30.dp)
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageDrawable(
            modifier = Modifier.padding(top = 56.dp, bottom = 48.dp),
            res = R.drawable.logo
        )
        Text(
            text = "Signup",
            color = White,
            fontSize = 24.sp,
            fontWeight = FontWeight.W700
        )
        Spacer(modifier = Modifier.height(24.dp))
        LabeledTextField(
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),
            value = username,
            label = "Username",
            onValueChange = { username = it },
            visualTransformation = VisualTransformation.None,
        )
        Spacer(modifier = Modifier.height(16.dp))
        LabeledTextField(
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),
            value = email,
            label = "Email",
            keyboardType = KeyboardType.Email,
            onValueChange = { email = it },
            visualTransformation = VisualTransformation.None
        )
        Spacer(modifier = Modifier.height(16.dp))
        LabeledTextField(
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),
            value = password,
            label = "Password",
            keyboardType = KeyboardType.Password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            ),
            onClick = {
                viewModel.postRegister(username, email, password)
                viewModel.isSignedup.observe(lifecycleOwner){
                    if (it){
                        navController.navigateUp()
                        showToast("Login Berhasil!")
                    } else {
                        viewModel.errorLiveData.observe(lifecycleOwner){
                            error = it
                            if (error != ""){
                                error = it
                                showToast(error)
                                error = ""
                            }
                        }
                    }
                }
            }) {
            Text(text = "Signup")
        }
        Spacer(modifier = Modifier.padding(bottom = 32.dp))
        Row {
            Text(
                text = "Already have account? ",
                color = White,
                fontSize = 15.sp,
            )
            Text(
                modifier = Modifier.clickable {
                    navController.navigateUp()
                },
                text = "Login",
                color = White,
                fontSize = 15.sp,
                fontWeight = FontWeight.W700
            )
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _isSignedup = MutableLiveData<Boolean>()
    val isSignedup: LiveData<Boolean> = _isSignedup

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun postRegister(username: String, email: String, password: String) {
        _errorLiveData.value = ""
        _isSignedup.value = false

        apiService.register(RegisterRequest(username, email, password)).enqueue(object :
            Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(ContentValues.TAG, "onSuccess: ${response.message()}")
                    _isSignedup.value = true
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.errorBody().toString()}")
                    _isSignedup.value = false
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        _errorLiveData.value = JSONObject(errorBody!!).getString("message")
                    } catch (e: JSONException) {
                        "Error parsing JSON"
                    }
                    println("Error message: $errorMessage")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}