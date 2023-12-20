package com.example.kreartsi.screens.upload

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kreartsi.R
import com.example.kreartsi.common.theme.Black
import com.example.kreartsi.common.ui.IconVector
import com.example.kreartsi.common.ui.ImageDrawable
import com.example.kreartsi.common.ui.PrimaryButton
import com.example.kreartsi.common.ui.SecondaryButton
import com.example.kreartsi.navigation.KreartsiScreens
import com.example.kreartsi.screens.profile.ProfileViewModel
import com.example.kreartsi.screens.reduceFileImage
import com.example.kreartsi.screens.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun UploadScreen(
    navController: NavController = rememberNavController()
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

    val viewModel: UploadViewModel = hiltViewModel()
    var token = viewModel.getToken()

    var description by remember {
        mutableStateOf("")
    }

    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                    Image(bitmap = btm.asImageBitmap(),
                        contentDescription =null,
                        modifier = Modifier
                            .padding(
                                start = 32.dp,
                                top = 56.dp,
                                end = 32.dp,
                                bottom = 24.dp
                            )
                            .size(300.dp),
                        contentScale = ContentScale.Fit)
                }
            }
        } else {
            ImageDrawable(
                modifier = Modifier
                    .padding(
                        start = 32.dp,
                        top = 56.dp,
                        end = 32.dp,
                        bottom = 24.dp
                    )
                    .size(300.dp),
                res = R.drawable.detect_placeholder,
                contentScale = ContentScale.Fit)
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(start = 45.dp, bottom = 12.dp, end = 45.dp),
            value = description,
            onValueChange = {
                description = it
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Black
            ))
        SecondaryButton(
            modifier = Modifier.padding(
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

                    val requestBody = description.toRequestBody("text/plain".toMediaType())

                    val multipartBody = MultipartBody.Part.createFormData(
                        "file",
                        imageFile.name,
                        requestImageFile
                    )

                    viewModel.uploadPost(token!!, multipartBody, requestBody)

                    viewModel.isUploaded.observe(lifecycleOwner){
                        if(it){
                            navController.navigate(route = KreartsiScreens.HomeScreen.routeName)
                            showToast("Success Uploaded")
                        } else {
                            showToast("Upload failed!")
                        }
                    }
//                    if (viewModel.isUploaded.value != false){
//                        navController.navigate(route = KreartsiScreens.HomeScreen.routeName)
//                        showToast("Success Uploaded")
//                    } else {
//                        showToast("Upload failed!")
//                    }
                }
            }
        )
    }
}