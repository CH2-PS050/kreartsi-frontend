package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

//data class UserDataResponse(
//
//	@field:SerializedName("UserDataResponse")
//	val userDataResponse: List<UserDataResponseItem>
//)
//
//data class UserDataResponseItem(
//
//	@field:SerializedName("user_id")
//	val userId: Int,
//
//	@field:SerializedName("coins")
//	val coins: Int,
//
//	@field:SerializedName("email")
//	val email: String,
//
//	@field:SerializedName("username")
//	val username: String
//)

data class UserDataResponse(
	@field:SerializedName("UserDataResponse")
	val userDataResponse: List<UserDataResponseItem>
)

data class UserDataResponseItem(
	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("coins")
	val coins: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("profilepic_url")
	val pothoUrl: String
)
