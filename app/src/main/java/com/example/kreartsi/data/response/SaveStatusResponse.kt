package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class SaveStatusResponse(

	@field:SerializedName("isSaved")
	val isSaved: Boolean
)
