package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: Status
)

data class Data(

	@field:SerializedName("confidence")
	val confidence: Any,

	@field:SerializedName("image_prediction")
	val imagePrediction: String
)

data class Status(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("message")
	val message: String
)
