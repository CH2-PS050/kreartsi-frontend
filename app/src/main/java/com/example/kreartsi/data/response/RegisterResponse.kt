package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("user")
    val user: User
)

data class User(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String
)
