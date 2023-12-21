package com.example.kreartsi.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kreartsi.R
import com.example.kreartsi.common.theme.Black
import com.example.kreartsi.common.theme.White
import com.example.kreartsi.common.ui.IconVector
import com.example.kreartsi.screens.coinHistory.CoinHistoryScreen
import com.example.kreartsi.screens.detail.DetailScreen
import com.example.kreartsi.screens.detailPost.DetailPostScreen
import com.example.kreartsi.screens.detailPost.EditPostScreen
import com.example.kreartsi.screens.detect.DetectScreen
import com.example.kreartsi.screens.home.HomeScreen
import com.example.kreartsi.screens.login.LoginScreen
import com.example.kreartsi.screens.profile.EditProfileScreen
import com.example.kreartsi.screens.profile.ProfileScreen
import com.example.kreartsi.screens.profile.SearchProfileScreen
import com.example.kreartsi.screens.register.RegisterScreen
import com.example.kreartsi.screens.splash.SplashScreen
import com.example.kreartsi.screens.upload.UploadScreen

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun KreartsiRoot() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        floatingActionButton = {
            if(currentRoute == KreartsiScreens.HomeScreen.routeName)
                FloatingActionButton(
                    containerColor = Black,
                    onClick = {
                    navController.navigate(KreartsiScreens.UploadScreen.routeName)
                }) {
                    IconVector(vector = Icons.Default.Add)
                }
        },
        bottomBar = {
            if(currentRoute == KreartsiScreens.HomeScreen.routeName ||
                currentRoute == KreartsiScreens.DetectScreen.routeName ||
                currentRoute == KreartsiScreens.ProfileScreen.routeName ||
                currentRoute == KreartsiScreens.DetailScreen.routeName ||
                currentRoute == KreartsiScreens.DetailPostScreen.routeName ||
                currentRoute == KreartsiScreens.GiftScreen.routeName)
                BottomBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = KreartsiScreens.SplashScreen.routeName) {
            composable(route = KreartsiScreens.SplashScreen.routeName) {
                SplashScreen(
                    navController = navController
                )
            }
            composable(route = KreartsiScreens.LoginScreen.routeName) {
                LoginScreen(
                    navController = navController
                )
            }
            composable(route = KreartsiScreens.RegisterScreen.routeName) {
                RegisterScreen(
                    navController = navController
                )
            }
            composable(route = KreartsiScreens.HomeScreen.routeName) {
                HomeScreen(
                    navController = navController,
                )
            }
            composable(route = KreartsiScreens.DetectScreen.routeName) {
                DetectScreen(
                    navController = navController
                )
            }
            composable(route = KreartsiScreens.ProfileScreen.routeName) {
                ProfileScreen(
                    navController = navController
                )
            }
            composable(route = KreartsiScreens.UploadScreen.routeName) {
                UploadScreen(
                    navController = navController
                )
            }
            composable(route = KreartsiScreens.CoinHistoryScreen.routeName) {
                CoinHistoryScreen(
                    navController = navController
                )
            }

            composable(route = "art_detail/{artwork_id}") { backStackEntry ->
                val artworkId = backStackEntry.arguments?.getString("artwork_id")
                DetailScreen(navController = navController, artworkId = artworkId)
            }

            composable(route = "art_post_detail/{artwork_id}") {backStackEntry ->
                val artworkId = backStackEntry.arguments?.getString("artwork_id")
                DetailPostScreen(navController = navController, artworkId = artworkId)
            }

            composable(route = "edit_post_detail/{artwork_id}") {backStackEntry ->
                val artworkId = backStackEntry.arguments?.getString("artwork_id")
                EditPostScreen(navController = navController, artworkId = artworkId)
            }

            composable(route = "edit_photo_profile/{user_id}") {backStackEntry ->
                val userId = backStackEntry.arguments?.getString("user_id")
                EditProfileScreen(navController = navController, userId = userId)
            }

            composable(route = "search_profile_screen/{user_id}") {backStackEntry ->
                val userId = backStackEntry.arguments?.getString("user_id")
                SearchProfileScreen(navController = navController, userId = userId)
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavController,
    currentRoute: String? = "Home"
) {
    NavigationBar(
        containerColor = Black,
        contentColor = White
    ) {
        NavigationBarItem(
            selected = currentRoute == "Home",
            onClick = { navController.navigate(KreartsiScreens.HomeScreen.routeName) },
            icon = {
                IconVector(vector = Icons.Outlined.Home)
            },
            label = { Text(text = "Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = White
            ))
        NavigationBarItem(
            selected = currentRoute == "Detect",
            onClick = { navController.navigate(KreartsiScreens.DetectScreen.routeName) },
            icon = {
                IconVector(vector = ImageVector.vectorResource(id = R.drawable.detect))
            },
            label = { Text(text = "Detect") },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = White
            ))
        NavigationBarItem(
            selected = currentRoute == "Profile",
            onClick = { navController.navigate(KreartsiScreens.ProfileScreen.routeName) },
            icon = {
                IconVector(vector = Icons.Outlined.Person)
            },
            label = { Text(text = "Profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = White
            ))
    }
}
