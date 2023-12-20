package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class DonateResponse(

	@field:SerializedName("donation")
	val donation: Donation,

	@field:SerializedName("message")
	val message: String
)

data class Donation(

	@field:SerializedName("recipientUserId")
	val recipientUserId: String,

	@field:SerializedName("donorUserId")
	val donorUserId: Int,

	@field:SerializedName("donatedAmount")
	val donatedAmount: Int,

	@field:SerializedName("id")
	val id: Int
)
