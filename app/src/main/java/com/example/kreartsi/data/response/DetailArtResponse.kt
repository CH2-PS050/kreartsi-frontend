package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class DetailArtResponse(

	@field:SerializedName("DetailArtResponse")
	val detailArtResponse: List<DetailArtResponseItem>
)

data class DetailArtResponseItem(

	@field:SerializedName("likes_count")
	val likesCount: Int,

	@field:SerializedName("category_id")
	val categoryId: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("caption")
	val caption: String,

	@field:SerializedName("artwork_id")
	val artworkId: Int,

	@field:SerializedName("upload_date")
	val uploadDate: String
)
