package com.example.kreartsi.screens.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.kreartsi.R
import com.example.kreartsi.common.theme.Black
import com.example.kreartsi.common.theme.Color58
import com.example.kreartsi.common.theme.Gray
import com.example.kreartsi.common.theme.White
import com.example.kreartsi.common.ui.IconVector
import com.example.kreartsi.common.ui.ImageDrawable
import com.example.kreartsi.data.response.GetArtResponse
import coil.compose.AsyncImage
import com.example.kreartsi.data.response.GetArtResponseItem
import com.example.kreartsi.navigation.KreartsiScreens

private var currentImageUri: Uri? = null


//private var selectedMenu: Boolean? = false

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController()
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Banner(navController)
        Spacer(modifier = Modifier.height(16.dp))
//        SearchBar(navController)
//        Spacer(modifier = Modifier.height(20.dp))
//        Chips()
//        Spacer(modifier = Modifier.height(16.dp))
//        ImageGrid(navController = navController,artworks = GetArtResponse())
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Banner(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    var token = viewModel.getToken()

    val decodedJWT: DecodedJWT = JWT.decode(token)

    var username = decodedJWT.getClaim("username").asString()

    var selectedMenu by remember {
        mutableStateOf(false)
    }

    var query by remember {
        mutableStateOf("")
    }

    var visibleArt by remember { mutableStateOf(true) }

    var visibleUser by remember { mutableStateOf(false) }

    LaunchedEffect(token, selectedMenu) {
        if (token != null){
            viewModel.getArts(token, selectedMenu)
        }
    }

//    LaunchedEffect(token, query) {
//        if (token != null){
//            viewModel.getSearchUser(token, query)
//        }
//    }

    Row(
        modifier = Modifier
            .background(Black)
            .padding(vertical = 16.dp, horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Hello ",
            color = White,
            fontSize = 12.sp)
        Text(
            text = "$username",
            color = White,
            fontSize = 12.sp,
            fontWeight = FontWeight.W700)
        Spacer(modifier = Modifier.weight(1f))
        ImageDrawable(
            modifier = Modifier.size(20.dp),
            res = R.drawable.coins)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "1000",
            color = White,
            fontSize = 12.sp,
            fontWeight = FontWeight.W700)
    }

    Spacer(modifier = Modifier.height(16.dp))


    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        value = query,
        shape = RoundedCornerShape(10.dp),
        placeholder = {
            Text(text = "Search Artist", color = Color58)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Black,
            unfocusedBorderColor = Black
        ),
        leadingIcon = {
            ImageDrawable(
                modifier = Modifier.size(20.dp),
                res = R.drawable.search)
        },
        onValueChange = {
            viewModel.getSearchUser(token!!, query)
            query = it
            visibleArt = false
            visibleUser = true
            if(query == ""){
                visibleArt = true
                visibleUser = false
            }
        })

    Spacer(modifier = Modifier.height(20.dp))

    var selectedChip by remember {
        mutableStateOf("All")
    }
    var selectedOrder by remember {
        mutableStateOf("Newest")
    }
    var expanded by remember { mutableStateOf(false) }

    val chipLabels = listOf(
        "All"
    )
    val orderLabels = listOf(
        "Newest", "Oldest"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        chipLabels.forEach { label ->
            Chip(
                label = label,
                selectedChip = selectedChip,
                onSelect = {
                    selectedChip = it
                })
        }
        Surface(
            color = Gray,
            shape = RoundedCornerShape(10.dp),
        ) {
            Row {
                Text(
                    modifier = Modifier
                        .padding(top = 6.dp, start = 12.dp, bottom = 6.dp)
                        .clickable { expanded = !expanded },
                    text = selectedOrder,
                    color = Black,
                    fontSize = 10.sp)
                IconVector(
                    vector = Icons.Default.KeyboardArrowDown,
                    tint = Black)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {

                orderLabels.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(text = label) },
                        onClick = {
                            selectedOrder = label
                            expanded = false
                            if (label == "Newest"){
                                selectedMenu = false
                                viewModel.getArts(token!!, selectedMenu)
                            } else {
                                selectedMenu = true
                                viewModel.getArts(token!!, selectedMenu)
                            }
                        }
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

//    val viewModel: HomeViewModel = hiltViewModel()
    val artListState = viewModel.artListState.collectAsState()
    val userListState = viewModel.usertListState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    var isLoading by remember {
        mutableStateOf(false)
    }

    if (visibleArt){
        viewModel.isLoading.observe(lifecycleOwner){
            isLoading = it == true
        }
        if (isLoading){
            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    modifier = Modifier.size(60.dp),
                    color = Color.Black
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 8.dp),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                state = rememberLazyStaggeredGridState(),
                content = {

                    items(artListState.value.size) {
                        Box (
                            modifier = Modifier.clickable {
                                navController.navigate("art_detail/${artListState.value[it].artworkId}")
                            }
                        ){

                            AsyncImage(
                                model = artListState.value[it].imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .heightIn(max = 260.dp)
                                    .fillMaxWidth(),
                            )
                        }
                    }
                })
        }
    }

    if (visibleUser){
        viewModel.isLoading.observe(lifecycleOwner){
            isLoading = it == true
        }
        if (isLoading){
            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    modifier = Modifier.size(60.dp),
                    color = Color.Black
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 8.dp),
                columns = StaggeredGridCells.Fixed(1),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                state = rememberLazyStaggeredGridState(),
                content = {

                    items(userListState.value.size) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.Black, // Outline color
                                    shape = RoundedCornerShape(20.dp) // Outline shape (you can use other shapes)
                                )
                                .clickable {
                                    navController.navigate("search_profile_screen/${userListState.value[it].userId}")
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (userListState.value[it].profilePic == null) {
                                        AsyncImage(
                                            model = "https://firebasestorage.googleapis.com/v0/b/paaproject-4a080.appspot.com/o/image%2013.png?alt=media&token=82144bcb-411e-4912-989e-b0085234fcfe",
                                            contentDescription = null,
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .size(50.dp)
                                                .background(Color.Black)
                                        )
                                    } else {
                                        AsyncImage(
                                            model = userListState.value[it].profilePic,
                                            contentDescription = null,
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .size(50.dp)
                                                .background(Color.Black)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(text = userListState.value[it].username)
                                }
                            }
                        }
                    }
                })
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchBar(navController: NavController) {
//    var query by remember {
//        mutableStateOf("")
//    }
//
//    OutlinedTextField(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 10.dp),
//        value = query,
//        shape = RoundedCornerShape(10.dp),
//        placeholder = {
//            Text(text = "Search Artist", color = Color58)
//        },
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            focusedBorderColor = Black,
//            unfocusedBorderColor = Black
//        ),
//        leadingIcon = {
//            ImageDrawable(
//                modifier = Modifier.size(20.dp),
//                res = R.drawable.search)
//        },
//        onValueChange = {
//            query = it
//        })
//
//    Spacer(modifier = Modifier.height(20.dp))
//
//    var selectedChip by remember {
//        mutableStateOf("All")
//    }
//    var selectedOrder by remember {
//        mutableStateOf("Newest")
//    }
//    var expanded by remember { mutableStateOf(false) }
//
//    val chipLabels = listOf(
//        "All", "Human", "Probably AI", "AI"
//    )
//    val orderLabels = listOf(
//        "Newest", "Oldest"
//    )
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 12.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        chipLabels.forEach { label ->
//            Chip(
//                label = label,
//                selectedChip = selectedChip,
//                onSelect = {
//                    selectedChip = it
//                })
//        }
//        Surface(
//            color = Gray,
//            shape = RoundedCornerShape(10.dp),
//        ) {
//            Row {
//                Text(
//                    modifier = Modifier
//                        .padding(top = 6.dp, start = 12.dp, bottom = 6.dp)
//                        .clickable { expanded = !expanded },
//                    text = selectedOrder,
//                    color = Black,
//                    fontSize = 10.sp)
//                IconVector(
//                    vector = Icons.Default.KeyboardArrowDown,
//                    tint = Black)
//            }
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }) {
//
//                orderLabels.forEach { label ->
//                    DropdownMenuItem(
//                        text = { Text(text = label) },
//                        onClick = {
//                            selectedOrder = label
//                            expanded = false
//                            if (label == "Newest"){
//                                selectedMenu = false
//                            } else {
//                                true
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
//
//    Spacer(modifier = Modifier.height(16.dp))
//
//    val viewModel: HomeViewModel = hiltViewModel()
//    val artListState = viewModel.artListState.collectAsState()
//
//    LazyVerticalStaggeredGrid(
//        modifier = Modifier
//            .padding(horizontal = 5.dp, vertical = 8.dp)
//            .clickable {
//                navController.navigate(
//                    KreartsiScreens.DetailScreen.routeName
//                )
//            },
//        columns = StaggeredGridCells.Fixed(2),
//        verticalItemSpacing = 8.dp,
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        state = rememberLazyStaggeredGridState(),
//        content = {
//
//            items(artListState.value.size) {
//                Box (
//                    modifier = Modifier.clickable {
//                        navController.navigate("art_detail/${artListState.value[it].artworkId}")
//                    }
//                ){
//
//                    AsyncImage(
//                        model = artListState.value[it].imageUrl,
//                        contentDescription = null,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(10.dp))
//                            .heightIn(max = 260.dp)
//                            .fillMaxWidth(),
//                    )
//                }
//            }
//        })
//}

//@Composable
//fun Chips() {
//    var selectedChip by remember {
//        mutableStateOf("All")
//    }
//    var selectedOrder by remember {
//        mutableStateOf("Newest")
//    }
//    var expanded by remember { mutableStateOf(false) }
//
//    val chipLabels = listOf(
//        "All", "Human", "Probably AI", "AI"
//    )
//    val orderLabels = listOf(
//        "Newest", "Oldest"
//    )
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 12.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        chipLabels.forEach { label ->
//            Chip(
//                label = label,
//                selectedChip = selectedChip,
//                onSelect = {
//                    selectedChip = it
//                })
//        }
//        Surface(
//            color = Gray,
//            shape = RoundedCornerShape(10.dp),
//        ) {
//            Row {
//                Text(
//                    modifier = Modifier
//                        .padding(top = 6.dp, start = 12.dp, bottom = 6.dp)
//                        .clickable { expanded = !expanded },
//                    text = selectedOrder,
//                    color = Black,
//                    fontSize = 10.sp)
//                IconVector(
//                    vector = Icons.Default.KeyboardArrowDown,
//                    tint = Black)
//            }
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }) {
//
//                orderLabels.forEach { label ->
//                    DropdownMenuItem(
//                        text = { Text(text = label) },
//                        onClick = {
//                            selectedOrder = label
//                            expanded = false
//                            if (label == "Newest"){
//                                selectedMenu = false
//                            } else {
//                                true
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun Chip(
    label: String,
    selectedChip: String,
    onSelect: (String) -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onSelect(label) },
        color = if(label == selectedChip) Black else Gray,
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
            text = label,
            color = if(label == selectedChip) White else Black,
            fontSize = 10.sp)
    }
}

//@Composable
//fun ImageGrid(navController: NavController, artworks: GetArtResponse) {
//
//    val viewModel: HomeViewModel = hiltViewModel()
//    val artListState = viewModel.artListState.collectAsState()
//
//    LazyVerticalStaggeredGrid(
//        modifier = Modifier
//            .padding(horizontal = 5.dp, vertical = 8.dp)
//            .clickable {
//                navController.navigate(
//                    KreartsiScreens.DetailScreen.routeName
//                )
//            },
//        columns = StaggeredGridCells.Fixed(2),
//        verticalItemSpacing = 8.dp,
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        state = rememberLazyStaggeredGridState(),
//        content = {
//
//            items(artListState.value.size) {
//                Box (
//                    modifier = Modifier.clickable {
//                        navController.navigate("art_detail/${artListState.value[it].artworkId}")
//                    }
//                ){
//
//                    AsyncImage(
//                        model = artListState.value[it].imageUrl,
//                        contentDescription = null,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(10.dp))
//                            .heightIn(max = 260.dp)
//                            .fillMaxWidth(),
//                    )
//                }
//            }
//        })
//}

@Preview(showBackground = true)
@Composable
fun HomeScreenReview() {
    HomeScreen()
}