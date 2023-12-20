package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("user")
    val user: UserLogin,

    @field:SerializedName("token")
    val token: String
)

data class UserLogin(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String
)
