package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(

	@field:SerializedName("SearchResponse")
	val searchResponse: List<SearchResponseItem>
)

data class SearchResponseItem(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("profilepic_url")
	val profilePic: String?
)
