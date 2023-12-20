package com.example.kreartsi.screens.detect

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kreartsi.R
import com.example.kreartsi.common.theme.Black
import com.example.kreartsi.common.theme.White
import com.example.kreartsi.common.ui.IconVector
import com.example.kreartsi.common.ui.ImageDrawable
import com.example.kreartsi.common.ui.PrimaryButton
import com.example.kreartsi.common.ui.SecondaryButton
import com.example.kreartsi.data.response.PredictResponse
import com.example.kreartsi.network.ApiConfig
import com.example.kreartsi.screens.reduceFileImage
import com.example.kreartsi.screens.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun DetectScreen(
    navController: NavController = rememberNavController()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var visible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    var predict by remember { mutableStateOf("") }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
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
            tint = Black)

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
                                top = 24.dp,
                                end = 32.dp,
                                bottom = 24.dp
                            )
                            .size(250.dp),
                        contentScale = ContentScale.Crop)
                }
            }
        } else {
            ImageDrawable(
                modifier = Modifier
                    .padding(
                        start = 32.dp,
                        top = 24.dp,
                        end = 32.dp,
                        bottom = 24.dp
                    )
                    .size(250.dp),
                res = R.drawable.detect_placeholder,
                contentScale = ContentScale.Crop)
        }

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Result",
            fontWeight = FontWeight.W700,
            fontSize = 16.sp,
            textAlign = TextAlign.Center)

        if(loading){
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 25.dp),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = Color.Black
                )
            }
        } else{
            if (visible){
                Button(
                    modifier = Modifier
                        .padding(top = 25.dp, bottom = 25.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = {},
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Black
                    )) {
                    Text(
                        text = predict,
                        fontSize = 12.sp,
                        color = White
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }


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
            label = "Detect",
            onClick = {
                imageUri?.let {
                    val imageFile = uriToFile(imageUri!!, context).reduceFileImage()
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

                    val multipartBody = MultipartBody.Part.createFormData(
                        "file",
                        imageFile.name,
                        requestImageFile
                    )

                    lifecycleOwner.lifecycleScope.launch {
                        try {
                            loading = true
                            val apiService = ApiConfig.getApiService("")
                            val response = apiService.predictImage(multipartBody)
                            response.enqueue(object : Callback<PredictResponse> {
                                override fun onResponse(
                                    call: Call<PredictResponse>,
                                    response: Response<PredictResponse>
                                ) {
                                    if (response.isSuccessful){
                                        var predictvalue = response.body()?.data?.imagePrediction
                                        Log.d(ContentValues.TAG, "onSuccess: ${predictvalue}")
                                        predict = predictvalue!!
                                        loading = false
                                        visible = true
                                    } else {
                                        loading = false
                                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                                    }
                                }
                                override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                                    loading = false
                                    Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                                }
                            })

                        } catch (e: HttpException) {
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorResponse = Gson().fromJson(errorBody, PredictResponse::class.java)
                        }
                    }

                }
            }
        )
    }
}

//private const val MAXIMAL_SIZE = 1000000
//private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
//private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
//
//fun createCustomTempFile(context: Context): File {
//    val filesDir = context.externalCacheDir
//    return File.createTempFile(timeStamp, ".jpg", filesDir)
//}
//
//fun uriToFile(imageUri: Uri, context: Context): File {
//    val myFile = createCustomTempFile(context)
//    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
//    val outputStream = FileOutputStream(myFile)
//    val buffer = ByteArray(1024)
//    var length: Int
//    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
//    outputStream.close()
//    inputStream.close()
//    return myFile
//}
//
//@RequiresApi(Build.VERSION_CODES.Q)
//fun File.reduceFileImage(): File {
//    val file = this
//    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
//    var compressQuality = 100
//    var streamLength: Int
//    do {
//        val bmpStream = ByteArrayOutputStream()
//        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
//        val bmpPicByteArray = bmpStream.toByteArray()
//        streamLength = bmpPicByteArray.size
//        compressQuality -= 5
//    } while (streamLength > MAXIMAL_SIZE)
//    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
//    return file
//}
//@RequiresApi(Build.VERSION_CODES.Q)
//fun Bitmap.getRotatedBitmap(file: File): Bitmap? {
//    val orientation = ExifInterface(file).getAttributeInt(
//        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
//    )
//    return when (orientation) {
//        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
//        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
//        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
//        ExifInterface.ORIENTATION_NORMAL -> this
//        else -> this
//    }
//}
//
//fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
//    val matrix = Matrix()
//    matrix.postRotate(angle)
//    return Bitmap.createBitmap(
//        source, 0, 0, source.width, source.height, matrix, true
//    )
//}
