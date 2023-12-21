package com.example.kreartsi.screens.detailPost

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DeleteOutline
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.kreartsi.common.theme.Gray
import com.example.kreartsi.common.theme.White
import com.example.kreartsi.common.ui.IconVector
import com.example.kreartsi.common.ui.ImageDrawable
import com.example.kreartsi.common.ui.boxShadow
import com.example.kreartsi.data.response.EditPostRequestBody
import com.example.kreartsi.screens.detail.DetailScreenViewModel
import com.vdurmont.emoji.EmojiParser
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EditPostScreen(
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

    LaunchedEffect(token) {
        if (token != null) {
            viewModel.getDetailArt(
                token,
                artworkId!!.toInt()
            ) { receivedImage, receivedCaption, receivedLike, receivedDate, receivedUid ->
                imageUrl = receivedImage
                caption = EmojiParser.parseToUnicode(receivedCaption)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Headers(navController, like.toString(), imageUrl)
        Spacer(modifier = Modifier.padding(top = 13.dp))
        BodyEdit(userName, caption, date, token, navController, artworkId?.toInt())
    }
}

@Composable
fun Headers(
    navController: NavController,
    like: String?,
    photoProfile: String?
) {
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
                IconVector(
                    vector = Icons.Default.FavoriteBorder,
                    tint = Color22
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = like!!,
                    fontWeight = FontWeight.W700,
                    fontSize = 10.sp)
                Spacer(modifier = Modifier.weight(1f))
                IconVector(
                    vector = Icons.Default.BookmarkBorder,
                    tint = Color22
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyEdit(
    userName: String?,
    description: String?,
    date: String?,
    token: String?,
    navController: NavController,
    artworkId: Int?
) {
    val viewModel: DetailScreenViewModel = hiltViewModel()

    var showDialog by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    var descriptionEdit by remember {
        mutableStateOf(description)
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
                text = userName!!,
                fontWeight = FontWeight.W700,
                fontSize = 12.sp)
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(24.dp))

        (if(descriptionEdit == ""){
            description
        }else{
            descriptionEdit!!
        })?.let {
            TextField(
                value = it,
                onValueChange = {descriptionEdit = it},
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = Black)
        Text(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 32.dp)
                .fillMaxWidth(),
            text = output_date,
            fontSize = 12.sp,
            color = Color0A,
            textAlign = TextAlign.End)
        Row(
            modifier = Modifier.padding(horizontal = 45.dp),
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp, bottom = 32.dp),
                onClick = {
                    viewModel.editPost(token!!, artworkId!!, EditPostRequestBody(EmojiParser.parseToAliases(descriptionEdit!!)))
                    viewModel.isChanged.observe(lifecycleOwner){
                        if(it){
                            navController.navigate("art_post_detail/${artworkId}"){
                                popUpTo("art_post_detail/${artworkId}") {
                                    inclusive = true
                                }
                            }
                            showToast("Success Edited")
                        } else {
                            showToast("Failed")
                        }
                    }
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Black
                )) {
                Text(
                    text = "Save",
                    fontSize = 12.sp,
                    color = White
                )
            }
        }
    }
}