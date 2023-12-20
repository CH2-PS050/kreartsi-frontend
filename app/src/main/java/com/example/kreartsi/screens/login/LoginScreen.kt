package com.example.kreartsi.screens.login

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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kreartsi.R
import com.example.kreartsi.common.theme.Green
import com.example.kreartsi.common.ui.ImageDrawable
import com.example.kreartsi.common.ui.LabeledTextField
import com.example.kreartsi.data.response.LoginRequest
import com.example.kreartsi.navigation.KreartsiScreens

var error = ""

@Composable
fun LoginScreen(
    navController: NavController = rememberNavController()
) {
    val viewModel: LoginViewModel = hiltViewModel()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .padding(horizontal = 30.dp)
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageDrawable(
            modifier = Modifier.padding(top = 56.dp, bottom = 64.dp),
            res = R.drawable.logo
        )
        Text(
            text = "Login",
            color = White,
            fontSize = 24.sp,
            fontWeight = FontWeight.W700
        )
        Spacer(modifier = Modifier.height(42.dp))
        LabeledTextField(
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),
            value = email,
            label = "Email",
            keyboardType = KeyboardType.Email,
            onValueChange = { email = it },
            visualTransformation = VisualTransformation.None,
        )
        Spacer(modifier = Modifier.height(42.dp))
        LabeledTextField(
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),
            value = password,
            label = "Password",
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { password = it }
        )
        Spacer(modifier = Modifier.height(72.dp))
        Button(
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green
            ),
            onClick = {
                viewModel.login(LoginRequest(email, password))
                viewModel.isLogin.observe(lifecycleOwner){
                    if (it == true){
                        navController.navigate(
                            KreartsiScreens.HomeScreen.routeName
                        ){
                            popUpTo(KreartsiScreens.LoginScreen.routeName) { inclusive = true }
                        }
                    } else {
                        viewModel.errorLiveData.observe(lifecycleOwner) { errorMessage ->
                            error = errorMessage
                            if (error != "") {
                                error = errorMessage
                                showToast(error)
                                error = ""
                            }
                        }
                    }
                }
//                val token = viewModel.getToken()
//
//                if (token != null){
//                    navController.navigate(
//                        KreartsiScreens.HomeScreen.routeName
//                    )
////                    showToast(token)
//                } else {
//                    showToast("token kosong")
//                }
//                np
            }) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.padding(bottom = 48.dp))
        Row {
            Text(
                text = "Don't have account? ",
                color = White,
                fontSize = 15.sp,
            )
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(
                        KreartsiScreens.RegisterScreen.routeName
                    )
                },
                text = "Signup",
                color = White,
                fontSize = 15.sp,
                fontWeight = FontWeight.W700
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}