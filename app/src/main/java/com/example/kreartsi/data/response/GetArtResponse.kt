package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class GetArtResponse(

	@field:SerializedName("GetArtResponse")
	val getArtResponse: List<GetArtResponseItem?>? = null
)

data class GetArtResponseItem(

	@field:SerializedName("likes_count")
	val likesCount: Int? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("artwork_id")
	val artworkId: Int? = null,

	@field:SerializedName("upload_date")
	val uploadDate: String? = null
)
