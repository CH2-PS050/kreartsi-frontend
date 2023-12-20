package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("art")
	val art: Art,

	@field:SerializedName("message")
	val message: String
)

data class Art(

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("caption")
	val caption: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("categoryId")
	val categoryId: Int
)
