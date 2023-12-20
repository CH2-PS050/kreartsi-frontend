package com.example.kreartsi.data.response

import com.google.gson.annotations.SerializedName

data class DonateRequest(

	@field:SerializedName("donatedAmount")
	val donatedAmount: Int
)
