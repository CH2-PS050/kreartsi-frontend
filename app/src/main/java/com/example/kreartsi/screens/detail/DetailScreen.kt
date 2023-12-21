package com.example.kreartsi.screens.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.kreartsi.common.theme.Color0A
import com.example.kreartsi.common.theme.Color22
import com.example.kreartsi.common.theme.White
import com.example.kreartsi.common.ui.IconVector
import com.example.kreartsi.common.ui.ImageDrawable
import com.example.kreartsi.common.ui.PrimaryButton
import com.example.kreartsi.common.ui.boxShadow
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import com.example.kreartsi.data.response.DonateRequest
import com.example.kreartsi.data.response.LoginRequest
import com.example.kreartsi.navigation.KreartsiScreens
import com.vdurmont.emoji.EmojiParser
import kotlinx.coroutines.flow.observeOn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//@Preview(showBackground = true)
@Composable
fun DetailScreen(
    navController: NavController = rememberNavController(),
    artworkId: String?
) {
    val viewModel: DetailScreenViewModel = hiltViewModel()
    var token = viewModel.getToken()
    var userName by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }
    var like by remember { mutableStateOf(0) }
    var date by remember { mutableStateOf("") }
    var uid by remember { mutableStateOf(0) }

    var uidLogedin by remember { mutableStateOf(0) }

    var isLikedRoot by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(token) {
        if (token != null) {
            viewModel.getDetailArt(
                token,
                artworkId!!.toInt()
            ) { receivedImage, receivedCaption, receivedLike, receivedDate, receivedUid ->
                imageUrl = receivedImage
                caption = receivedCaption
                like = receivedLike
                date = receivedDate
                uid = receivedUid
            }
        }
    }

    LaunchedEffect(token, uid) {
        if (token != null && uid != null) {
            viewModel.getUsername(
                token,
                uid
            ) { receivedUserName ->
                userName = receivedUserName
            }
        }
    }

    LaunchedEffect(token) {
        if (token != null) {
            viewModel.getUserdata(
                token
            ) { receivedUserId ->
                uidLogedin = receivedUserId
            }
        }
    }

    LaunchedEffect(token, artworkId){
        if (token != null && artworkId != null){
            viewModel.getIsliked(token,artworkId.toInt()){
                isLikedRoot = it
            }
        }
    }

    LaunchedEffect(token, artworkId){
        if (token != null && artworkId != null){
            viewModel.getIssaved(token,artworkId.toInt()){
                isSaved = it
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Header(navController,imageUrl, like.toString(), isLikedRoot, token, artworkId?.toInt())
        Spacer(modifier = Modifier.padding(top = 13.dp))
        Body(userName, caption, date, navController, uid, uidLogedin)
    }
}

@Composable
fun Header(
    navController: NavController,
    photoProfile: String?,
    like: String?,
    isLikedRoot: Boolean,
    token: String?,
    artworkId: Int?
) {

    val viewModel: DetailScreenViewModel = hiltViewModel()

    val lifecycleOwner = LocalLifecycleOwner.current

    val isLiked by viewModel.isLiked.collectAsState()
    val likeCount by viewModel.likeCount.collectAsState()

    val isSaved by viewModel.isSaved.collectAsState()

    Column {
        Box {
            AsyncImage(
                model = photoProfile,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
            )
            IconVector(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigateUp()
                    },
                vector = Icons.Default.ArrowBack)
        }
        Box(
            modifier = Modifier
                .background(White)
                .boxShadow(
                    offset = DpOffset(0.dp, 4.dp),
                    blurRadius = 15.dp,
                    spreadRadius = 5.dp,
                    color = Black5
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                if (isLiked){
                IconVector(
                    vector = if (isLiked) {
                        Icons.Default.Favorite
                    } else{
                        Icons.Default.FavoriteBorder
                    },
                    tint = Color22,
                    modifier = Modifier.clickable {
                        if (isLiked) {
                            viewModel.postUnlike(token!!, artworkId!!)
                        } else {
                            viewModel.postLike(token!!, artworkId!!)
                        }
                    })
                Spacer(modifier = Modifier
                    .width(8.dp)
                )
                Text(
                    text = likeCount.toString(),
                    fontWeight = FontWeight.W700,
                    fontSize = 10.sp)
                Spacer(modifier = Modifier.weight(1f))
                IconVector(
                    vector = if (isSaved){
                        Icons.Default.Bookmark
                    } else {
                        Icons.Default.BookmarkBorder
                    },
                    tint = Color22,
                    modifier = Modifier.clickable {
                        if (isSaved) {
                            viewModel.postUnsave(token!!, artworkId!!)
                        } else {
                            viewModel.postSave(token!!, artworkId!!)
                        }
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Body(
    username: String?,
    caption: String?,
    date: String?,
    navController: NavController,
    uid: Int?,
    uidLogedin: Int?
) {
    val viewModel: DetailScreenViewModel = hiltViewModel()

    var showDialog by remember { mutableStateOf(false) }
    var coinValue by remember { mutableStateOf("") }

    val context = LocalContext.current

    val token = viewModel.getToken()

    val lifecycleOwner = LocalLifecycleOwner.current

    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    var output_date by remember { mutableStateOf("") }

    if (date!!.isNotEmpty()) {
        val inputDateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        val outputDateFormat = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
        output_date = inputDateTime.format(outputDateFormat)
    } else {
        println("date empty")
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageDrawable(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp),
                res = R.drawable.profile,
                contentScale = ContentScale.Crop)
            Text(
                text = username!!,
                fontWeight = FontWeight.W700,
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    if (uid == uidLogedin){
                        navController.navigate(
                            KreartsiScreens.ProfileScreen.routeName
                        )
                    } else {
                        navController.navigate("search_profile_screen/${uid}")
                    }
                })
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = EmojiParser.parseToUnicode(caption!!),
            fontSize = 12.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = Black)
        Text(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 87.dp)
                .fillMaxWidth(),
            text = output_date,
            fontSize = 12.sp,
            color = Color0A,
            textAlign = TextAlign.End)
        PrimaryButton(
            onClick = {
               showDialog = true
            },
            modifier = Modifier
                .padding(horizontal = 45.dp),
            label = "Gift Coins"
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Box (
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "Enter the nominal gift")
                    }
                },
                text = {
                    TextField(
                        value = coinValue,
                        onValueChange = {
                            coinValue = it
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.postDonate(token!!, uid!!, DonateRequest(coinValue.toInt()))
                            viewModel.isDonated.observe(lifecycleOwner){
                                if (it){
                                    showDialog = false
                                    showToast("Gift Success")
                                    coinValue = ""
                                } else {
                                    showToast("Gift failed")
                                }
                            }
                        }
                    ) {
                        Text(text = "Gift")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            coinValue = ""
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            )
        }
    }
}