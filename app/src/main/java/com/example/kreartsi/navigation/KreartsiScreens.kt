package com.example.kreartsi.navigation

sealed class KreartsiScreens(
    val routeName: String
) {
    object SplashScreen: KreartsiScreens("splashScreen")
    object LoginScreen: KreartsiScreens("loginScreen")
    object RegisterScreen: KreartsiScreens("registerScreen")
    object HomeScreen: KreartsiScreens("homeScreen")
    object DetectScreen: KreartsiScreens("detectScreen")
    object ProfileScreen: KreartsiScreens("profileScreen")
    object UploadScreen: KreartsiScreens("uploadScreen")
    object CoinHistoryScreen: KreartsiScreens("coinHistoryScreen")
    object DetailScreen: KreartsiScreens("detailScreen")
    object DetailPostScreen: KreartsiScreens("detailPostScreen")
    object GiftScreen: KreartsiScreens("giftScreen")
}