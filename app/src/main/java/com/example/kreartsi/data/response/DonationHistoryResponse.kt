package com.example.kreartsi.data.response

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json                     = Json { allowStructuredMapKeys = true }
// val donationHistorytResponse = json.parse(DonationHistorytResponse.serializer(), jsonString)

data class DonationHistoryResponse (
    @SerializedName("donation_id")
    val donationID: Long,

    @SerializedName("donor_user_id")
    val donorUserID: Long,

    @SerializedName("artwork_id")
    val artworkID: Long? = null,

    @SerializedName("donated_amount")
    val donatedAmount: Long,

    @SerializedName("donation_date")
    val donationDate: String,

    @SerializedName("recipient_user_id")
    val recipientUserID: Long,

    @SerializedName("donor_username")
    val donorUsername: String
)
