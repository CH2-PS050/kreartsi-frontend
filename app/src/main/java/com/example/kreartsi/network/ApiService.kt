package com.example.kreartsi.network

import com.example.kreartsi.data.response.DeletePostResponse
import com.example.kreartsi.data.response.DetailArtResponseItem
import com.example.kreartsi.data.response.DonateRequest
import com.example.kreartsi.data.response.DonateResponse
import com.example.kreartsi.data.response.EditPostRequestBody
import com.example.kreartsi.data.response.EditPostResponse
import com.example.kreartsi.data.response.EditProfileResponse
import com.example.kreartsi.data.response.GetArtResponseItem
import com.example.kreartsi.data.response.LikeResponse
import com.example.kreartsi.data.response.LikeStatusResponse
import com.example.kreartsi.data.response.LoginRequest
import com.example.kreartsi.data.response.LoginResponse
import com.example.kreartsi.data.response.PredictResponse
import com.example.kreartsi.data.response.RegisterRequest
import com.example.kreartsi.data.response.RegisterResponse
import com.example.kreartsi.data.response.SaveResponse
import com.example.kreartsi.data.response.SaveStatusResponse
import com.example.kreartsi.data.response.SearchResponse
import com.example.kreartsi.data.response.SearchResponseItem
import com.example.kreartsi.data.response.UploadResponse
import com.example.kreartsi.data.response.UserDataResponse
import com.example.kreartsi.data.response.UserDataResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("users/register")
    fun register(@Body requestBody: RegisterRequest): Call<RegisterResponse>

    @POST("users/login")
    fun login(@Body loginRequestBody: LoginRequest): Call<LoginResponse>

    @GET("users/my-data")
    fun getUserData(@Header("token") authToken: String): Call<List<UserDataResponseItem>>

    @Multipart
    @POST("/predict")
    fun predictImage(
        @Part file: MultipartBody.Part
    ): Call<PredictResponse>

    @GET("arts")
    fun getArt(@Header("token") authToken: String): Call<List<GetArtResponseItem>>

    @GET("arts")
    fun getArtSorted(
        @Header("token") authToken: String,
        @Query("newest") newest: Boolean
    ): Call<List<GetArtResponseItem>>

    @GET("/api/v1/arts/{artworkId}")
    fun getArtDetails(
        @Header("token") authToken: String,
        @Path("artworkId") artworkId: Int
    ): Call<List<DetailArtResponseItem>>

    @GET("/api/v1/users/{userId}")
    fun getUserbyId(
        @Header("token") authToken: String,
        @Path("userId") userId: Int
    ): Call<List<UserDataResponseItem>>

    @GET("arts/my-arts")
    fun getUserArt(
        @Header("token") authToken: String
    ): Call<List<GetArtResponseItem>>

    @GET("users/{userId}/arts")
    fun getUserArtById(
        @Header("token") authToken: String,
        @Path("userId") userId: Int
    ): Call<List<GetArtResponseItem>>

    @GET("arts/my-saved-arts")
    fun getSavedArt(@Header("token") authToken: String): Call<List<GetArtResponseItem>>

    @GET("/api/v1/arts/isliked/{artworkId}")
    fun likedArt(
        @Header("token") authToken: String,
        @Path("artworkId") artworkId: Int
    ): Call<LikeStatusResponse>

    @POST("/api/v1/arts/like/{artworkId}")
    fun like(
        @Header("token") authToken: String,
        @Path("artworkId") artworkId: Int
    ): Call<LikeResponse>

    @POST("/api/v1/arts/unlike/{artworkId}")
    fun unlike(
        @Header("token") authToken: String,
        @Path("artworkId") artworkId: Int
    ): Call<LikeResponse>

    @GET("/api/v1/arts/issaved/{artworkId}")
    fun savedArt(
        @Header("token") authToken: String,
        @Path("artworkId") artworkId: Int
    ): Call<SaveStatusResponse>

    @POST("/api/v1/arts/save/{artworkId}")
    fun save(
        @Header("token") authToken: String,
        @Path("artworkId") artworkId: Int
    ): Call<SaveResponse>

    @DELETE("/api/v1/arts/unsave/{artworkId}")
    fun unsave(
        @Header("token") authToken: String,
        @Path("artworkId") artworkId: Int
    ): Call<SaveResponse>

    @POST("/api/v1/arts/donate/{userId}")
    fun donatePost(
        @Header("token") authToken:String,
        @Path("userId") userId: Int,
        @Body requestBody: DonateRequest
    ):Call<DonateResponse>

    @Multipart
    @POST("arts/upload")
    fun uploadArt(
        @Header("token") authToken: String,
        @Part file: MultipartBody.Part,
        @Part("caption") caption: RequestBody
    ): Call<UploadResponse>

    @DELETE("arts/{artworkId}")
    fun deletePost(
        @Header("token") authToken: String,
        @Path("artworkId") artworkId: Int
    ): Call<DeletePostResponse>

    @PUT("arts/caption/{artworkId}")
    fun editPost(
        @Header("token") authToken: String,
        @Path("artworkId") artworkId: Int,
        @Body editRequestBody: EditPostRequestBody
    ): Call<EditPostResponse>

    @Multipart
    @PUT("users/editprofile")
    fun updateProfile(
        @Header("token") authToken: String,
        @Part file: MultipartBody.Part,
    ): Call<EditProfileResponse>

    @GET("users/search")
    fun searchUsers(
        @Header("token") authToken: String,
        @Query("searchTerm") searchTerm: String
    ): Call<List<SearchResponseItem>>
}