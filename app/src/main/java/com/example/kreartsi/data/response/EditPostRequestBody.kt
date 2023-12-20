package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class EditPostRequestBody(

	@field:SerializedName("caption")
	val caption: String
)
