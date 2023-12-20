package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class LikeStatusResponse(

	@field:SerializedName("isLiked")
	val isLiked: Boolean
)
