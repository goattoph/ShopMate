package ipt.dam.shopmate.models

import com.google.gson.annotations.SerializedName

data class Item (
    @SerializedName("itemId") val itemId: Int,
    @SerializedName("itemName") val itemName: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("price") val price: Double?,
    @SerializedName("amount") val amount: Int?,
    @SerializedName("isChecked") val isChecked: Boolean?
)
