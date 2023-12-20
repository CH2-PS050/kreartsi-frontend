package com.example.kreartsi.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.kreartsi.R
import com.example.kreartsi.common.theme.Black
import com.example.kreartsi.common.theme.Black5
import com.example.kreartsi.common.theme.White
import com.example.kreartsi.common.ui.IconVector
import com.example.kreartsi.common.ui.ImageDrawable
import com.example.kreartsi.common.ui.boxShadow
import com.example.kreartsi.navigation.KreartsiScreens
import com.example.kreartsi.screens.home.HomeViewModel

@Preview(showBackground = true)
@Composable
fun ProfileScreen(
    navController: NavController = rememberNavController()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileHeader(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            navController = navController
        )
        Spacer(modifier = Modifier.height(22.dp))
        ProfileTabs(navController = navController)
    }
}

@Composable
fun ProfileHeader(
    modifier: Modifier,
    navController: NavController
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    var token = viewModel.getToken()
    var username by remember { mutableStateOf("") }
    var coins by remember { mutableStateOf("") }
    var photoProfile by remember { mutableStateOf("") }
    var receivedUserid by remember { mutableStateOf(0) }

    LaunchedEffect(token) {
        if (token != null){
            viewModel.getUserdata(token) { receivedUsername, receivedCoins, receivedUrl, receivedUid ->
                username = receivedUsername
                coins = receivedCoins
                if (receivedUrl != null) {
                    photoProfile = receivedUrl
                } else run {
                    photoProfile =
                        "https://firebasestorage.googleapis.com/v0/b/paaproject-4a080.appspot.com/o/image%2013.png?alt=media&token=82144bcb-411e-4912-989e-b0085234fcfe"
                }
                receivedUserid = receivedUid
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconVector(
                modifier = Modifier.
                clickable {
                    navController.navigateUp()
                },
                vector = Icons.Default.ArrowBack,
                tint = Black)
            IconVector(
                modifier = Modifier.
                clickable {
                    navController.navigate(
                        KreartsiScreens.LoginScreen.routeName
                    ) {
                        viewModel.clearPref()
                        popUpTo(KreartsiScreens.HomeScreen.routeName) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                vector = Icons.Default.Login,
                tint = Black)
        }
        AsyncImage(
            model = photoProfile,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clip(CircleShape)
                .size(120.dp)
                .clickable {
                    navController.navigate("edit_photo_profile/${receivedUserid}")
                },
        )
        Text(
            text = username,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.padding(bottom = 12.dp))
        Row(
            modifier = Modifier.clickable {
                navController.navigate(KreartsiScreens.CoinHistoryScreen.routeName)
            }
        ) {
            ImageDrawable(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp),
                res = R.drawable.coins_black,
                contentScale = ContentScale.FillWidth)
            Text(
                text = coins,
                fontSize = 12.sp,
                fontWeight = FontWeight.W700)
        }
    }
}

@Composable
fun ProfileTabs(
    navController: NavController
) {
    var selectedTab by remember {
        mutableStateOf("Photo")
    }

    val viewModel: ProfileViewModel = hiltViewModel()

    var token = viewModel.getToken()

    LaunchedEffect(token) {
        if (token != null){
            viewModel.getUserArt(token)
        }
    }

    LaunchedEffect(token) {
        if (token != null){
            viewModel.getSavedArt(token)
        }
    }

    val artListState = viewModel.artListState.collectAsState()
    val artSavedState = viewModel.saveListState.collectAsState()

    Surface(
        modifier = Modifier.boxShadow(
            offset = DpOffset(0.dp, 4.dp),
            blurRadius = 15.dp,
            spreadRadius = 10.dp,
            color = Black5
        ),
        color = White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ImageDrawable(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        selectedTab = "Photo"
                    },
                res = if(selectedTab == "Photo") R.drawable.photo_selected
                else R.drawable.photo_unselected)
            ImageDrawable(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        selectedTab = "Bookmark"
                    },
                contentScale = ContentScale.FillHeight,
                res = if(selectedTab == "Bookmark") R.drawable.bookmark_selected
                else R.drawable.bookmark_unselected)
        }
    }

    if (selectedTab == "Photo"){
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            columns = GridCells.Fixed(3),
            content = {
                items(artListState.value.size) {
                    AsyncImage(
                        model = artListState.value[it].imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(Color.Black)
                            .clickable {
                                navController.navigate("art_post_detail/${artListState.value[it].artworkId}")
                            }
                    )
                }
            }
        )
    } else {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            columns = GridCells.Fixed(3),
            content = {
                items(artSavedState.value.size) {
                    AsyncImage(
                        model = artSavedState.value[it].imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(Color.Black)
                            .clickable {
                                navController.navigate("art_detail/${artSavedState.value[it].artworkId}")
                            }
                    )
                }
            }
        )
    }
}

//@Composable
//fun ProfileGrid(navController: NavController,) {
//    LazyVerticalGrid(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(10.dp)
//            .clickable {
//                navController.navigate(KreartsiScreens.DetailPostScreen.routeName)
//            },
//        verticalArrangement = Arrangement.spacedBy(5.dp),
//        horizontalArrangement = Arrangement.spacedBy(5.dp),
//        columns = GridCells.Fixed(3),
//        content = {
//            items(3) {
//                ImageDrawable(
//                    modifier = Modifier.aspectRatio(1f),
//                    res = R.drawable.scenery)
//            }
//        }
//    )
//}