package com.example.kreartsi.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kreartsi.R
import com.example.kreartsi.common.ui.ImageDrawable
import com.example.kreartsi.navigation.KreartsiScreens
import com.example.kreartsi.screens.home.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController = rememberNavController()
) {
    val viewModel: HomeViewModel = hiltViewModel()
    LaunchedEffect(key1 = "SplashEffectKey") {
        delay(1500)
        var token = viewModel.getToken()
        if (token != null){
            navController.navigate(
                KreartsiScreens.HomeScreen.routeName
            ){
                popUpTo(KreartsiScreens.SplashScreen.routeName) { inclusive = true }
            }
        } else {
            navController.navigate(KreartsiScreens.LoginScreen.routeName) {
                popUpTo(KreartsiScreens.SplashScreen.routeName) { inclusive = true }
            }
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageDrawable(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 56.dp),
            res = R.drawable.logo)
        Spacer(modifier = Modifier.height(36.dp))
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = White
        )
    }
}

@Preview
@Composable
fun SplashScreenReview() {
    SplashScreen()
}