package com.example.kreartsi.screens.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.kreartsi.common.theme.Black
import com.example.kreartsi.common.ui.IconVector
import com.example.kreartsi.common.ui.PrimaryButton
import com.example.kreartsi.common.ui.SecondaryButton
import com.example.kreartsi.navigation.KreartsiScreens
import com.example.kreartsi.screens.reduceFileImage
import com.example.kreartsi.screens.upload.UploadViewModel
import com.example.kreartsi.screens.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController = rememberNavController(),
    userId: String?
) {
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModel: ProfileViewModel = hiltViewModel()
    var token = viewModel.getToken()

    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

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
                } else  {
                    photoProfile =
                        "https://firebasestorage.googleapis.com/v0/b/paaproject-4a080.appspot.com/o/image%2013.png?alt=media&token=82144bcb-411e-4912-989e-b0085234fcfe"
                }
                receivedUserid = receivedUid
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        IconVector(
            modifier = Modifier.
            clickable {
                navController.navigateUp()
            },
            vector = Icons.Default.ArrowBack,
            tint = Black
        )

        if (imageUri != null){
            imageUri.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver,it)

                } else {
                    val source = ImageDecoder
                        .createSource(context.contentResolver,it!!)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

                bitmap.value?.let {  btm ->
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 56.dp,
                                bottom = 24.dp
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Image(bitmap = btm.asImageBitmap(),
                            contentDescription =null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(250.dp),
                            contentScale = ContentScale.Crop)
                    }
                }
            }
        } else {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 56.dp,
                        bottom = 24.dp
                    ),
                contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    model = photoProfile,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(250.dp),
                    contentScale = ContentScale.Crop)
            }
        }

        SecondaryButton(
            modifier = Modifier.padding(
                top = 64.dp,
                start = 45.dp,
                bottom = 16.dp,
                end = 45.dp),
            label = "Gallery",
            onClick = {
                launcher.launch("image/*")
            }
        )
        PrimaryButton(
            modifier = Modifier.padding(
                start = 45.dp,
                end = 45.dp),
            label = "Upload",
            onClick = {
                imageUri?.let {
                    val imageFile = uriToFile(imageUri!!, context).reduceFileImage()
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

                    val multipartBody = MultipartBody.Part.createFormData(
                        "file",
                        imageFile.name,
                        requestImageFile
                    )

                    viewModel.editProfilePhoto(token!!, multipartBody)
                    viewModel.isChanged.observe(lifecycleOwner){
                        if (it){
                            showToast("Photo profile changed")
                            navController.navigate(
                                KreartsiScreens.ProfileScreen.routeName
                            ){
                                popUpTo(KreartsiScreens.ProfileScreen.routeName) {
                                    inclusive = true
                                }
                            }
                        } else{
                            showToast("Failed to change photo profile")
                        }
                    }

                }
            }
        )
    }
}